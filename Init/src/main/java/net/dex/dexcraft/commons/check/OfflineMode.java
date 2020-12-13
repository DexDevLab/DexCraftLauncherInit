package net.dex.dexcraft.commons.check;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.ErrorAlerts;
import net.dex.dexcraft.commons.tools.JSONUtility;
import net.dex.dexcraft.commons.tools.Logger;


/**
 * Check the internet connection and if the program will
 * run on Offline Mode.
 * In the Offline Mode, the program won't download any files
 * needed to run the Launcher. If the Launcher need some
 * essential file to run and don't have downloaded it before,
 * the program will close.
 */
public class OfflineMode
{
  static ErrorAlerts alerts = new ErrorAlerts();
  static boolean keepOfflineMode = false;
  static Logger logger = new Logger();
  static JSONUtility ju = new JSONUtility();

  /**
   * Check if Offline Mode was enabled before.
   * @return if the program will keep running on Offline Mode
   * or not, depending if it was enabled on previous sessions.
   */
  public static boolean IsRunning()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Verificando status do Modo Offline...");
    if (ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "OfflineMode").equals("true"))
    {
      logger.log("INFO", "Modo Offline foi ativado na sessão anterior.");
      keepOfflineMode = alerts.offline(true);
    }
    if (!keepOfflineMode)
    {
      logger.log("INFO", "Modo Offline DESATIVADO pelo usuário.");
      ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "OfflineMode", "false");
      testConnection();
    }
    return keepOfflineMode;
  }

  /**
   * Check internet connection. Throws exceptions if
   * computer isn't connected to the internet.
   */
  private static void testConnection()
  {
    try
    {
      logger.log("INFO", "Testando conexão com a internet...");
      URL url = new URL("http://www.google.com.br");
      URLConnection connection = url.openConnection();
      connection.connect();
      logger.log("INFO", "Detectada conexão com a internet.");
    }
    catch (MalformedURLException ex)
    {
      logger.log(ex,"EXCEÇÃO EM OfflineCheck.OfflineCheck()");
      internetCheckException();
    }
    catch (IOException ex1)
    {
      logger.log(ex1,"EXCEÇÃO EM OfflineCheck.OfflineCheck()");
      internetCheckException();
    }
  }

  /**
   * Asks to user if they will keep the program running on
   * Offline Mode. If yes, the "OfflineMode" category in
   * the launcher script file asset will be changed.
   * @see net.dex.dexcraft.launcher.tools.DexCraftFiles.launcherProperties
   */
  private static void internetCheckException()
  {
    logger.log("INFO", "O usuário optou por não ativar o Modo Offline, mas não há conexão com a internet.");
    keepOfflineMode = alerts.offline(false);
    if (keepOfflineMode)
    {
      ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties","OfflineMode", "true");
      logger.log("INFO", "Modo Offline ATIVADO pelo usuário.");
    }
    else
    {
      testConnection();
    }
  }

}

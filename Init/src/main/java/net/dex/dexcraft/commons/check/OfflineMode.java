package net.dex.dexcraft.commons.check;


import static net.dex.dexcraft.commons.Commons.alerts;
import static net.dex.dexcraft.commons.Commons.logger;
import net.dex.dexcraft.commons.dto.SessionDTO;
import net.dex.dexcraft.commons.tools.Connections;


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
  static boolean keepOfflineMode = false;

  /**
   * Check if Offline Mode was enabled before.
   * @return if the program will keep running on Offline Mode
   * or not, depending if it was enabled on previous sessions.
   */
  public static boolean IsRunning()
  {
    logger.log("INFO", "Verificando status do Modo Offline...");
    if (SessionDTO.isOfflineModeOn())
    {
      logger.log("INFO", "Modo Offline foi ativado na sessão anterior.");
      keepOfflineMode = alerts.offline(true);
    }
    if (!keepOfflineMode)
    {
      logger.log("INFO", "Modo Offline DESATIVADO pelo usuário.");
      SessionDTO.setOfflineModeStatus(keepOfflineMode);
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
    if (!SessionDTO.isConnectionTestDisabled())
    {
      Connections con = new Connections();
      logger.log("INFO", "Testando conexão com a internet...");
      String ping = con.getPing("www.google.com.br");
      if (ping.equals("99999"))
      {
        internetCheckException();
      }
      else
      {
        logger.log("INFO", "Detectada conexão com a internet.");
      }
    }
    else
    {
      keepOfflineMode = false;
    }

//    try
//    {
//      logger.log("INFO", "Testando conexão com a internet...");
//      URL url = new URL("http://8aff09b1b49c.sn.mynetname.net:5023");
//      URLConnection connection = url.openConnection();
//      connection.connect();
//      logger.log("INFO", "Detectada conexão com a internet.");
//    }
//    catch (MalformedURLException ex)
//    {
//      logger.log(ex,"EXCEÇÃO EM OfflineCheck.OfflineCheck()");
//      internetCheckException();
//    }
//    catch (IOException ex1)
//    {
//      logger.log(ex1,"EXCEÇÃO EM OfflineCheck.OfflineCheck()");
//      internetCheckException();
//    }
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
      SessionDTO.setOfflineModeStatus(keepOfflineMode);
      logger.log("INFO", "Modo Offline ATIVADO pelo usuário.");
    }
    else
    {
      testConnection();
    }
  }

}

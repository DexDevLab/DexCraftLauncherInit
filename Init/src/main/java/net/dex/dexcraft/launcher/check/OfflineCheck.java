package net.dex.dexcraft.launcher.check;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.FileIO;
import net.dex.dexcraft.launcher.tools.Logger;


/**
 *
 *
 */
public class OfflineCheck
{
  static Alerts alerts = new Alerts();
  static boolean keepOfflineMode = false;
  static Logger logger = new Logger();

  public static boolean OfflineCheck()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Verificando status do Modo Offline...");
    if (DexCraftFiles.offlineModeFile.exists())
    {
      logger.log("INFO", "Modo Offline foi ativado na sessão anterior.");
      keepOfflineMode = alerts.offline(true);
    }
    if (!keepOfflineMode)
    {
      logger.log("INFO", "Modo Offline DESATIVADO pelo usuário.");
      if (DexCraftFiles.offlineModeFile.exists())
      {
        FileIO fio = new FileIO();
        fio.excluir(DexCraftFiles.offlineModeFile, false);
      }
      testConnection();
    }
    return keepOfflineMode;
  }

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
      logger.log(ex, "***ERRO***", "EXCEÇÃO EM OfflineCheck.OfflineCheck()");
      internetCheckException();
    }
    catch (IOException ex1)
    {
      logger.log(ex1, "***ERRO***", "EXCEÇÃO EM OfflineCheck.OfflineCheck()");
      internetCheckException();
    }
  }

  private static void internetCheckException()
  {
    logger.log("INFO", "O usuário optou por não ativar o Modo Offline, mas não há conexão com a internet.");
    keepOfflineMode = alerts.offline(false);
    if (keepOfflineMode)
    {
      FileIO fio = new FileIO();
      fio.criar(DexCraftFiles.offlineModeFile);
      logger.log("INFO", "Modo Offline ATIVADO pelo usuário.");
    }
    else
    {
      testConnection();
    }
  }

}

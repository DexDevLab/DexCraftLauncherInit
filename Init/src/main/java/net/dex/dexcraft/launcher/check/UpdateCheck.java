package net.dex.dexcraft.launcher.check;


import java.io.File;
import java.io.IOException;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.Cache;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.FileIO;
import net.dex.dexcraft.launcher.tools.Logger;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.writeStringToFile;


/**
 *
 *
 */
public class UpdateCheck
{

  public static boolean isOutdated(File localVersionFile, String versionProvisioned)
  {
    Alerts alerts = new Alerts();
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Identificando e comparando versão informada no arquivo \"" + localVersionFile + "\"...");
    if (!localVersionFile.exists())
    {
      try
      {
        logger.log("***ERRO***", "ARQUIVO \"" + localVersionFile.toString() + "\" NÃO FOI ENCONTRADO");
        FileIO file = new FileIO();
        logger.log("INFO", "Recriando arquivo provisionado \"" + localVersionFile.toString() + "\"");
        file.criar(localVersionFile);
        if (!localVersionFile.exists())
        {
          logger.log("***ERRO***", "ARQUIVO \"" + localVersionFile.toString() + "\" NÃO PÔDE SER RECRIADO");
          alerts.tryAgain();
          Cache.closeOnError();
        }
        writeStringToFile(localVersionFile, "0", "UTF-8");
      }
      catch (IOException ex)
      {
        logger.log("***ERRO***", "EXCEÇÃO EM UpdateChecker.isOutdated(File, File)");
        alerts.exceptionHandler(ex, "ERRO DURANTE A ANÁLISE DE VERSÃO");
        Cache.closeOnError();
      }
    }
    try
    {
      String versionInstalled = FileUtils.readFileToString(localVersionFile, "UTF-8");
      if (!versionInstalled.equals(versionProvisioned))
      {
        logger.log("INFO", "Recurso se encontra na versão " + versionInstalled + " e está desatualizado.");
      }
      return (!versionInstalled.equals(versionProvisioned));
    }
    catch (IOException ex)
    {
      logger.log("***ERRO***", "EXCEÇÃO EM UpdateChecker.isOutdated(File, String)");
      alerts.exceptionHandler(ex, "ERRO DURANTE A ANÁLISE DE VERSÃO");
      Cache.closeOnError();
    }
    return false;
  }

}

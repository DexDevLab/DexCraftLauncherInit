package net.dex.dexcraft.launcher.check;


import java.io.File;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.Cache;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.Logger;
import net.dex.dexcraft.launcher.tools.ScriptFileReader;


/**
 *
 *
 */
public class UpdateCheck
{

  public static boolean isOutdated(File versionFile, String categoryWithVersionData, String versionProvisioned)
  {
    Alerts alerts = new Alerts();
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Identificando e comparando versão informada no arquivo \"" + versionFile.toString() + "\"...");
    if (!versionFile.exists())
    {
      logger.log("***ERRO***", "ARQUIVO \"" + versionFile.toString() + "\" NÃO FOI ENCONTRADO");
      alerts.tryAgain();
      Cache.closeOnError();
    }
    ScriptFileReader sfr = new ScriptFileReader();
    String versionInstalled = sfr.getOutputEntry(versionFile, categoryWithVersionData);
    if (!versionInstalled.equals(versionProvisioned))
    {
      logger.log("INFO", "Recurso se encontra na versão " + versionInstalled + " e está desatualizado.");
    }
    return (!versionInstalled.equals(versionProvisioned));
  }

}

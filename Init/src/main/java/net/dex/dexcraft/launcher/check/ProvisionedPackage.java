package net.dex.dexcraft.launcher.check;


import java.io.File;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.Close;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.JSONUtility;
import net.dex.dexcraft.launcher.tools.Logger;


/**
 * Check if provisioned package is outdated.
 */
public class ProvisionedPackage
{

  /**
   * Check if specific package is out of date, considering
   * version value comparison.It doesn't have an update list,
 it just compares the text between the local file containing
 the version and the online one.
   * @param versionFile the script file contatining the version data.
   * @param versionFileCategory the name of the category which
   * contains the version data.
   * @param objectName the provisioned component's JSON key. <br>
   * Both script files (the online one and the local one)
   * MUST use the same category, with the exactly same name.
   * @param versionProvisioned the version contained on the online
   * reference script file
   * @return if the package version is outdated (true) or not (false)
   */
  public static boolean isOutdated(File versionFile, String versionFileCategory,String objectName, String versionProvisioned)
  {
    //Logger settings and custom alerts initialization
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
      Close.close(1);
    }
    JSONUtility ju = new JSONUtility();
    String versionInstalled = ju.readValue(versionFile, versionFileCategory, objectName);
    if (!versionInstalled.equals(versionProvisioned))
    {
      logger.log("INFO", "Recurso se encontra na versão " + versionInstalled + " e está desatualizado.");
    }
    return (!versionInstalled.equals(versionProvisioned));
  }

}

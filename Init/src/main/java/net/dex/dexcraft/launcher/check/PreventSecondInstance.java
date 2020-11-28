package net.dex.dexcraft.launcher.check;


import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.JSONUtility;
import net.dex.dexcraft.launcher.tools.Logger;


/**
 * Check if there is another instance of the program
 * currently running.
 */
public class PreventSecondInstance
{
  /**
   * Check if there is another instance of the program
   * currently running.
   */
  public static void PreventSecondInstance()
  {
    //Logger settings
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Verificando se já existe uma instância do programa na memória...");
    JSONUtility ju = new JSONUtility();
    if (ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "IsDexCraftLauncherRunning").equals("true"))
    {
      logger.log("INFO", "Foi encontrada uma instância do programa na memória.");
      Alerts alerts = new Alerts();
      alerts.doubleInstance();
    }
    else
    {
      logger.log("INFO", "Não foi encontrada uma instância do programa na memória.");
      ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "IsDexCraftLauncherRunning", "true");
    }
  }

}

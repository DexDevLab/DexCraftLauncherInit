package net.dex.dexcraft.commons.check;


import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.JSONUtility;
import net.dex.dexcraft.commons.tools.Logger;


/**
 * Check if there is another instance of the program
 * currently running.
 */
public class PreventSecondInstance
{
  /**
   * Check if there is another instance of the program
   * currently running.
   * @param checkKey the JSON key which the instance refers to.
   * @return if the instance is running already (true) or not(false)
   */
  public static boolean isThereAnotherInstance(String checkKey)
  {
    //Logger settings
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    JSONUtility ju = new JSONUtility();
    if (!ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", checkKey).equals("true"))
    {
      ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", checkKey, "true");
    }
    else
    {
      return true;
    }
    return false;
  }

}

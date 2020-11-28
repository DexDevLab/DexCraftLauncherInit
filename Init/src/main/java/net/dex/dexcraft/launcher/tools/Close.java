package net.dex.dexcraft.launcher.tools;


import java.io.File;

/**
 * Class for closing program properly,
 * removing temporary folders
 */
public class Close
{
  private static Logger logger;

  /**
   * Close the program properly, removing
   * temporary folders and unneeded lockers.
   * @param code the closing code:<br>
   * 0 - normal program close. Keeps the instance
   * lock, the log lock and the run folder, needed
   * to the Launcher Client and the DCBS.<br>
   * 1 - program exiting with error because of a
   * critical exception, or used when DCBS closes
   * completely.
   */
  public static void close(int code)
  {
    logger = new Logger();
    Clean clean = new Clean();
    JSONUtility ju = new JSONUtility();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Exit Code: " + code);
    switch (code)
    {
      case 0:
        logger.log("INFO", "Limpando Cache...");
        clean.excluir(DexCraftFiles.adminCheck, false);
        clean.excluir(DexCraftFiles.tempFolder, true);
        break;
      case 1:
        logger.log("INFO", "Fechando...");
        clean.excluir(DexCraftFiles.adminCheck, false);
        clean.excluir(DexCraftFiles.tempFolder, true);
        clean.excluir(DexCraftFiles.logLock, false);
        ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "IsDexCraftLauncherRunning", "false");
        break;
      default:
    }
    System.exit(0);
  }

  /**
   * Since its common the FileIO Class provide an
   * exception which closes the program,<br>
   * in order to do not prevent program running
   * in absence of some file just for verification
   * the method was overwriten.
   */
  private static class Clean extends FileIO
  {
    @Override
    public void excluir(File source, boolean includeParentDir)
    {
      if (source.exists())
      {
        super.excluir(source, includeParentDir);
      }
      else
      {
        logger.log("INFO", "SOURCE \"" + source.toString() + "\" não foi encontrado.");
      }
    }
  }

}

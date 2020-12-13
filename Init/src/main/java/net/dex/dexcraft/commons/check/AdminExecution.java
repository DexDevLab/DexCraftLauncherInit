package net.dex.dexcraft.commons.check;


import java.io.IOException;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.ErrorAlerts;
import net.dex.dexcraft.commons.tools.FileIO;
import net.dex.dexcraft.commons.tools.Logger;
import org.apache.commons.io.FileUtils;

/**
 * Check if application is running as Administrator.
 */
public class AdminExecution
{
  /**
   * Check if application is running as Administrator.
   */
  public static void AdminExecution()
  {
    //Logger settings
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    FileIO fio = new FileIO();
    /** This file can be created only under Admin permissions,
        since it's created in C:**/
    if (!DexCraftFiles.adminCheck.exists())
    {
      try
      {
        FileUtils.touch(DexCraftFiles.adminCheck);
      }
      catch (IOException ex)
      {
        logger.log(ex, "NÃO FOI POSSÍVEL CRIAR O DexCraftFiles.adminCheck");
        ErrorAlerts alerts = new ErrorAlerts();
        alerts.noAdmin();
      }
    }
    logger.log("INFO", "O software está sendo executado como Administrador.");
    fio.excluir(DexCraftFiles.adminCheck, false);
  }

}

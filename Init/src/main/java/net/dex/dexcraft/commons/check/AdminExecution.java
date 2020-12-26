package net.dex.dexcraft.commons.check;


import java.io.IOException;
import static net.dex.dexcraft.commons.Commons.alerts;
import static net.dex.dexcraft.commons.Commons.logger;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.FileIO;
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
        alerts.noAdmin();
      }
    }
    logger.log("INFO", "O software está sendo executado como Administrador.");
    fio.excluir(DexCraftFiles.adminCheck, false);
  }

}

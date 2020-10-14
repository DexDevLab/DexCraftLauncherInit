/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dex.dexcraft.launcher.check;


import java.io.IOException;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.FileIO;
import net.dex.dexcraft.launcher.tools.Logger;
import org.apache.commons.io.FileUtils;


/**
  * Verifica se a aplicação está sendo executada
  * como Administrador.
  */
public class AdmCheck
{

  public static void AdmCheck()
  {
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    FileIO fio = new FileIO();
    if (!DexCraftFiles.adminCheck.exists())
    {
      try
      {
        FileUtils.touch(DexCraftFiles.adminCheck);
      }
      catch (IOException ex)
      {
        logger.log(ex, "***ERRO***", "NÃO FOI POSSÍVEL CRIAR O DexCraftFiles.adminCheck");
        Alerts alerts = new Alerts();
        alerts.noAdmin();
      }
    }
    else
    {
      fio.excluir(DexCraftFiles.adminCheck, false);
    }
    logger.log("INFO", "O software está sendo executado como Administrador.");
    fio.excluir(DexCraftFiles.adminCheck, false);
  }

}

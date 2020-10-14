package net.dex.dexcraft.launcher.init;

import java.io.IOException;
import net.dex.dexcraft.launcher.check.UpdateCheck;
import static net.dex.dexcraft.launcher.init.Init.alerts;
import static net.dex.dexcraft.launcher.init.Init.changeStatus;
import static net.dex.dexcraft.launcher.init.Init.logger;
import static net.dex.dexcraft.launcher.init.Init.sfr;
import static net.dex.dexcraft.launcher.init.Init.ui;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.Download;
import net.dex.dexcraft.launcher.tools.FileIO;
import net.dex.dexcraft.launcher.tools.Install;
import org.apache.commons.io.FileUtils;


/**
 *
 *
 */
public class Validate
{

  public static void resources()
  {
    if ( (!DexCraftFiles.resFolder.exists()) || (DexCraftFiles.resFolder.listFiles().length == 0) )
    {
      changeStatus("Baixando recursos...");
      String resURL = sfr.getOutputEntry(DexCraftFiles.coreFile, "LauncherResourceFile");
      Download downloadRes = new Download();
      Thread threadDownloadRes = new Thread(()->
      {
        downloadRes.zipResource(resURL, DexCraftFiles.tempFolder, DexCraftFiles.resZip);
      });
      threadDownloadRes.start();
      while(threadDownloadRes.isAlive())
      {
        try
        {
          Thread.sleep(1100);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO em Validate.resources()");
        }
        logger.log("INFO", downloadRes.getTimeEstimatedMsg());
        changeStatus("Baixando recursos... " + downloadRes.getProgressPercent() + "% concluído");
      }
      ui.changeProgress(true, 60, 40);
      changeStatus("Instalando recursos...");
      Install installRes = new Install();
      Thread threadInstallRes = new Thread(()->
      {
        installRes.downloadedZipResource(DexCraftFiles.resZip, DexCraftFiles.resFolder);
      });
      threadInstallRes.start();
      String checkFile = " ";
      while(threadInstallRes.isAlive())
      {
        if (!(installRes.getInstallingFileName()).equals(""))
        {
          if (!installRes.getInstallingFileName().equals(checkFile))
          {
            changeStatus("Instalando recursos... " + installRes.getInstallingFilePosition() + " / " + installRes.getTotalFilesQuantity());
            checkFile = installRes.getInstallingFileName();
          }
          if (Integer.parseInt(installRes.getInstallingFilePosition()) == (Integer.parseInt(installRes.getTotalFilesQuantity())-1))
          {
            changeStatus("Instalando recursos... " + installRes.getTotalFilesQuantity() + " / " + installRes.getTotalFilesQuantity());
          }
        }
        try
        {
          Thread.sleep(10);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO em Validate.resources()");
        }
      }
      if ( (!DexCraftFiles.resFolder.exists()) || (DexCraftFiles.resFolder.listFiles().length == 0) )
      {
        logger.log("***ERRO***", "RECURSO PROVISIONADO INDISPONÍVEL");
        alerts.tryAgain();
      }
    }
    logger.log("INFO", "Recursos instalados. Validando atalhos...");
    if ( (!DexCraftFiles.shortcutDefaultDesktop.exists()) | (!DexCraftFiles.shortcutProgramFolder.exists()) | (!DexCraftFiles.shortcutUserDesktop.exists()) )
    {
      FileIO file = new FileIO();
      file.copiar(DexCraftFiles.shortcutSrc, DexCraftFiles.shortcutDefaultDesktop);
      file.copiar(DexCraftFiles.shortcutSrc, DexCraftFiles.shortcutProgramFolder);
      try
      {
        FileUtils.copyFile(DexCraftFiles.shortcutSrc, DexCraftFiles.shortcutUserDesktop);
      }
      catch (IOException ex)
      {
        logger.log(ex, "***ERRO***", "EXCEÇÃO em Validate.resources()");
      }
    }
  }


  public static void launcher()
  {
    if(UpdateCheck.isOutdated(DexCraftFiles.versInstalledLauncherFile, sfr.getOutputEntry(DexCraftFiles.coreFile, "DexCraftLauncherVersion")))
    {
      changeStatus("Baixando atualizações do Launcher...");
      String dclURL = sfr.getOutputEntry(DexCraftFiles.coreFile, "DCLUpdate");
      Download downloadDCL = new Download();
      Thread threadDownloadDCL = new Thread(()->
      {
        downloadDCL.zipResource(dclURL, DexCraftFiles.tempFolder, DexCraftFiles.updateLauncherZip);
      });
      threadDownloadDCL.start();
      while(threadDownloadDCL.isAlive())
      {
        try
        {
          Thread.sleep(1100);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO em Validate.launcher()");
        }
        logger.log("INFO", downloadDCL.getTimeEstimatedMsg());
        changeStatus("Baixando atualizações do Launcher... " + downloadDCL.getProgressPercent() + "% concluído");
      }
      ui.changeProgress(true, 80, 40);
      Install installDCL = new Install();
      Thread threadInstallDCL = new Thread(()->
      {
        installDCL.downloadedZipResource(DexCraftFiles.updateLauncherZip, DexCraftFiles.launcherFolder);
      });
      threadInstallDCL.start();
      String checkFile = " ";
      while(threadInstallDCL.isAlive())
      {
        if (!(installDCL.getInstallingFileName()).equals(""))
        {
          if (!installDCL.getInstallingFileName().equals(checkFile))
          {
            changeStatus("Instalando os arquivos do Launcher... " + installDCL.getInstallingFilePosition() + " / " + installDCL.getTotalFilesQuantity());
            checkFile = installDCL.getInstallingFileName();
          }
          if (Integer.parseInt(installDCL.getInstallingFilePosition()) == (Integer.parseInt(installDCL.getTotalFilesQuantity())-1))
          {
            changeStatus("Instalando os arquivos do Launcher... " + installDCL.getTotalFilesQuantity() + " / " + installDCL.getTotalFilesQuantity());
          }
        }
        try
        {
          Thread.sleep(10);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO em Validate.launcher()");
        }
      }
      if(UpdateCheck.isOutdated(DexCraftFiles.versInstalledLauncherFile, sfr.getOutputEntry(DexCraftFiles.coreFile, "DexCraftLauncherVersion")))
      {
        logger.log("***ERRO***", "FALHA NA ATUALIZAÇÃO DO LAUNCHER");
        alerts.tryAgain();
      }
    }
  }
}

package net.dex.dexcraft.launcher.init;

import java.io.File;
import java.io.IOException;
import net.dex.dexcraft.commons.check.PreventSecondInstance;
import net.dex.dexcraft.commons.check.ProvisionedPackage;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.Download;
import net.dex.dexcraft.commons.tools.FileIO;
import net.dex.dexcraft.commons.tools.Install;
import net.dex.dexcraft.commons.tools.JSONUtility;
import static net.dex.dexcraft.launcher.init.Init.alerts;
import static net.dex.dexcraft.launcher.init.Init.changeStatus;
import static net.dex.dexcraft.launcher.init.Init.initUI;
import static net.dex.dexcraft.launcher.init.Init.logger;
import org.apache.commons.io.*;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Wrap the update check, download, install and
 * verify processes in one single class, with UI
 * update.
 */
public class Validate
{

  // Launcher version variables
  public static String dexCraftLauncherInitVersion;
  public static String dexCraftLauncherClientVersion;
  public static String dexCraftBackgroundServicesVersion;
  public static String dexCraftFactionsPatchVersion;
  public static String dexCraftPixelmonPatchVersion;
  public static String dexCraftVanillaPatchVersion;
  public static String dexCraftBetaPatchVersion;

  // FTP server variables
  public static String ftpAddress;
  public static int ftpPort;
  public static String ftpUser;
  public static String ftpPassword;
  public static String ftpWorkingDir;
  public static FTPClient ftpClient;

  // Database server variables
  public static String dbClass;
  public static String dbDriver;
  public static String dbName;
  public static String dbAddress;
  public static String dbPort;
  public static String dbUser;
  public static String dbPassword;

  // Easter Egg URL
  public static String easterEggURL;

  // Last user logged
  public static String lastUser;

  // Last server chosen
  public static String lastServer;

  // If offline mode was chosen
  public static boolean offlineMode = false;

  // JSON Utility instance
  private static JSONUtility ju = new JSONUtility();


  /**
   * Prepare version values's static variables.
   */
  public static void versions()
  {
    dexCraftLauncherInitVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DexCraftLauncherInitVersion");
    dexCraftLauncherClientVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DexCraftLauncherVersion");
    dexCraftBackgroundServicesVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DexCraftBackgroundServicesVersion");
    dexCraftFactionsPatchVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DCPatchVersion");
    dexCraftPixelmonPatchVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DCPXPatchVersion");
    dexCraftVanillaPatchVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DCVNPatchVersion");
    dexCraftBetaPatchVersion = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "DCBPatchVersion");
  }

  /**
   * Prepare connection's static variables.
   */
  public static void connectionAssets()
  {
    try
    {
      easterEggURL = ju.readValue(DexCraftFiles.coreFileLinkFile, "URLs", "EasterEggURL");
      ftpAddress = ju.readValue(DexCraftFiles.coreFile, "FtpServer", "ServerWebAddress");
      ftpPort = Integer.parseInt(ju.readValue(DexCraftFiles.coreFile, "FtpServer", "ServerPort"));
      ftpUser = ju.readValue(DexCraftFiles.coreFile, "FtpServer", "ServerUser");
      ftpPassword = ju.readValue(DexCraftFiles.coreFile, "FtpServer", "ServerPassword");
      ftpWorkingDir = ju.readValue(DexCraftFiles.coreFile, "FtpServer", "ServerPlayerDataLocation");
      ftpClient = new FTPClient();
      ftpClient.connect(ftpAddress, ftpPort);
      ftpClient.login(ftpUser, ftpPassword);
      ftpClient.enterLocalPassiveMode();
      ftpClient.changeWorkingDirectory(ftpWorkingDir + "/");
      logger.log("INFO", "FTP: Conectado!");
      dbClass = ju.readValue(DexCraftFiles.coreFile, "DBServer", "DBClass");
      dbDriver = ju.readValue(DexCraftFiles.coreFile, "DBServer", "DBDriver");
      dbName = ju.readValue(DexCraftFiles.coreFile, "DBServer", "DBName");
      dbAddress = ju.readValue(DexCraftFiles.coreFile, "DBServer", "ServerWebAddress");
      dbPort = ju.readValue(DexCraftFiles.coreFile, "DBServer", "ServerPort");
      dbUser = ju.readValue(DexCraftFiles.coreFile, "DBServer", "ServerUser");
      dbPassword = ju.readValue(DexCraftFiles.coreFile, "DBServer", "ServerPassword");
      logger.log("INFO",  dbDriver.toUpperCase() + ": " + "Assets carregados.");
    }
    catch (IOException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Validate.ftpConnection()");
    }
  }


  /**
   * Feed last server variable with data<br>
   * from the Launcher Properties JSON file.
   */
  public static void getLastServer()
  {
    lastServer = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "LastServerIndex");
  }

  /**
   * Fills JSON object with the last server<br>
   * selected on login and updates the static
   * variable.
   * @param index the server index from server list.
   */
  public static void setLastServer(Integer index)
  {
    ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "LastServerIndex", Integer.toString(index));
    getLastServer();
  }

  /**
   * Feed last user variable with data<br>
   * from the Launcher Properties JSON file.
   */
  public static void getLastUser()
  {
    lastUser = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "LastUser");
    if (lastUser.equals("null"))
    {
      lastUser = "";
    }
  }

  /**
   * Fills JSON object with the last user logged on launcher<br>
   * and updates the static variable.
   * @param user the username.
   */
  public static void setLastUser(String user)
  {
    ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "LastUser", user);
    getLastUser();
  }

  /**
   * Feed offline mode variable with data<br>
   * from Launcher Properties JSON file.
   */
  public static void setOfflineMode()
  {
    offlineMode = ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "OfflineMode").equals("true");
  }

  /**
   * Validates Launcher instance, preventing users from<br>
   * opening Launcher without Init.
   * @param instanceName the name os instance (Init, Client
   * or DCBS).
   */
  public static void instance(String instanceName)
  {
    boolean isInstanceInvalid = true;
    switch (instanceName)
    {
      case "Init":
        if (DexCraftFiles.logLock.exists())
        {
          ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "IsDexCraftLauncherInitRunning", "true");
          isInstanceInvalid = true;
        }
        else
        {
          isInstanceInvalid = false;
        }
        break;
      case "Client":
        if (PreventSecondInstance.isThereAnotherInstance("IsDexCraftLauncherInitRunning"))
        {
          isInstanceInvalid = PreventSecondInstance.isThereAnotherInstance("IsDexCraftLauncherClientRunning");
        }
        break;
      case "DCBS":
        if ( (PreventSecondInstance.isThereAnotherInstance("IsDexCraftLauncherInitRunning"))
              && PreventSecondInstance.isThereAnotherInstance("IsDexCraftLauncherClientRunning") )
        {
          isInstanceInvalid = PreventSecondInstance.isThereAnotherInstance("IsDexCraftBackgroundServicesRunning");
        }
        break;
      default:
        break;
    }
    if (isInstanceInvalid)
    {
      System.out.println("Foi encontrada uma instância do programa na memória.");
      alerts.doubleInstance();
    }
    else
    {
      System.out.println("Não foi encontrada uma instância do programa na memória.");
    }
  }

  /**
   * Used on Init.<br>
   * Prepares launcher cache files and folders.
   */
  public static void cache()
  {
    FileIO fio = new FileIO();
    fio.excluir(DexCraftFiles.tempFolder, true);
    ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "IsDexCraftLauncherClientRunning", "false");
    ju.editValue(DexCraftFiles.launcherProperties, "LauncherProperties", "IsDexCraftBackgroundServicesRunning", "false");
  }

  /**
   * Validate the Launcher resources, which are the
   * program backgrounds, runtime sounds etc.
   */
  public static void resources()
  {
    //If the resource folder was updated remotely, delete the previous one
    if (ju.readValue(DexCraftFiles.launcherProperties, "LauncherProperties", "ForceResourcesUpdate").equals("true"))
    {
      FileIO fio = new FileIO();
      fio.excluir(DexCraftFiles.resFolder, true);
    }
    //Check if resource folder already exist. If not, download the resources again
    if ( (!DexCraftFiles.resFolder.exists()) || (DexCraftFiles.resFolder.listFiles().length == 0) )
    {
      changeStatus("Baixando recursos...");
      String resURL = ju.readValue(DexCraftFiles.coreFile, "LauncherUpdates", "LauncherResourceFile");
      Download downloadRes = new Download();
      //Start a separated thread for download
      Thread threadDownloadRes = new Thread(()->
      {
        downloadRes.zipResource(resURL, DexCraftFiles.tempFolder, DexCraftFiles.resZip);
      });
      threadDownloadRes.start();
      while(threadDownloadRes.isAlive())
      {
        //UI output during download process
        try
        {
          Thread.sleep(1100);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "EXCEÇÃO em Validate.resources()");
        }
        logger.log("INFO", downloadRes.getTimeEstimatedMsg());
        changeStatus("Baixando recursos... " + downloadRes.getProgressPercent() + "% concluído");
      }
      initUI.changeProgress(true, 60, 40);
      changeStatus("Instalando recursos...");
      Install installRes = new Install();
      //Start a separated thread for installing (zip file extraction)
      Thread threadInstallRes = new Thread(()->
      {
        installRes.downloadedZipResource(DexCraftFiles.resZip, DexCraftFiles.resFolder);
      });
      threadInstallRes.start();
      String checkFile = " ";
      while(threadInstallRes.isAlive())
      {
        //UI output during installing process
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
          Thread.sleep(25);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "EXCEÇÃO em Validate.resources()");
        }
      }
      // throws an error if something happened and the resource folder isn't in its place
      if ( (!DexCraftFiles.resFolder.exists()) || (DexCraftFiles.resFolder.listFiles().length == 0) )
      {
        logger.log("***ERRO***", "RECURSO PROVISIONADO INDISPONÍVEL");
        alerts.tryAgain();
      }
    }
    logger.log("INFO", "Recursos instalados. Validando atalhos...");
    /** Check if program shortcuts were created properly. If not, they will be installed:
        desktop for all users, start menu and current user's desktop **/
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
        logger.log(ex, "EXCEÇÃO em Validate.resources()");
      }
    }
  }


  /**
   * Validates a provisioned component.<br>
   * A provisioned component is a package with data
   * which needs to be updated or compared with some
   * local version.<br>
   * This method checks if the current component is
   * updated, and if not, downloads and install it.
   * @param coreFile the script file containing the version data
   * @param versionFile the local script file containing the current
   * version data
   * @param componentName the provisioned component's name
   * @param objectName the provisioned component's JSON key. <br>
   * Both script files (the online one and the local one)
   * MUST use the same category, with the exactly same name.
   * @param coreFileCategory the provisioned component's JSON object
   * (category), on coreFile.
   * @param versionFileCategory the provisioned component's JSON object
   * (category), on the versionFile.
   * @param componentCategoryURL the provisioned component's JSON object
   * (category) on the coreFile, which contains the objectURL.
   * @param objectURL the provisioned component's JSON key
   * (category) on the coreFile,<br> which contains the update URL to
   * download.
   * @param destinationDownloadDir the folder which the file will be
   * downloaded to
   * @param destinationDownloadFile the component update file
   * @param destinationInstallDir the directory to install the update
   */
  public static void provisionedComponent(File coreFile, File versionFile, String componentName,
                                          String objectName, String coreFileCategory,
                                          String versionFileCategory,
                                          String componentCategoryURL,
                                          String objectURL,
                                          File destinationDownloadDir,
                                          File destinationDownloadFile, File destinationInstallDir)
  {
    String getProvisionedVersion = ju.readValue(coreFile, coreFileCategory , objectName);
    if(ProvisionedPackage.isOutdated(versionFile,versionFileCategory, objectName, getProvisionedVersion ))
    {
      changeStatus("Baixando atualização - " + componentName + "...");
      String componentURL = ju.readValue(coreFile, componentCategoryURL , objectURL);
      Download downloadComponent = new Download();
      Thread threadDownloadComponent = new Thread(()->
      {
        downloadComponent.zipResource(componentURL, destinationDownloadDir, destinationDownloadFile);
      });
      threadDownloadComponent.start();
      while(threadDownloadComponent.isAlive())
      {
        try
        {
          Thread.sleep(1100);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex, "EXCEÇÃO em Validate.launcher()");
        }
        logger.log("INFO", downloadComponent.getTimeEstimatedMsg());
        changeStatus("Baixando " + componentName + "..." + downloadComponent.getProgressPercent() + "% concluído");
      }
      changeStatus("Baixando " + componentName + "..." + "100% concluído");
      initUI.changeProgress(true, 80, 40);
      Install installComponent = new Install();
      Thread threadInstallComponent = new Thread(()->
      {
        installComponent.downloadedZipResource(destinationDownloadFile, destinationInstallDir);
      });
      threadInstallComponent.start();
      String checkFile = " ";
      while(threadInstallComponent.isAlive())
      {
        if (!(installComponent.getInstallingFileName()).equals(""))
        {
          if (!installComponent.getInstallingFileName().equals(checkFile))
          {
            changeStatus("Instalando - " + componentName + "..." + installComponent.getInstallingFilePosition() + " / " + installComponent.getTotalFilesQuantity());
            checkFile = installComponent.getInstallingFileName();
          }
        }
        try
        {
          Thread.sleep(10);
        }
        catch (InterruptedException ex)
        {
          logger.log(ex,"EXCEÇÃO em Validate.provisionedComponent()");
        }
      }
      changeStatus("Instalando - " + componentName + "..." + installComponent.getTotalFilesQuantity() + " / " + installComponent.getTotalFilesQuantity());
      ju.editValue(versionFile, versionFileCategory, objectName, getProvisionedVersion);
    }
  }

}

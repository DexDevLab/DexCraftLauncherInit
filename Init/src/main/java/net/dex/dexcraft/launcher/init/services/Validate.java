package net.dex.dexcraft.launcher.init.services;


import java.io.IOException;
import net.dex.dexcraft.commons.check.AdminExecution;
import net.dex.dexcraft.commons.check.PreventSecondInstance;
import net.dex.dexcraft.commons.check.ProvisionedPackage;
import net.dex.dexcraft.commons.check.SystemRequirements;
import net.dex.dexcraft.commons.dto.SessionDTO;
import net.dex.dexcraft.commons.dto.SystemDTO;
import net.dex.dexcraft.commons.dto.UrlsDTO;
import net.dex.dexcraft.commons.dto.VersionsDTO;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.DexUI;
import net.dex.dexcraft.commons.tools.Download;
import net.dex.dexcraft.commons.tools.FileIO;
import net.dex.dexcraft.commons.tools.Install;
import net.dex.dexcraft.launcher.init.Init;
import static net.dex.dexcraft.launcher.init.Init.alerts;
import static net.dex.dexcraft.launcher.init.Init.logger;


/**
 * Class for program validations
 */
public class Validate
{
  public static String bgImageRandomizerCaller = "null";
  public static boolean isPingServiceOnLoginRunning = false;
  public static boolean isPingServiceOnMainWindowRunning = false;
  public static boolean isJavaVerified = false;

  /**
  * Validates Launcher instance, preventing users from<br>
  * opening Launcher without Init.
  * @param instanceName the name os instance (Init, Client
  * or DCBS).
  */
  public static void instance(String instanceName)
  {
    // Read the session assets from JSON properties file
    SessionDTO.parseSessionAssets();
    //Check if program is running as Admin
    AdminExecution.AdminExecution();
    boolean isInstanceInvalid = true;
    switch (instanceName)
    {
      case "Init":
        if (DexCraftFiles.logLock.exists())
        {
          SessionDTO.setDexCraftLauncherInitInstance(true);
          isInstanceInvalid = true;
        }
        else
        {
          isInstanceInvalid = false;
        }
        break;
      case "Client":
        if (PreventSecondInstance.isThereAnotherInstance("Init"))
        {
          isInstanceInvalid = PreventSecondInstance.isThereAnotherInstance("Client");
        }
        break;
      case "DCBS":
        if ( (PreventSecondInstance.isThereAnotherInstance("Init"))
              && PreventSecondInstance.isThereAnotherInstance("Client") )
        {
          isInstanceInvalid = PreventSecondInstance.isThereAnotherInstance("DCBS");
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
    if (DexCraftFiles.tempFolder.exists())
    {
      FileIO fio = new FileIO();
      fio.excluir(DexCraftFiles.tempFolder, true);
    }
    SessionDTO.setDexCraftLauncherClientInstance(false);
    SessionDTO.setDexCraftBackgroundServicesInstance(false);
  }

  /**
   * Validates a provisionedComponent.<br>
   * 1 - Check if the version is outdated or not installed<br>
   * 2 - Perform the download of the update with progress to the UI<br>
   * 3 - Perform the installation of the update with progress to the UI<br>
   * @param ui the User Interface which will be updated<br>
   * @param componentName the name of the component.
   * @param uiProgressValue the progress percent which represents this task.
   * @return if the component was validated successfully (true) or not (false)
   */
  public static boolean provisionedComponent(DexUI ui, String componentName, int uiProgressValue)
  {
    // Read current version assets
    VersionsDTO.parseVersions();
    VersionsDTO.parseProvisionedVersions();
    String programName = "";
    switch (componentName)
    {
      case "Resources":
        programName = "Recursos";
        break;
      case "Init":
        programName = "DexCraft Launcher Init";
        break;
      case "Client":
        programName = "DexCraft Launcher";
        break;
      case "DCBS":
        programName = "DexCraft Background Services";
        break;
      case "DCGame":
        programName = "DexCraft Factions Client";
        break;
      case "DCPXGame":
        programName = "DexCraft Pixelmon Client";
        break;
      case "DCVNGame":
        programName = "DexCraft Vanilla Client";
        break;
      case "DCBGame":
        programName = "DexCraft Beta Client";
        break;
      case "DCPatchGame":
        programName = "DexCraft Factions Patch";
        break;
      case "DCPXPatchGame":
        programName = "DexCraft Pixelmon Patch";
        break;
      case "DCVNPatchGame":
        programName = "DexCraft Vanilla Patch";
        break;
      case "DCBPatchGame":
        programName = "DexCraft Beta Patch";
        break;
      default:
        break;
    }
    if (!SessionDTO.isOfflineModeOn())
    {
      if ( (ProvisionedPackage.isOutdated(componentName)) || (!ProvisionedPackage.isInstalled(componentName)) )
      {
        if (componentName.contains("Game"))
        {
          ui.resetProgress();
        }
        Init.changeStatus(ui, "Baixando " + programName + "...", "");
        Download downloadComponent = new Download();
        Thread threadDownloadComponent = new Thread(()->
        {
          switch (componentName)
          {
            case "Resources":
              downloadComponent.zipResource(UrlsDTO.getLauncherResourceFile(), DexCraftFiles.tempFolder, DexCraftFiles.resZip);
              break;
            case "Init":
              downloadComponent.zipResource(UrlsDTO.getInitUpdate(), DexCraftFiles.tempFolder, DexCraftFiles.updateInitZip);
              break;
            case "Client":
              downloadComponent.zipResource(UrlsDTO.getDCLUpdate(), DexCraftFiles.tempFolder, DexCraftFiles.updateLauncherZip);
              break;
            case "DCBS":
              downloadComponent.zipResource(UrlsDTO.getDCBSUpdate(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCBSZip);
              break;
            case "DCGame":
              downloadComponent.zipResource(UrlsDTO.getClientDC(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCZip);
              break;
            case "DCPXGame":
              downloadComponent.zipResource(UrlsDTO.getClientDCPX(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCPXZip);
              break;
            case "DCVNGame":
              downloadComponent.zipResource(UrlsDTO.getClientDCVN(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCVNZip);
              break;
            case "DCBGame":
              downloadComponent.zipResource(UrlsDTO.getClientDCB(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCBZip);
              break;
            case "DCPatchGame":
              downloadComponent.zipResource(UrlsDTO.getPatchDC(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCPatchZip);
              break;
            case "DCPXPatchGame":
              downloadComponent.zipResource(UrlsDTO.getPatchDCPX(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCPXPatchZip);
              break;
            case "DCVNPatchGame":
              downloadComponent.zipResource(UrlsDTO.getPatchDCVN(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCVNPatchZip);
              break;
            case "DCBPatchGame":
              downloadComponent.zipResource(UrlsDTO.getPatchDCB(), DexCraftFiles.tempFolder, DexCraftFiles.updateDCBPatchZip);
              break;
            default:
              break;
          }
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
          if (componentName.contains("Game"))
          {
            Init.changeStatus(ui, "Baixando " + programName + "... "
                    , downloadComponent.getTimeEstimatedMsg() );
            ui.changeProgress(true, Double.parseDouble(downloadComponent.getProgressPercent().replace(",",".")), 35);
          }
          else
          {
            Init.changeStatus(ui, "Baixando " + programName + "..."
                    + downloadComponent.getProgressPercent() + "% concluído", "");
          }
        }
        if (componentName.contains("Game"))
        {
          Init.changeStatus(ui, "Baixando " + programName + "...", "100% concluído");
          ui.changeProgress(true, 100, 35);
          ui.resetProgress();
        }
        else
        {
          Init.changeStatus(ui, "Baixando " + programName + "..." + "100% concluído", "");
          ui.changeProgress(true, uiProgressValue, 30);
        }
        Init.changeStatus(ui, "Instalando " + programName + "... ", "");
        Install installComponent = new Install();
        Thread threadInstallComponent = new Thread(()->
        {
          switch (componentName)
          {
            case "Resources":
              installComponent.downloadedZipResource(DexCraftFiles.resZip, DexCraftFiles.resFolder);
              break;
            case "Init":
              installComponent.downloadedZipResource(DexCraftFiles.updateInitZip, DexCraftFiles.launcherFolder);
              break;
            case "Client":
              installComponent.downloadedZipResource(DexCraftFiles.updateLauncherZip, DexCraftFiles.launcherFolder);
              break;
            case "DCBS":
              installComponent.downloadedZipResource(DexCraftFiles.updateDCBSZip, DexCraftFiles.launcherFolder);
              break;
            case "DCGame":
              installComponent.downloadedZipResource(DexCraftFiles.updateDCZip, DexCraftFiles.launcherFolder);
              break;
            case "DCPXGame":
              installComponent.downloadedZipResource(DexCraftFiles.updateDCPXZip, DexCraftFiles.launcherFolder);
              break;
            case "DCVNGame":
              installComponent.downloadedZipResource(DexCraftFiles.updateDCVNZip, DexCraftFiles.launcherFolder);
              break;
            case "DCBGame":
              installComponent.downloadedZipResource(DexCraftFiles.updateDCBZip, DexCraftFiles.launcherFolder);
              break;
            case "DCPatchGame":
              installComponent.downloadedZipPatch(DexCraftFiles.updateDCPatchZip, DexCraftFiles.launcherFolder);
              break;
            case "DCPXPatchGame":
              installComponent.downloadedZipPatch(DexCraftFiles.updateDCPXPatchZip, DexCraftFiles.launcherFolder);
              break;
            case "DCVNPatchGame":
              installComponent.downloadedZipPatch(DexCraftFiles.updateDCVNPatchZip, DexCraftFiles.launcherFolder);
              break;
            case "DCBPatchGame":
              installComponent.downloadedZipPatch(DexCraftFiles.updateDCBPatchZip, DexCraftFiles.launcherFolder);
              break;
            default:
              break;
          }
        });
        threadInstallComponent.start();
        String checkFile = " ";
        while(threadInstallComponent.isAlive())
        {
          if (!(installComponent.getInstallingFileName()).equals(""))
          {
            if (!installComponent.getInstallingFileName().equals(checkFile))
            {
              if (componentName.contains("Game"))
              {
                Init.changeStatus(ui, "Instalando " + programName + "... "
                        , installComponent.getInstallingFileName() + ", "
                        + installComponent.getInstallingFilePosition() + " / "
                        + installComponent.getTotalFilesQuantity());
                ui.changeProgress(true, Double.parseDouble(installComponent.getProgressPercent().replace(",",".")), 35);
              }
              else
              {
                Init.changeStatus(ui, "Instalando " + programName + "... "
                        + installComponent.getInstallingFilePosition() + " / "
                        + installComponent.getTotalFilesQuantity(), "");
              }
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
        if (componentName.contains("Game"))
        {
          Init.changeStatus(ui, "Instalando " + programName + "... "
                        , installComponent.getTotalFilesQuantity() + " / "
                        + installComponent.getTotalFilesQuantity());
          ui.changeProgress(true, 100, 35);
          ui.resetProgress();
        }
        else
        {
          Init.changeStatus(ui, "Instalando " + programName + "... "
                  + installComponent.getTotalFilesQuantity() + " / "
                  + installComponent.getTotalFilesQuantity() , "");
        }
        switch (componentName)
        {
          case "Resources":
            break;
          case "Init":
            VersionsDTO.setDexCraftLauncherInitVersion(VersionsDTO.getProvisionedInitVersion());
            break;
          case "Client":
            VersionsDTO.setDexCraftLauncherClientVersion(VersionsDTO.getProvisionedClientVersion());
            break;
          case "DCBS":
            VersionsDTO.setDexCraftBackgroundServicesVersion(VersionsDTO.getProvisionedBackgroundServicesVersion());
            break;
          case "DCGame":
            // Game Clients don't need versioning
            break;
          case "DCPXGame":
            // Game Clients don't need versioning
            break;
          case "DCVNGame":
            // Game Clients don't need versioning
            break;
          case "DCBGame":
            // Game Clients don't need versioning
            break;
          case "DCPatchGame":
            VersionsDTO.setDexCraftFactionsPatchVersion(VersionsDTO.getProvisionedFactionsPatchVersion());
            break;
          case "DCPXPatchGame":
            VersionsDTO.setDexCraftPixelmonPatchVersion(VersionsDTO.getProvisionedPixelmonPatchVersion());
            break;
          case "DCVNPatchGame":
            VersionsDTO.setDexCraftVanillaPatchVersion(VersionsDTO.getProvisionedVanillaPatchVersion());
            break;
          case "DCBPatchGame":
            VersionsDTO.setDexCraftBetaPatchVersion(VersionsDTO.getProvisionedBetaPatchVersion());
            break;
          default:
            break;
        }
      }
    }
    else
    {
      if (!ProvisionedPackage.isInstalled(componentName))
      {
        alerts.noComponents();
      }
    }
    return true;
  }

  /**
   * Validates JRE Version.
   * @param ui DexUI instance
   * @return true if the method was performed with no errors.
   */
  public static boolean javaVersion(DexUI ui)
  {
    ui.resetProgress();
    if (!isJavaVerified)
    {
      Init.changeStatus(ui, "Verificando Java...", "");
      SystemRequirements req = new SystemRequirements();
      if (!req.checkJavaVersion().equals(SystemDTO.getJavaVersion()))
      {
        logger.log("INFO", "Java desatualizado! (" + req.checkJavaVersion() + ")");
        if (SessionDTO.isOfflineModeOn())
        {
          alerts.noComponents();
        }
        else
        {
          Download downloadJRE = new Download();
          Thread threadDownloadJRE = new Thread(()->
          {
            downloadJRE.zipResource(UrlsDTO.getJREInstaller(), DexCraftFiles.tempFolder, DexCraftFiles.updateJREZip);
          });
          threadDownloadJRE.start();
          while(threadDownloadJRE.isAlive())
          {
            try
            {
              Thread.sleep(1100);
            }
            catch (InterruptedException ex)
            {
              logger.log(ex, "EXCEÇÃO em Validate.launcher()");
            }
            logger.log("INFO", downloadJRE.getTimeEstimatedMsg());
            Init.changeStatus(ui, "Baixando atualização do Java... "
                      , downloadJRE.getTimeEstimatedMsg() );
            ui.changeProgress(true, Double.parseDouble(downloadJRE.getProgressPercent().replace(",",".")), 35);
          }
          Init.changeStatus(ui, "Baixando atualização do Java... ", "100% concluído");
          ui.changeProgress(true, 100, 35);
          ui.resetProgress();
          Init.changeStatus(ui, "Instalando Java...", "");
          Install installJRE = new Install();
          Thread threadInstallJRE = new Thread(()->
          {
            installJRE.downloadedZipResource(DexCraftFiles.updateJREZip, DexCraftFiles.tempFolder);
          });
          threadInstallJRE.start();
          String checkFile = " ";
          while(threadInstallJRE.isAlive())
          {
            if (!(installJRE.getInstallingFileName()).equals(""))
            {
              if (!installJRE.getInstallingFileName().equals(checkFile))
              {
                Init.changeStatus(ui, "Instalando Java... "
                          , installJRE.getInstallingFileName() + ", "
                          + installJRE.getInstallingFilePosition() + " / "
                          + installJRE.getTotalFilesQuantity());
                ui.changeProgress(true, Double.parseDouble(downloadJRE.getProgressPercent().replace(",",".")), 35);
                checkFile = installJRE.getInstallingFileName();
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
          Thread jreInstallerThread = new Thread(()->
          {
            try
            {
              new ProcessBuilder("cmd", "/c", "start", "/wait", "jre.exe", "INSTALLCFG=C:\\DexCraft\\launcher\\temp\\java.settings.cfg")
                    .directory(DexCraftFiles.tempFolder).start().waitFor();
            }
            catch (IOException | InterruptedException ex)
            {
              alerts.exceptionHandler(ex, "EXCEÇÃO em Validate.javaVersion(DexUI).jreInstallerThread()");
            }
          });
          jreInstallerThread.start();
          while(threadInstallJRE.isAlive())
          {
            Init.changeStatus(ui, "Executando atualização. Aguarde... "
                          , installJRE.getTotalFilesQuantity() + " / "
                          + installJRE.getTotalFilesQuantity());
            ui.changeProgress(true, -1, 35);
            try
            {
              Thread.sleep(1000);
            }
            catch (InterruptedException ex)
            {
              logger.log(ex,"EXCEÇÃO em Validate.provisionedComponent()");
            }
          }
          ui.resetProgress();
        }
      }
      logger.log("INFO", "Java Atualizado! (" + req.checkJavaVersion() + ")");
      isJavaVerified = true;
    }
    return true;
  }


}

/**
 * DexCraft Launcher Initializer (Init). This program checks system
 * requirements to run the Launcher, updates the Launcher and the
 * Background Services program, and downloads Launcher's basic
 * resources.
 */
package net.dex.dexcraft.launcher.init;


import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dex.dexcraft.commons.check.AdminExecution;
import net.dex.dexcraft.commons.check.OfflineMode;
import net.dex.dexcraft.commons.check.SystemRequirements;
import net.dex.dexcraft.commons.tools.Close;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.DexUI;
import net.dex.dexcraft.commons.tools.Download;
import net.dex.dexcraft.commons.tools.ErrorAlerts;
import net.dex.dexcraft.commons.tools.Logger;


/**
  * @author Dex
  * @since 30/04/2020
  * @version v2.1.2-201213-595
  *
  * Preloader Class with splash screen.
  */
public class Init extends Application
{
  static Stage preloaderStage;
  static Stage mainWindowStage =  new Stage();
  static Label preloaderLabel;
  static ProgressBar pbar;
  static ErrorAlerts alerts = new ErrorAlerts();
  static Logger logger = new Logger();
  static DexUI initUI = new DexUI();


  /**
   * Dynamic label change in UI with additional logging.
   * @param text the text to be shown and logged.
   */
  public static void changeStatus(String text)
  {
    initUI.changeMainLabel(text);
    logger.log("INFO", text);
  }

  /**
   * Starts the preloader screen stage.
   * @param primaryStage the stage itself (no need to specify)
   * @throws java.lang.Exception when can't load the Stack Pane
   * @see #callMain()
   */
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    alerts.setImage(new Image(Init.class.getResourceAsStream("icon1.jpg")));
    FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Preloader.fxml"));
    StackPane splashPane = splashLoader.load();
    preloaderStage = new Stage(StageStyle.TRANSPARENT);
    //Remove comment on next line to force focus on the Splash Screen
//    preloaderStage.setAlwaysOnTop(true);
    final Scene scene = new Scene(splashPane);
    preloaderStage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
    scene.setFill(Color.TRANSPARENT);
    Font.loadFont(Init.class.getResource("Minecrafter.Alt.ttf").toExternalForm(), 10);
    scene.getStylesheets().add(getClass().getResource("fxmlFont1.css").toExternalForm());
    preloaderStage.setScene(scene);
    preloaderStage.setResizable(false);
    pbar = new ProgressBar(0.0);
    preloaderLabel = new Label("Iniciando...");
    splashPane.getChildren().add(preloaderLabel);
    splashPane.getChildren().add(pbar);
    preloaderLabel.getStyleClass().add("mainlabel");
    pbar.setMaxSize(606, 11);
    pbar.setTranslateY(190);
    pbar.getStylesheets().add(getClass().getResource("gradientprogressbar2.css").toExternalForm());
    initUI.setProgressBar(pbar);
    initUI.setMainLabel(preloaderLabel);


    /** Opens a Service to show Splash before initialize the application **/
    Service<Boolean> splashService = new Service<Boolean>()
    {
      /** Show Splash Screen Stage **/
      @Override
      public void start()
      {
        preloaderStage.show();
        super.start();
      }

      /** Create a Task inside Service to interact with the UI Thread **/
      @Override
      protected Task<Boolean> createTask()
      {
        return new Task<Boolean>()
        {

          @Override
          protected Boolean call() throws Exception
          {

            // Check if another instance is running
            Validate.instance("Init");

            //Logger settings
            logger.setLogLock(DexCraftFiles.logLock);
            logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
            logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
            logger.setLogDir(DexCraftFiles.logFolder);
            logger.log("INFO", "Logger inicializado.");

            changeStatus("Iniciando...");
            AdminExecution.AdminExecution();
            initUI.changeProgress(true, 10, 40);
            logger.log("INFO", "Preparando Cache...");
            Validate.cache();
            initUI.changeProgress(true, 20, 40);
            if(!OfflineMode.IsRunning())
            {
              initUI.changeProgress(true, 30, 40);
              changeStatus("Baixando Corefile...");
              Download downloadCf = new Download();
              downloadCf.coreFile();
              initUI.changeProgress(true, 40, 40);
              changeStatus("Verificando o sistema. Aguarde...");
              SystemRequirements req = new SystemRequirements();
              req.checkRequirements();
              initUI.changeProgress(true, 50, 40);
              changeStatus("Verificando recursos...");
              Validate.resources();
              initUI.changeProgress(true, 70, 40);
              changeStatus("Verificando versão do DexCraft Launcher...");
              Validate.provisionedComponent(DexCraftFiles.coreFile, DexCraftFiles.launcherProperties, "DexCraft Launcher",
                       "DexCraftLauncherVersion", "Versions", "LauncherProperties", "LauncherUpdates", "DCLUpdate",
                       DexCraftFiles.tempFolder, DexCraftFiles.updateLauncherZip, DexCraftFiles.launcherFolder);
              initUI.changeProgress(true, 80, 40);
              changeStatus("Verificando versão do DexCraft Background Services...");
              Validate.provisionedComponent(DexCraftFiles.coreFile, DexCraftFiles.launcherProperties, "DexCraft Background Services",
                       "DexCraftBackgroundServicesVersion", "Versions", "LauncherProperties", "LauncherUpdates", "DCBSUpdate",
                       DexCraftFiles.tempFolder, DexCraftFiles.updateDCBSZip, DexCraftFiles.launcherFolder);
            }
            initUI.changeProgress(true, 90, 40);
            if(!DexCraftFiles.coreFile.exists())
            {
              logger.log("***ERRO***", "COREFILE NÃO ENCONTRADO.");
              alerts.noCoreFile();
            }
            changeStatus("Abrindo DexCraft Launcher...");
            initUI.changeProgress(true, 100, 40);
            logger.log("INFO", "Splash Screen terminada");
            return true;
          }
        };
      }

      /** When task above retuns "true", close the Splash Screen Stage and call Application. **/
      @Override
      protected void succeeded()
      {
        preloaderStage.close();
        try
        {
          callMain();
          logger.log("INFO", "Instância principal aberta");
        }
        catch (Exception ex)
        {
          alerts.exceptionHandler(ex, "EXCEÇÃO EM Client.start().start().succeeded()");
        }
      }
    };
    splashService.start();
  }

  /**
   * Main Scene method. Calls the Launcher and close the application.
   * @throws Exception when occurs some error on loading the Launcher.
   */
  private void callMain() throws Exception
  {
    new ProcessBuilder("cmd", "/c", "C:\\DexCraft\\launcher\\bin\\javaw.exe -jar DexCraftLauncher.jar").directory(DexCraftFiles.launcherFolder).start();
    logger.log("INFO", "Inicializando DexCraft Launcher...");
    Close.close(0);
  }

}
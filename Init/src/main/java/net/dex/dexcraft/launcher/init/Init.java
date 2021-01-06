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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dex.dexcraft.commons.Commons;
import net.dex.dexcraft.commons.check.OfflineMode;
import net.dex.dexcraft.commons.check.SystemRequirements;
import net.dex.dexcraft.commons.dto.UrlsDTO;
import net.dex.dexcraft.commons.tools.Close;
import net.dex.dexcraft.commons.tools.DexCraftFiles;
import net.dex.dexcraft.commons.tools.DexUI;
import net.dex.dexcraft.commons.tools.Download;
import net.dex.dexcraft.commons.tools.ErrorAlerts;
import net.dex.dexcraft.commons.tools.Logger;
import net.dex.dexcraft.launcher.init.services.Validate;


/**
  * @author Dex
  * @since 30/04/2020
  * @version v2.2.1-210105-628
  *
  * Preloader Class with splash screen.
  */
public class Init extends Application
{
  static Stage preloaderStage;
  static Stage mainWindowStage =  new Stage();
  static Label preloaderLabel;
  static ProgressBar pbar;
  public static ErrorAlerts alerts = new ErrorAlerts();
  public static Logger logger = new Logger();
  public static DexUI preloaderUI = new DexUI();


  /**
   * Dynamic label change in UI with additional logging.
   * @param ui the DexUI instance
   * @param mainLabelText the main label text (used to the logger aswell)
   * @param secLabelText the secondary label text. It can't be null but can be empty.
   */
  public static void changeStatus(DexUI ui, String mainLabelText, String secLabelText)
  {
    ui.changeMainLabel(mainLabelText);
    if (!secLabelText.equals(""))
    {
      ui.changeSecondaryLabel(secLabelText);
    }
    logger.log("INFO", mainLabelText);
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
    //DexCraft Commons alerts binding
    Commons.setErrorAlerts(alerts);

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
    preloaderLabel.getStyleClass().add("mainlabel");
    preloaderLabel.setMaxSize(480, 20);
    preloaderLabel.setTranslateY(135);
    preloaderLabel.setAlignment(Pos.CENTER);
    preloaderLabel.toFront();
    pbar.setMaxSize(480, 11);
    pbar.setTranslateY(150);
    pbar.getStylesheets().add(getClass().getResource("gradientprogressbar2.css").toExternalForm());
    pbar.toFront();
    splashPane.getChildren().add(preloaderLabel);
    splashPane.getChildren().add(pbar);
    preloaderUI.setProgressBar(pbar);
    preloaderUI.setMainLabel(preloaderLabel);


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
             //DexCraft Commons logger binding
            Commons.setLogger(logger);
            logger.log("INFO", "Logger inicializado.");

            changeStatus(preloaderUI, "Iniciando...", "");

            preloaderUI.changeProgress(true, 10, 30);
            logger.log("INFO", "Preparando Cache...");

            // Prepares the program cache folder and files
            Validate.cache();

            preloaderUI.changeProgress(true, 20, 30);

            // Do tasks below only if launcher isn't on Offline Mode
            if(!OfflineMode.IsRunning())
            {
              preloaderUI.changeProgress(true, 30, 30);
              changeStatus(preloaderUI, "Baixando Corefile...", "");

              //Performs CoreFile download
              Download downloadCf = new Download();
              downloadCf.coreFile();

              UrlsDTO.parseURLs();

              preloaderUI.changeProgress(true, 40, 30);
              changeStatus(preloaderUI, "Verificando o sistema. Aguarde...", "");

              //Check System Requirements
              SystemRequirements req = new SystemRequirements();
              req.checkRequirements();

              preloaderUI.changeProgress(true, 50, 30);
              changeStatus(preloaderUI, "Verificando recursos...", "");

              // Check if resouce folder is downloaded and updated
              Validate.provisionedComponent(preloaderUI, "Resources", 60);

              preloaderUI.changeProgress(true, 70, 30);
              changeStatus(preloaderUI, "Verificando versão do DexCraft Launcher...", "");

              // Check and update Client version if needed
              Validate.provisionedComponent(preloaderUI, "Client", 80);

              changeStatus(preloaderUI, "Verificando versão do DexCraft Background Services...", "");

              // Check and update DCBS version if needed
              Validate.provisionedComponent(preloaderUI, "DCBS", 90);
            }
            preloaderUI.changeProgress(true, 95, 30);

            // Interrupt Launcher if CoreFile is absent
            if(!DexCraftFiles.coreFile.exists())
            {
              logger.log("***ERRO***", "COREFILE NÃO ENCONTRADO.");
              alerts.noCoreFile();
            }

            changeStatus(preloaderUI, "Abrindo DexCraft Launcher...", "");
            preloaderUI.changeProgress(true, 100, 30);
            logger.log("INFO", "JAVAFX: Splash Screen terminada");
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
    Close.init();
  }

}
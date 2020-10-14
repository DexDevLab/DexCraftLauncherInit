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
import net.dex.dexcraft.launcher.check.AdmCheck;
import net.dex.dexcraft.launcher.check.InstanceCheck;
import net.dex.dexcraft.launcher.check.OfflineCheck;
import net.dex.dexcraft.launcher.check.ReqCheck;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.Cache;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.DexUI;
import net.dex.dexcraft.launcher.tools.Download;
import net.dex.dexcraft.launcher.tools.Logger;
import net.dex.dexcraft.launcher.tools.ScriptFileReader;


/**
  * @author Dex
  * @since 30/04/2020
  * @version v2.0.0-201013-357
  *
  * Inicia um Preloader como um Splash para fazer
  * todos os downloads essenciais antes de abrir
  * a interface do Launcher.
  */
public class Init extends Application
{
  static Stage preloaderStage;
  static Stage mainWindowStage =  new Stage();
  static Label preloaderLabel;
  static ProgressBar pbar;
  static Alerts alerts = new Alerts();
  static Logger logger = new Logger();
  static DexUI ui = new DexUI();
  static ScriptFileReader sfr = new ScriptFileReader();

  public static void changeStatus(String text)
  {
    ui.changeMainLabel(text);
    logger.log("INFO", text);
  }

  /**
   * Inicializa o Stage que abre o Preloader e cria a task em segundo plano.
   * @param primaryStage
   * @throws java.lang.Exception
   * @see #callMain(javafx.stage.Stage)
   */
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    // Carrega o FXML
    FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Preloader.fxml"));
    // Cria a Janela do Splash
    StackPane splashPane = splashLoader.load();
    // Define como transparente para que não apareça decoração de janela (maximizar, minimizar)
    preloaderStage = new Stage(StageStyle.TRANSPARENT);
//    preloaderStage.setAlwaysOnTop(true);
    final Scene scene = new Scene(splashPane);
    preloaderStage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
    // Define que a cor do painel root seja transparente para que dê o efeito de sombra
    scene.setFill(Color.TRANSPARENT);
    preloaderStage.setScene(scene);
    preloaderStage.setResizable(false);
    pbar = new ProgressBar(0.0);
    preloaderLabel = new Label("Iniciando...");
    splashPane.getChildren().add(preloaderLabel);
    splashPane.getChildren().add(pbar);
    pbar.setMaxSize(606, 11);
    pbar.setTranslateY(190);
    pbar.getStylesheets().add(getClass().getResource("gradientprogressbar2.css").toExternalForm());
    ui.setProgressBar(pbar);
    preloaderLabel.setAlignment(Pos.CENTER);
    preloaderLabel.setTranslateY(170);
    preloaderLabel.setTextFill(Color.web("#FFFFFF"));
    preloaderLabel.setFont(Font.font("MS Outlook", 12));
    ui.setMainLabel(preloaderLabel);
    // Cria o serviço para rodar alguma tarefa em background enquanto o splash é mostrado (no caso somente um delay)

    Service<Boolean> splashService = new Service<Boolean>()
    {
      // Mostra o splash quando o serviço for iniciado
      @Override
      public void start()
      {
        preloaderStage.show();
        logger.log("INFO", "Logger inicializado.");
        logger.log("INFO", "Preloader inicializado");
        super.start();
        // mostra a janela
      }

      @Override
      protected Task<Boolean> createTask()
      {
        return new Task<Boolean>()
        {

          @Override
          protected Boolean call() throws Exception
          {
            InstanceCheck.InstanceCheck();
            Cache.open();
            // Delay com atualização da Label
            changeStatus("Iniciando...");
            ui.changeProgress(true, 10, 40);
            AdmCheck.AdmCheck();
            ui.changeProgress(true, 20, 40);
            if(!OfflineCheck.OfflineCheck())
            {
              ui.changeProgress(true, 30, 40);
              changeStatus("Baixando Corefile...");
              Download downloadCf = new Download();
              downloadCf.coreFile();
              ui.changeProgress(true, 40, 40);
              changeStatus("Verificando requisitos...");
              ReqCheck.ReqCheck();
              ui.changeProgress(true, 50, 40);
              changeStatus("Verificando recursos...");
              Validate.resources();
              ui.changeProgress(true, 70, 40);
              changeStatus("Verificando versão do DexCraft Launcher...");
              Validate.launcher();
            }
            ui.changeProgress(true, 90, 40);
            if(!DexCraftFiles.coreFile.exists())
            {
              logger.log("***ERRO***", "COREFILE NÃO ENCONTRADO.");
              alerts.noCoreFile();
            }
            changeStatus("Abrindo DexCraft Launcher...");
            ui.changeProgress(true, 100, 40);
            logger.log("INFO", "Splash Screen terminada");
            return true;
          }
        };
      }

      // Quando a tarefa for finalizada fecha o splash e mostra a tela principal
      @Override
      protected void succeeded()
      {
        // Fecha o splash
        preloaderStage.close();
        try
        {
          // Chama a tela principal
          callMain();
          logger.log("INFO", "Instância principal aberta");
        } catch (Exception ex) { logger.log(ex, "ERRO", "EXCEÇÃO EM succeeded() de SplashService - NÃO FOI POSSÍVEL INICIALIZAR callMain(primaryStage)");}
      }
    };
    splashService.start();
  }

  /**
   * Método da Cena principal.Chama o Launcher para ser exibido na tela.
   * @throws Exception quando ocorre um erro na cena
   * @see #start(javafx.stage.Stage)
   */
  private void callMain() throws Exception
  {
    new ProcessBuilder("cmd", "/c", "C:\\DexCraft\\launcher\\bin\\javaw.exe -jar DexCraftLauncher.jar").directory(DexCraftFiles.launcherFolder).start();
    logger.log("INFO", "DexCraft Launcher inicializado");
    Cache.close();
  }

}

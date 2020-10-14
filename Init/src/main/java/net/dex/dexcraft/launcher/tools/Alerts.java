package net.dex.dexcraft.launcher.tools;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.dex.dexcraft.launcher.init.Init;


/**
 *
 */
public class Alerts
{

  private Throwable exceptionHandlerThrowable;
  private String exceptionHandlerContext;
  private Stage preloaderStage;

  private Boolean offlineModeBefore = false;
  private Boolean keepOfflineMode = false;

  Logger logger = new Logger();

  private void setLogging()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }


  private void alertLock(FutureTask<String> futureTask)
  {
    setLogging();
    while(!futureTask.isDone())
    {
      try
      {
        Thread.sleep(1500);
      }
      catch (InterruptedException ex)
      {
        logger.log(ex, "***ERRO***", "EXCEÇÃO EM Alerts.alertLock(FutureTask<String> futureTask)");
      }
    }
  }


  public void noAdmin()
  {
    setLogging();
    FutureTask<String> noAdmin = new FutureTask<>(new NoAdmin());
    logger.log("INFO", "Exibindo Alerts.NoAdmin()...");
    Platform.runLater(noAdmin);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(noAdmin);
    logger.log("INFO", "Alerts.NoAdmin() finalizado");
  }

  public void tryAgain()
  {
    setLogging();
    FutureTask<String> tryAgain = new FutureTask<>(new TryAgain());
    logger.log("INFO", "Exibindo Alerts.TryAgain()...");
    Platform.runLater(tryAgain);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(tryAgain);
    logger.log("INFO", "Alerts.TryAgain() finalizado");
  }

  public void doubleInstance()
  {
    setLogging();
    FutureTask<String> doubleInstance = new FutureTask<>(new DoubleInstance());
    logger.log("INFO", "Exibindo Alerts.DoubleInstance()...");
    Platform.runLater(doubleInstance);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(doubleInstance);
    logger.log("INFO", "Alerts.DoubleInstance() finalizado");
  }

  public void noReq()
  {
    setLogging();
    FutureTask<String> noReq = new FutureTask<>(new NoReq());
    logger.log("INFO", "Exibindo Alerts.NoArch()...");
    Platform.runLater(noReq);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(noReq);
    logger.log("INFO", "Alerts.noArch() finalizado");
  }

  public boolean offline(Boolean offline)
  {
    setLogging();
    if (offline)
    {
      offlineModeBefore = true;
    }
    else
    {
      offlineModeBefore = false;
    }
    FutureTask<String> offlineMode = new FutureTask<>(new OfflineMode());
    logger.log("INFO", "Exibindo Alerts.OfflineMode()...");
    Platform.runLater(offlineMode);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(offlineMode);
    logger.log("INFO", "Alerts.OfflineMode() finalizado");
    return keepOfflineMode;
  }

  public void noCoreFile()
  {
    setLogging();
    FutureTask<String> noCoreFile = new FutureTask<>(new NoCoreFile());
    logger.log("INFO", "Exibindo Alerts.noCoreFile()...");
    Platform.runLater(noCoreFile);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(noCoreFile);
    logger.log("INFO", "Alerts.noCoreFile() finalizado");
  }

  public void exceptionHandler(Throwable ex, String exceptionMessage)
  {
    setLogging();
    FutureTask<String> exceptionhandler = new FutureTask<>(new ExceptionHandler());
    logger.log("INFO", "Exibindo Alerts.exceptionHandler(Throwable, String)");
    exceptionHandlerThrowable = ex;
    exceptionHandlerContext = exceptionMessage;
    Platform.runLater(exceptionhandler);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(exceptionhandler);
    logger.log("INFO", "Alerts.exceptionHandler(Throwable, String) finalizado");
    Cache.closeOnError();
  }

  /**
   * Cria a janela de exceções do Launcher
   */
  class ExceptionHandler implements Callable
  {

    @Override
    public ExceptionHandler call() throws Exception
    {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("ERRO");
      alert.setHeaderText("EXCEÇÃO - " + exceptionHandlerThrowable.getMessage());
      alert.setContentText(exceptionHandlerContext);
      Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      alert.initOwner(preloaderStage);
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      exceptionHandlerThrowable.printStackTrace(pw);
      String exceptionText = sw.toString();

      Label label = new Label("Descrição completa do erro:");

      TextArea textArea = new TextArea(exceptionText);
      textArea.setEditable(false);
      textArea.setWrapText(true);

      textArea.setMaxWidth(Double.MAX_VALUE);
      textArea.setMaxHeight(Double.MAX_VALUE);
      GridPane.setVgrow(textArea, Priority.ALWAYS);
      GridPane.setHgrow(textArea, Priority.ALWAYS);

      GridPane expContent = new GridPane();
      expContent.setMaxWidth(Double.MAX_VALUE);
      expContent.add(label, 0, 0);
      expContent.add(textArea, 0, 1);

      alert.getDialogPane().setExpandableContent(expContent);

      alert.showAndWait();
      return null;
    }
  }

  class NoAdmin implements Callable
  {

    @Override
    public NoAdmin call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Cache.closeOnError();});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro de Inicialização");
      alerts.setHeaderText("Sistema Anti-Palles™");
      alerts.setContentText("O Launcher não foi \"executado como Administrador\".\n"
                            + "Isso pode interferir em diversas funções, como instalação, atualização, verificação de arquivos e produção de memes.\n\n"
                            + "Se certifique em clicar no atalho do DexCraft Launcher com o botão direito do mouse e depois em \"Executar como Administrador\".");
      ButtonType btnok = new ButtonType("OK");
      alerts.getButtonTypes().add(btnok);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnok)
      {
        Cache.closeOnError();
      }
      return null;
    }
  }


  /**
   * Janela genérica de falha
   */
  class TryAgain implements Callable
  {

    @Override
    public TryAgain call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Cache.closeOnError();});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro Crítico");
      alerts.setHeaderText("Houve um erro crítico durante a execução do DexCraft Launcher.");
      alerts.setContentText("Tente iniciar o Launcher novamente.");
      ButtonType btnok = new ButtonType("Sair");
      alerts.getButtonTypes().add(btnok);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnok)
      {
        Cache.closeOnError();
      }
      return null;
    }
  }


  /**
   * Cria a janela de alerta que informa que o Launcher já
   * está sendo executado.
   */
  class DoubleInstance implements Callable
  {

    @Override
    public DoubleInstance call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {System.exit(0);});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro de Inicialização");
      alerts.setHeaderText("Sistema Anti-Palles™ v2.0");
      alerts.setContentText("O Launcher já está sendo executado.\n"
                            + "Feche a atual janela do Launcher e abra o programa novamente.");
      ButtonType btnok = new ButtonType("OK");
      alerts.getButtonTypes().add(btnok);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnok)
      {
        System.exit(0);
      }
      return null;
    }
  }


  /**
   * Cria a janela de alerta que informa que o Sistema é de 32 bits.
   */
  class NoReq implements Callable
  {

    @Override
    public NoReq call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Cache.closeOnError();});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro");
      alerts.setHeaderText("");
      alerts.setContentText("O seu Sistema Operacional não é de 64 bits ou não atende aos"
                            + " requisitos mínimos de hardware e software.\n"
                            + "Você deseja instalar o Launcher mesmo assim?\n");
      ButtonType btnsim = new ButtonType("Sim");
      ButtonType btnnao = new ButtonType("Não");
      alerts.getButtonTypes().add(btnsim);
      alerts.getButtonTypes().add(btnnao);
      alerts.initOwner(preloaderStage);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnnao)
      {
        Cache.closeOnError();
      }
      return null;
    }
  }


  /**
   * Cria a janela de alerta que informa que o Launcher não
   * está sendo executado como Admin.
   */
  class OfflineMode implements Callable
  {

    @Override
    public  OfflineMode call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.INFORMATION);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Cache.closeOnError();});
      alerts.getButtonTypes().clear();
      ButtonType sim = null;
      ButtonType nao = null;
      if (offlineModeBefore)
      {
        alerts.setTitle("Modo Offline Ativado");
        alerts.setHeaderText("");
        alerts.setContentText("Foi detectada a ativação do Modo Offline em sessão anterior.\n"
                            + "Você deseja continuar com o Modo Offline?\n");
        sim = new ButtonType("SIM, Continuar OFFLINE");
        nao = new ButtonType("NÃO, Continuar ONLINE");
      }
      else
      {
        alerts.setTitle("Falha de conexão com a internet");
        alerts.setHeaderText("");
        alerts.setContentText("Não foi detectada uma conexão com a internet.\n"
                            + "Você deseja ativar o Modo Offline?\n"
                            + "Seu login não será verificado e seu backup será sincronizado na próxima vez que você conectar.\n");
        sim = new ButtonType("Ativar Modo Offline e continuar");
        nao = new ButtonType("Fechar o Launcher");
      }
      alerts.getButtonTypes().add(sim);
      alerts.getButtonTypes().add(nao);
      alerts.initOwner(preloaderStage);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == sim)
      {
        keepOfflineMode = true;
      }
      else
      {
        if (!offlineModeBefore)
        {
          Cache.closeOnError();
        }
      }
      return null;
    }
  }

  /**
   * Cria a janela de alerta que informa da ausência do CoreFile.
   */
  class NoCoreFile implements Callable
  {

    @Override
    public NoCoreFile call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Cache.closeOnError();});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro do CoreFile");
      alerts.setHeaderText("O CoreFile não pôde ser baixado ou carregado no sistema. Causa desconhecida.");
      alerts.setContentText("Tente iniciar o DexCraft Launcher novamente.");
      ButtonType btnok = new ButtonType("Sair");
      alerts.getButtonTypes().add(btnok);
      alerts.initOwner(preloaderStage);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnok)
      {
        Cache.closeOnError();
      }
      return null;
    }
  }

}

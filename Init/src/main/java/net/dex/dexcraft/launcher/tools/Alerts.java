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
 * Alert class with custom alerts.
 */
public class Alerts
{

  private Throwable exceptionHandlerThrowable;
  private String exceptionHandlerContext;
  private Stage preloaderStage;

  /** Inform if user has enabled the Offline Mode
   * in previous program running.<br>
   * This value is changed depending of the answer
   * they give on the Offline Mode alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.OfflineMode
   * @see #offline(java.lang.Boolean)
   */
  private Boolean offlineModeBefore = false;
  /**
   * Inform if the user wants to keep the Launcher on
   * Offline Mode or not.
   */
  private Boolean keepOfflineMode = false;

  Logger logger = new Logger();

  /**
   * Logger basic constructor.
   */
  private void setLogging()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  /**
   * Prevents thread keep running even before user
   * interaction.
   * @param futureTask the alert to being observed by its
   * Future Task
   */
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
        logger.log(ex, "EXCEÇÃO EM Alerts.alertLock(FutureTask<String> futureTask)");
      }
    }
  }

  /**
   * Calls the "No Admin" alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.NoAdmin
   */
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

  /**
   * Calls the "Try Again" alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.TryAgain
   */
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

  /**
   * Calls the "Double Instance" alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.DoubleInstance
   */
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

  /**
   * Calls the "No match with the requirements" alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.NoReq
   */
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

  /**
   * Calls the "Offline Mode" alert.
   * @param offline inform if the Launcher did not detected
   * the internet connection (false) or the player enabled
   * Offline Mode in other session (true)
   * @return the user decision to keep the Launcher on
   * Offline Mode(true) or not(false)
   * @see net.dex.dexcraft.launcher.tools.Alerts.OfflineMode
   * @see #keepOfflineMode
   */
  public boolean offline(Boolean offline)
  {
    setLogging();
    offlineModeBefore = offline;
    FutureTask<String> offlineMode = new FutureTask<>(new OfflineMode());
    logger.log("INFO", "Exibindo Alerts.OfflineMode()...");
    Platform.runLater(offlineMode);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(offlineMode);
    logger.log("INFO", "Alerts.OfflineMode() finalizado");
    return keepOfflineMode;
  }


  /**
   * Calls the "No core file" alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.NoCoreFile
   */
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

  /**
   * Calls the "No Spd" alert.
   * @see net.dex.dexcraft.launcher.tools.Alerts.NoSpd
   */
  public void noSpd()
  {
    setLogging();
    FutureTask<String> noSpd = new FutureTask<>(new NoSpd());
    logger.log("INFO", "Exibindo Alerts.NoSpd()...");
    Platform.runLater(noSpd);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(noSpd);
    logger.log("INFO", "Alerts.NoSpd() finalizado");
  }

  /**
   * Handles the exception message and throwable, putting it
   * on a window for the user.
   * @param ex the exception throwable
   * @param exceptionMessage the message of the error
   */
  public void exceptionHandler(Throwable ex, String exceptionMessage)
  {
    setLogging();
    logger.log(ex, exceptionMessage);
    FutureTask<String> exceptionhandler = new FutureTask<>(new ExceptionHandler());
    logger.log("INFO", "Exibindo Alerts.exceptionHandler(Throwable, String)");
    exceptionHandlerThrowable = ex;
    exceptionHandlerContext = exceptionMessage;
    Platform.runLater(exceptionhandler);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(exceptionhandler);
    logger.log("INFO", "Alerts.exceptionHandler(Throwable, String) finalizado");
    Close.close(1);
  }

  /**
   * Creates a customized window to show exceptions
   * and errors to the user.
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

  /**
   * This alert is shown when the admin file
   * isn't found after being requested.
   * This is seen as a critical error and
   * the program can't keep running.
   * @see net.dex.dexcraft.launcher.check.AdmCheck
   */
  class NoAdmin implements Callable
  {

    @Override
    public NoAdmin call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Close.close(1);});
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
        Close.close(1);
      }
      return null;
    }
  }

  /**
   * This alert is shown when some critical
   * error is triggered. It can be used when
   * isn't needed a specific message for an
   * error, but needs to close the program
   * anyway.
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
      stage.setOnCloseRequest((e) -> {Close.close(1);});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro Crítico");
      alerts.setHeaderText("Houve um erro crítico durante a execução do DexCraft Launcher.");
      alerts.setContentText("Tente iniciar o Launcher novamente.");
      ButtonType btnok = new ButtonType("Sair");
      alerts.getButtonTypes().add(btnok);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnok)
      {
        Close.close(1);
      }
      return null;
    }
  }


  /**
   * This alert is shown when the double instance
   * lock file is found after the program being
   * launched.
   * The first program keeps running while the second
   * is closed.
   * @see net.dex.dexcraft.launcher.check.InstanceCheck
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
        logger.log("INFO", "Encerrando...");
        System.exit(0);
      }
      return null;
    }
  }


  /**
   * This alert is shown when the System doesn't
   * find the minimum requirements to run any of the
   * minecraft clients. The user is asked if they want
   * to keep running the Launcher anyway.
   * @see net.dex.dexcraft.launcher.check.ReqCheck
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
      stage.setOnCloseRequest((e) -> {Close.close(1);});
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
        Close.close(1);
      }
      return null;
    }
  }


  /**
   * This alert is shown when there is no internet
   * connection (offlineModeBefore = false) or
   * when the user decided to enable the Offline Mode
   * last time they run the program (offlineModeBefore = true).
   * This alert changes the value of the offlineModeBefore
   * variable depending of what the user wants about keep
   * the Launcher offline or not.
   * @see #offlineModeBefore
   * @see #offline(java.lang.Boolean)
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
      stage.setOnCloseRequest((e) -> {Close.close(1);});
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
          Close.close(1);
        }
      }
      return null;
    }
  }


  /**
   * This alert is shown when the core file isn't
   * found, even after it had been downloaded.
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
      stage.setOnCloseRequest((e) -> {Close.close(1);});
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
        Close.close(1);
      }
      return null;
    }
  }

  /**
   * This alert is shown when the detected upload speed is below<br>
   * the minium required to the DexCraft Backuground Services.
   * @see net.dex.dexcraft.launcher.check.SystemRequirements
   * @see net.dex.dexcraft.launcher.tools.Connection
   */
  class NoSpd implements Callable
  {

    @Override
    public NoSpd call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.INFORMATION);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {Close.close(1);});
      alerts.getButtonTypes().clear();
      alerts.setTitle("Erro de Conexão");
      alerts.setHeaderText("Baixa Velocidade de Upload");
      alerts.setContentText("Foi detectada uma baixa velocidade de upload.\n"
                            + "Seus dados de jogo podem não ser sincronizados.\n\n"
                            + "Deseja continuar?");
      ButtonType btnsim = new ButtonType("Sim");
      ButtonType btnnao = new ButtonType("Não");
      alerts.getButtonTypes().add(btnsim);
      alerts.getButtonTypes().add(btnnao);
      alerts.initOwner(preloaderStage);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnnao)
      {
        Close.close(1);
      }
      return null;
    }
  }

}

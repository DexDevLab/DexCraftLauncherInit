package net.dex.dexcraft.launcher.tools;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.dex.dexcraft.launcher.init.Init;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.Cache;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;


/**
 *
 *
 */
public class FileIO
{

  private Alerts alerts = new Alerts();
  private int fileIoErrorCode = 0;
  private File src;
  private File dest;
  private static Logger logger;


  public FileIO()
  {
    logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  public void copiar(File source, File destination)
  {
    src = new File (source.toString());
    dest = new File (destination.toString());
    if (src.isDirectory())
    {
      if (dest.isDirectory())
      {
        if (!dest.exists()) {dest.mkdirs();}
        try
        {
          logger.log("INFO", "Copiando o diretório \"" + src.toString() + "\" para o diretório \"" + dest.toString() + "\"...");
          FileUtils.copyDirectory(src, dest);
          Iterator<File> files = FileUtils.iterateFilesAndDirs(src,new WildcardFileFilter("*.*"), new WildcardFileFilter("*"));
          files.forEachRemaining((f)->
          {
            logger.log("INFO", "Copiado arquivo/diretório \"" + f + "");
          });
        }
        catch (IOException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO EM FileIO.copiar(File, File)");
          alerts.exceptionHandler(ex, "ERRO DURANTE A CÓPIA");
        }
      }
      else
      {
        error(1);
      }
    }
    else
    {
      if (dest.isDirectory())
      {
        if (!dest.exists()) {dest.mkdirs();}
        try
        {
          logger.log("INFO", "Copiando o arquivo \"" + src.toString() + "\" para o diretório \"" + dest.toString() + "\"");
          FileUtils.copyFileToDirectory(src, dest);
//          Iterator<File> files = FileUtils.iterateFilesAndDirs(src,new WildcardFileFilter("*.*"), new WildcardFileFilter("*"));
//          files.forEachRemaining((f)->
//          {
//            logger.log("INFO", "Copiado arquivo \"" + f + "");
//          });
        }
        catch (IOException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO EM FileIO.copiar(File, File)");
          alerts.exceptionHandler(ex, "ERRO DURANTE A CÓPIA");
        }
      }
      else
      {
        try
        {
          logger.log("INFO", "Copiando o arquivo \"" + src.toString() + "\" para \"" + dest.toString() + "\"");
          FileUtils.copyFile(src, dest);
//          Iterator<File> files = FileUtils.iterateFilesAndDirs(src,new WildcardFileFilter("*.*"), new WildcardFileFilter("*"));
//          files.forEachRemaining((f)->
//          {
//            logger.log("INFO", "Copiado arquivo \"" + f + "");
//          });
        }
        catch (IOException ex)
        {
          logger.log(ex, "***ERRO***", "EXCEÇÃO EM FileIO.copiar(File, File)");
          alerts.exceptionHandler(ex, "ERRO DURANTE A CÓPIA");
        }
      }
    }
  }

  public void mover(File source, File destination)
  {
    src = new File (source.toString());
    dest = new File (destination.toString());
    logger.log("INFO", "Iniciando movimentação: de \"" + src.toString() + "\" para \"" + dest.toString() + "\"");
    if (!dest.exists()) {dest.mkdirs();}
    copiar(src, dest);
    if (src.isDirectory())
    {
      excluir(src, true);
    }
    else
    {
      excluir(src, false);
    }
    if (src.exists())
    {
      logger.log("***ERRO***", "EXCEÇÃO EM FileIO.mover(File, File) - ARQUIVO DE ORIGEM \"" + src.toString() + "\" AINDA EXISTE");
    }
  }


  public void criar(File source)
  {
    src = new File (source.toString());
    try
    {
      FileUtils.touch(src);
    }
    catch (IOException ex)
    {
      alerts.exceptionHandler(ex, "FALHA NA OPERAÇÃO");
    }
    if(!src.exists())
    {
      error(4);
    }
  }


  public void excluir(File source, boolean includeParentDir)
  {
    src = new File (source.toString());
    if (src.exists())
    {
      try
      {
      Stream<Path> files = Files.walk(src.toPath());
      files.sorted(Comparator.reverseOrder()).map(Path::toFile)
        .forEach((file) ->
        {
          if (file.isDirectory())
          {
            try
            {
              if((file.toString()).equals(src.toString()))
              {
                if(includeParentDir)
                {
                  logger.log("INFO", "Excluindo diretório pai \"" + src.toString() + "\"...");
                  FileUtils.deleteDirectory(file);
                }
              }
              else
              {
                logger.log("INFO", "Excluindo diretório \"" + src.toString() + "\"...");
                FileUtils.deleteDirectory(file);
              }
            }
            catch (IOException ex)
            {
              logger.log(ex, "***ERRO***", "EXCEÇÃO em FileIO.excluir(File, boolean)");
              error(3);
            }
          }
          else
          {
            logger.log("INFO", "Excluindo arquivo \"" + src.toString() + "\"...");
            FileUtils.deleteQuietly(file);
          }
        });
      files.close();
      }
      catch (IOException ex)
      {
        logger.log(ex, "***ERRO***", "EXCEÇÃO em FileIO.excluir(File, boolean)");
        error(3);
      }
      if (src.exists())
      {
        if ((src.isFile()) | (includeParentDir))
        {
          logger.log("***ERRO***","Não foi possível remover SOURCE \"" + src.toString() + "\"");
          error(3);
        }
      }
    }
    else
    {
      logger.log("***ERRO***", "SOURCE \"" + src.toString() + "\" não existe.");
      error(5);
    }
  }

  private void error(int errorCode)
  {
    fileIoErrorCode = errorCode;
    FutureTask<String> fileIoError = new FutureTask<>(new FileIOError());
    logger.log("INFO", "Exibindo alerta de erro FileIOError()...");
    Platform.runLater(fileIoError);
    logger.log("INFO", "Aguardando resposta do usuário...");
    alertLock(fileIoError);
    logger.log("INFO", "Alerta FileIOError() encerrado.");
  }


  private static void alertLock(FutureTask<String> futureTask)
  {
    while(!futureTask.isDone())
    {
      try
      {
        Thread.sleep(1500);
      }
      catch (InterruptedException ex)
      {
        logger.log(ex, "***ERRO***", "EXCEÇÃO EM FileIO.error().alertLock(FutureTask<String>) - THREAD INTERROMPIDA");
      }
    }
  }


  /////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Falhas de FileIO
   */
  class FileIOError implements Callable
  {

    @Override
    public FileIOError call() throws Exception
    {
      Alert alerts = new Alert(Alert.AlertType.ERROR);
      alerts.initModality(Modality.APPLICATION_MODAL);
      Stage stage = (Stage) alerts.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(Init.class.getResourceAsStream("icon1.jpg")));
      stage.setOnCloseRequest((e) -> {e.consume();});
      alerts.getButtonTypes().clear();
      alerts.setTitle("ERRO CRÍTICO NO PROCESSO");
      switch (fileIoErrorCode)
      {
        case 1:
          alerts.setHeaderText("Falha durante processo de cópia.");
          alerts.setContentText("SOURCE \"" + src.toString() + "\" é um diretório, mas DESTINATION \"" + dest.toString() + "\" é um arquivo.");
          break;
        case 2:
          alerts.setHeaderText("Falha durante processo de mover.");
          alerts.setContentText("Não foi possível remover SOURCE \"" + src.toString() + "\" após copiar.");
          break;
        case 3:
          alerts.setHeaderText("Falha durante processo de excluir.");
          alerts.setContentText("Não foi possível remover SOURCE \"" + src.toString() + "\"");
          break;
        case 4:
          alerts.setHeaderText("Falha durante processo de criar.");
          alerts.setContentText("Não foi possível criar o arquivo SOURCE \"" + src.toString() + "\"");
        case 5:
          alerts.setHeaderText("Falha durante processo de excluir");
          alerts.setContentText("SOURCE \"" + src.toString() + "\" não existe.");
          break;
        default:
          break;
      }
      ButtonType btnok = new ButtonType("OK");
      alerts.getButtonTypes().add(btnok);
      Optional<ButtonType> result = alerts.showAndWait();
      if (result.get() == btnok)
      {
        fileIoErrorCode = 0;
        Cache.closeOnError();
      }
      return null;
    }
  }

}

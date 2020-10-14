package net.dex.dexcraft.launcher.tools;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.sizeOfDirectory;



/**
 *
 * @author Dex
 * @since 10/06/2019
 * @version 1.0.0-20191007-8
 * Generates a Log.
 *
 */
public class Logger
{
  /**
    * Formato de data e hora da mensagem do Log.
    * @see #time
    */
  private DateFormat df;
  /**
    * Coleta a data e hora atual da mensagem do Log.
    * @see #df
    */
  private String time;
  /**
   * Formato de data e hora do arquivo do Log.
   * @see #time2
   */
  private DateFormat df2;
  /**
    * Coleta a data e hora atual do arquivo do Log.
    * @see #df
    * @see #logname
    */
  private String time2;
  /**
    * Define o formato de nome para o novo Log gerado.
    * @see #time2
    * @see #logfile
    */
  private File logname;
  /* Diretório contendo os logs. */
  private File logdir;
  private File loglock;
  /**
    * Objeto de arquivo de Log com a data e hora gravados.
    */
  private File logfile;
  /**
    * Separador de linha necessário para concatenar o texto
    */
  private final String line = System.getProperty("line.separator");
  /**
    * Armazena o tamanho do diretório de Logs para manipulação, em bytes.
    */
  private long logsize;



  public void setLogLock(File lock) { this.loglock = new File (lock.toString()); }

  public File getLogLock() { return this.loglock; }

  public void setMessageFormat(String dateformat) { this.df = new SimpleDateFormat(dateformat); }

  public String getMessageFormat() { return this.df.toString(); }

  public void setLogNameFormat(String dateformat) { this.df2 = new SimpleDateFormat(dateformat); }

  public String getLogNameFormat() { return this.df2.toString(); }

  public void setLogFile(File fil) { this.logfile = fil; }

  public File getLogFile() { return this.logfile; }

  public void setLogDir(File dir) { this.logdir = dir; }

  public File getLogDir() { return this.logdir; }

  public String getLogSize()
  {
    String logSizeMsg = "";
    long size = sizeOfDirectory(this.logdir);
    long sizeCalculated = 0;
    String sizeMeasurement = "B";
    if (size >= 1048576)
    {
      sizeCalculated = size / 1048576;
      sizeMeasurement = "MB";
    }
    else if (size >= 1024)
    {
      sizeCalculated = size / 1024;
      sizeMeasurement = "KB";
    }
    logSizeMsg = sizeCalculated + sizeMeasurement;
    return logSizeMsg;
  }


  /**
   * Escreve o log no console e gera a mesma frase externamente.
   *
   * @param throwable - o throwable da exceção (se necessário)
   * @param logtype - "INFO" para informações em geral e "ERRO" para erros
   * @param logmsg - A mensagem a ser escrita
   */
  public void log (Throwable throwable, String logtype, String logmsg)
  {
    try
    {
      if (!getLogLock().exists())
      {
        try
        {
          FileUtils.touch(getLogLock());
        }
        catch (IOException ex)
        {
          System.out.println("ERRO CRÍTICO: ERRO AO GERAR O ARQUIVO DE LOGLOCK.");
        }
      }
      else
      {
        this.logfile = new File(getLatestFileFromDir(this.logdir.toString()));
      }
      if (this.logname == null)
      {
        this.logname = new File (this.df2.format(Calendar.getInstance().getTime()) + ".txt");
        this.logfile = new File (this.logdir + File.separator + this.logname);
      }
      this.time = this.df.format(Calendar.getInstance().getTime());
      logtype = "[" + logtype + "]: ";
//      logmsg = (line + logmsg + line + throwable.getMessage());
      Exception ex = new Exception(throwable);
      logmsg = (logmsg + line + ex);
      FileUtils.writeStringToFile(this.logfile, line, "UTF-8", true);
      System.out.println("");
      FileUtils.writeStringToFile(this.logfile, this.time + logtype + logmsg + line, "UTF-8", true);
      System.out.println(time + logtype + logmsg);
      StackTraceElement[] message = throwable.getStackTrace();
      for (StackTraceElement message1 : message)
      {
        FileUtils.writeStringToFile(this.logfile, message1.toString() + line, "UTF-8", true);
        System.out.println(message1.toString());
      }
      FileUtils.writeStringToFile(this.logfile, "" + line, "UTF-8", true);
      System.out.println("");
    }
    catch (IOException ex)
    {
      System.out.println("ERRO CRÍTICO: ERRO AO GERAR O ARQUIVO DE LOG.");
    }
  }

  /**
   * Escreve o log no console e gera a mesma frase externamente.
   *
   * @param logtype - "INFO" para informações em geral e "ERRO" para erros
   * @param logmsg - A mensagem a ser escrita
   */
  public void log (String logtype, String logmsg)
  {
    try
    {
      if (!getLogLock().exists())
      {
        try
        {
          FileUtils.touch(getLogLock());
        }
        catch (IOException ex)
        {
          System.out.println("ERRO CRÍTICO: ERRO AO GERAR O ARQUIVO DE LOGLOCK.");
        }
      }
      else
      {
        this.logfile = new File(getLatestFileFromDir(this.logdir.toString()));
      }
      if (this.logname == null)
      {
        this.logname = new File (this.df2.format(Calendar.getInstance().getTime()) + ".txt");
        this.logfile = new File (this.logdir + File.separator + this.logname);
      }
      this.time = this.df.format(Calendar.getInstance().getTime());
      String finalmsg = "";
      logtype = "[" + logtype + "]: ";
      switch (logtype)
      {
        case "[INFO]: ":
          finalmsg = this.time + logtype + logmsg;
          break;
        case "[***ERRO***]: ":
          finalmsg = line + this.time + logtype + logmsg + line;
          break;
        default:
          throw new IOException();
      }
      System.out.println(finalmsg);
      FileUtils.writeStringToFile(this.logfile, finalmsg + line, "UTF-8", true);
    }
    catch (IOException ex)
    {
      System.out.println("ERRO CRÍTICO: ERRO AO GERAR O ARQUIVO DE LOG.");
    }
  }


  private String getLatestFileFromDir(String dirPath)
  {
    File dir = new File(dirPath);
    File[] files = dir.listFiles();
    if (files == null || files.length == 0)
    {
      return null;
    }
    File lastModifiedFile = files[0];
    for (File file : files)
    {
      if (lastModifiedFile.lastModified() < file.lastModified())
      {
        lastModifiedFile = file;
      }
    }
    this.logname = lastModifiedFile;
    return lastModifiedFile.toString();
  }


  public String cleanLogs()
  {
    File dir = new File(getLogDir().toString());
    String sizeBeforeClean = this.getLogSize();
    System.out.println("Limpando o diretório de logs \"" + dir.toString() + "\"...");
    File logToExcludeFromDeletion = new File(getLatestFileFromDir(dir.toString()));
    File[] files = dir.listFiles();
    for (File file : files)
    {
      if(!((file.getName().equals(logToExcludeFromDeletion.getName()))
        | (file.getName().equals(getLogLock().getName()))))
      {
        FileUtils.deleteQuietly(file);
      }
    }
    return sizeBeforeClean;
  }



//  /* For Testing. */
//  public static void main(String[] args)
//  {
//    Logger l = new Logger();
//    l.setMessageFormat("yyyy/MM/dd HH:mm:ss");
//    l.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
//    l.setLogDir(new File("D:/"));
//    l.log(null, "INFO", "Início");
//    l.log(null, "INFO", "Teste1");
//    l.log(null, "INFO", "Teste2");
//    l.log(null, "INFO", "Teste3");
//    l.log(null, "INFO", "Teste4");
//    l.log(null, "INFO", "Teste5");
//    l.log(null, "INFO", "Teste6");
//    l.log(null, "INFO", "Teste7");
//    l.log(null, "INFO", "Teste8");
//    l.log(null, "INFO", "Teste9");
//    try
//    {
//      Thread.sleep(30000);
//    }
//    catch (InterruptedException ex)
//    {
//
//    }
//    l.log(null, "INFO", "Teste10");
//    l.log(null, "INFO", "Teste11");
//    l.log(null, "INFO", "Teste12");
//    l.log(null, "INFO", "Teste13");
////    try
////    {
////      FileUtils.writeStringToFile(new File("C:\blabla.txt"), "blabla", "UTF-8");
////    }
////    catch (IOException ex)
////    {
////      l.log(ex, "ERRO", "Teste de Exceção1");
////    }
//    l.log(null, "INFO", "Teste14");
//    l.log(null, "INFO", "Fim.");
//  }

}





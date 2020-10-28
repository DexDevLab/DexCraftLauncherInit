package net.dex.dexcraft.launcher.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.io.*;


/**
 *
 *
 */
public class Download
{
  /* URL do arquivo core */
  private URL downloadURL;
  private InputStream input;
  long count = 0;
  int n = 0;
  double progress = 0;
  long currentTime = 0;
  long startTime = 0;
  private static int EOF = -1;
  private static int DEFAULT_BUFFER_SIZE = 1024 * 4;
  private Alerts alerts = new Alerts();
  private Logger logger = new Logger();
  private FileIO file = new FileIO();
  private NumberFormat formatter = new DecimalFormat("#0.0");
  private NumberFormat formatter2 = new DecimalFormat("#0.00");

  private String downloadedSize = "";
  private String totalSize = "";
  private String timeEstimatedMsg = "";
  private String estimatedHours = "";
  private String estimatedMinutes = "";
  private String estimatedSeconds = "";
  private String progressPercent = "";
  private String downloadSpeed = "";

  public String getDownloadedSize() { return this.downloadedSize; }

  public void setDownloadedSize(String value) { this.downloadedSize = value; }

  public String getTotalSize() { return this.totalSize; }

  public void setTotalSize(String value) { this.totalSize = value; }

  public String getTimeEstimatedMsg() { return this.timeEstimatedMsg; }

  public void setTimeEstimatedMsg(String value) { this.timeEstimatedMsg = value; }

  public String getEstimatedHours() { return this.estimatedHours; }

  public void setEstimatedHours(String value) { this.estimatedHours = value; }

  public String getEstimatedMinutes() { return this.estimatedMinutes; }

  public void setEstimatedMinutes(String value) { this.estimatedMinutes = value; }

  public String getEstimatedSeconds() { return this.estimatedSeconds; }

  public void setEstimatedSeconds(String value) { this.estimatedSeconds = value; }

  public String getProgressPercent()
  {
    if ((this.progressPercent == null) || (this.progressPercent.isEmpty()))
    {
      setProgressPercent("0");
    }
    return this.progressPercent;
  }

  public void setProgressPercent(String value) { this.progressPercent = value; }

  public String getDownloadSpeed() { return this.downloadSpeed; }

  public void setDownloadSpeed(String value) { this.downloadSpeed = value; }


  private void setLogging()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  public void coreFile()
  {
    setLogging();
    logger.log("INFO", "Verificando se existe um CoreFile anterior...");
    if (DexCraftFiles.coreFile.exists())
    {
      logger.log("INFO", "Excluindo CoreFile antigo...");
      file.excluir(DexCraftFiles.coreFile, false);
    }
    try
    {
      logger.log("INFO", "Coletando link de download do CoreFile...");
      ScriptFileReader sfr = new ScriptFileReader();
      downloadURL = new URL (sfr.getOutputEntry(DexCraftFiles.coreFileLinkFile, "CoreFileURL"));
      logger.log("INFO", "Baixando CoreFile...");
      FileUtils.copyURLToFile(downloadURL, DexCraftFiles.coreFile);
      logger.log("INFO", "Download concluído...");
    }
    catch (MalformedURLException ex)
    {
      logger.log(ex, "***ERRO***", "EXCEÇÃO EM Download.coreFile() - FALHA NO DOWNLOAD");
      alerts.exceptionHandler(ex, "ERRO DURANTE O DOWNLOAD DO COREFILE");
    }
    catch (IOException ex)
    {
      logger.log(ex, "***ERRO***", "EXCEÇÃO EM Download.coreFile() - ERRO AO CRIAR O COREFILE A PARTIR DE UM URL");
      alerts.exceptionHandler(ex, "EXCEÇÃO AO CRIAR O COREFILE A PARTIR DE UM URL");
    }
    if (!DexCraftFiles.coreFile.exists())
    {
      logger.log("***ERRO***", "EXCEÇÃO EM Download.coreFile() - ARQUIVO COREFILE NÃO ENCONTRADO");
      alerts.noCoreFile();
    }
  }


  public void zipResource(String url, File destFolder, File destZip)
  {
    if (!destFolder.exists())
    {
      destFolder.mkdirs();
    }
    downloadWithProgress(url, destZip);
    if (!destZip.exists())
    {
      alerts.tryAgain();
    }
  }

  private void downloadWithProgress(String url, File destZip)
  {
    try
    {
      downloadURL = new URL (url);
      input = downloadURL.openStream();
      URLConnection urlConnection = downloadURL.openConnection();
      urlConnection.connect();
      long fileSize = urlConnection.getContentLength();
      FileOutputStream output = FileUtils.openOutputStream(destZip);
      startTime = System.currentTimeMillis();
      byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
      double divisor = 0;
      progress = 0;
      count = 0;
      n = 0;
      currentTime = 0;
      while (EOF != (n = input.read(buffer)))
      {
        divisor = (double)fileSize / count ;
        progress = 100 / divisor;
        currentTime = System.currentTimeMillis();
        output.write(buffer, 0, n);
        count += n;
        showProgress(progress, fileSize, count, startTime, currentTime);
      }
      progress = 100;
      showProgress(progress, fileSize, fileSize, startTime, currentTime);
      output.close();
      input.close();
    }
    catch (MalformedURLException ex)
    {
      alerts.exceptionHandler(ex, "ERRO DURANTE O DOWNLOAD DO ARQUIVO");
    }
    catch (IOException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO AO CRIAR O ARQUIVO A PARTIR DE UM URL");
    }
  }

  private void showProgress(double progressPercent, long fileSize, long downloadedSize, long startTime, long currentTime)
  {
    long downloadedTime = currentTime - startTime;
    long downloadedSizeDiff = (fileSize - (downloadedSize/4096));
    if (downloadedTime >= 1000)
    {
      int currentTimeInSeconds = Math.round(downloadedTime /1000);
      double bytesPerSecond = Math.round(downloadedSize / currentTimeInSeconds);
      long totalSecondsRemaining = Math.round((downloadedSizeDiff / bytesPerSecond) - currentTimeInSeconds);
      int hoursRemaining = 0;
      int minutesRemaining = 0;
      String measurement = "B/s";
      if (bytesPerSecond > 1024)
      {
        measurement = "KB/s";
        bytesPerSecond /= 1024;
        if (bytesPerSecond > 1024)
        {
          measurement = "MB/s";
          bytesPerSecond /= 1024;
        }
      }
      long fileSizeCalculated = 0;
      long downloadedSizeCalculated = 0;
      String fileSizeMeasurement = "B";
      String downloadedSizeMeasurement = "B";
      if (fileSize >= 1048576)
      {
        fileSizeCalculated = fileSize / 1048576;
        fileSizeMeasurement = "MB";
      }
      else if (fileSize >= 1024)
      {
        fileSizeCalculated = fileSize / 1024;
        fileSizeMeasurement = "KB";
      }
      if (downloadedSize >= 1048576)
      {
        downloadedSizeCalculated = downloadedSize / 1048576;
        downloadedSizeMeasurement = "MB";
      }
      else if (downloadedSize >= 1024)
      {
        downloadedSizeCalculated = downloadedSize / 1024;
        downloadedSizeMeasurement = "KB";
      }
      String progressOutput = formatter2.format(progressPercent);
      setProgressPercent(progressOutput);
      setDownloadedSize(downloadedSizeCalculated + downloadedSizeMeasurement);
      setTotalSize(fileSizeCalculated + fileSizeMeasurement);
      setDownloadSpeed(formatter.format(bytesPerSecond) + measurement);
      setEstimatedHours("");
      setEstimatedMinutes("");
      setEstimatedSeconds("");
      if (totalSecondsRemaining >= 3600)
      {
        totalSecondsRemaining /= 3600;
        totalSecondsRemaining = Math.round(totalSecondsRemaining);
        hoursRemaining = (int) totalSecondsRemaining;
        setEstimatedHours(hoursRemaining + " hora(s), ");
      }
      if (totalSecondsRemaining >= 60)
      {
        totalSecondsRemaining /= 60;
        totalSecondsRemaining = Math.round(totalSecondsRemaining);
        minutesRemaining = (int) totalSecondsRemaining;
        setEstimatedMinutes(minutesRemaining + " minuto(s) e ");
      }
      setEstimatedSeconds(totalSecondsRemaining + " segundo(s) ");
      // next line follows an example of message to estimated time and their values //
      setTimeEstimatedMsg(getEstimatedHours() + getEstimatedMinutes() + getEstimatedSeconds()
          + "restante(s), " + getDownloadedSize() + " / " + getTotalSize() + ", " + getDownloadSpeed()
          + ", " + getProgressPercent() + "% concluído");
    }
    else
    {
      setTimeEstimatedMsg("Aguarde...");
    }
  }

  // EXAMPLE OF HOW TO DEVELOP THE DOWNLOAD THREAD WITH MONITORING AND OUTPUT

//  public static void main(String[] args)
//  {
//    String url = "http://myurl.com/downloadfile";
//    File destFolder = new File("C:/DownloadTest");
//    File destZip = new File("C:/DownloadTest/dclclientdcpx.zip");
//    Download download = new Download();
//    Thread testDownload = new Thread(()->
//    {
//      download.zipResource(url, destFolder, destZip);
//    });
//    testDownload.start();
//    while(testDownload.isAlive())
//    {
//      try
//      {
//        Thread.sleep(1000);
//      }
//      catch (InterruptedException ex)
//      {
//        //
//      }
//      System.out.println(download.getTimeEstimatedMsg());
//    }
//  }


}

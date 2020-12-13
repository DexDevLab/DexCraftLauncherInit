package net.dex.dexcraft.commons.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.io.FileUtils;


/**
 * Class for downloading files and packages.
 *
 */
public class Download
{
  private URL downloadURL;
  private InputStream input;
  long count = 0;
  int n = 0;
  double progress = 0;
  long currentTime = 0;
  long startTime = 0;
  private static int EOF = -1;
  private static int DEFAULT_BUFFER_SIZE = 1024 * 4;
  private ErrorAlerts alerts = new ErrorAlerts();
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


  public Download()
  {
    //Logger constructor.
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  /**
   * Get the downloaded file size.
   * @return the size of the downloaded file so far, in bytes.
   */
  public String getDownloadedSize() { return this.downloadedSize; }

  /**
   * Set the downloaded file size.
   * @param value the size of the downloaded file so far, in bytes.
   */
  private void setDownloadedSize(String value) { this.downloadedSize = value; }

  /**
   * Get the size of the file on the source.
   * @return the size of the file, in bytes.
   */
  public String getTotalSize() { return this.totalSize; }

  /**
   * Set the size of the file on the source.
   * @param value the size of the file, in bytes.
   */
  private void setTotalSize(String value) { this.totalSize = value; }

  /**
   * Get the text informing the size of the download so far,<br>
   * how much time it will take to the downloaded be completed,<br>
   * and the total download size.
   * @return the download information and progress.
   */
  public String getTimeEstimatedMsg() { return this.timeEstimatedMsg; }

  /**
   * Set the text informing the size of the download so far,<br>
   * how much time it will take to the downloaded be completed,<br>
   * and the total download size.
   * @param value the download information and progress.
   */
  private void setTimeEstimatedMsg(String value) { this.timeEstimatedMsg = value; }

  /**
   * Get how many hours are needed to finish the download.
   * @return the hours remaining.
   */
  public String getEstimatedHours() { return this.estimatedHours; }

  /**
   * Set how many hours are needed to finish the download.
   * @param value the hours remaining
   */
  private void setEstimatedHours(String value) { this.estimatedHours = value; }

  /**
   * Get how many minutes are needed to finish the download.
   * @return the minutes remaining.
   */
  public String getEstimatedMinutes() { return this.estimatedMinutes; }

  /**
   * Set how many minutes are needed to finish the download.
   * @param value the minutes remaining
   */
  private void setEstimatedMinutes(String value) { this.estimatedMinutes = value; }

  /**
   * Get how many seconds are needed to finish the download.
   * @return the seconds remaining.
   */
  public String getEstimatedSeconds() { return this.estimatedSeconds; }

  /**
   * Set how many seconds are needed to finish the download.
   * @param value the seconds remaining
   */
  private void setEstimatedSeconds(String value) { this.estimatedSeconds = value; }

  /**
   * Get the download progress so far.
   * @return the progress value in percent.
   */
  public String getProgressPercent()
  {
    if ((this.progressPercent == null) || (this.progressPercent.isEmpty()))
    {
      setProgressPercent("0");
    }
    return this.progressPercent;
  }

  /**
   * Set the download progress so far.
   * @param value the progress value in percent.
   */
  private void setProgressPercent(String value) { this.progressPercent = value; }

  /**
   * Get the download speed.
   * @return the speed in a proper measure unit.
   */
  public String getDownloadSpeed() { return this.downloadSpeed; }

  /**
   * Set the download speed.
   * @param value the speed in a proper measure unit.
   */
  public void setDownloadSpeed(String value) { this.downloadSpeed = value; }

  /**
   * Logger basic constructor.
   */
  private void setLogging()
  {

  }

  /**
   * Method for downloading the CoreFile.
   */
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
      JSONUtility ju = new JSONUtility();
      downloadURL = new URL(ju.readValue(DexCraftFiles.coreFileLinkFile, "URLs", "CoreFileURL"));
      logger.log("INFO", "Baixando CoreFile...");
      FileUtils.copyURLToFile(downloadURL, DexCraftFiles.coreFile);
      logger.log("INFO", "Download concluído...");
    }
    catch (MalformedURLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Download.coreFile()");
    }
    catch (IOException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Download.coreFile()");
    }
    if (!DexCraftFiles.coreFile.exists())
    {
      logger.log("***ERRO***", "EXCEÇÃO EM Download.coreFile() - ARQUIVO COREFILE NÃO ENCONTRADO");
      alerts.noCoreFile();
    }
  }

  /**
   * Method for download a zip file.<br>
   * Has some logic just to prevent trivial errors.
   * @param url the download URL.
   * @param destFolder the download destination folder.
   * @param destZip the downloaded file location.
   * @see #downloadWithProgress(java.lang.String, java.io.File)
   */
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

  /**
   * Proceeds with the download of a zip file, showing<br>
   * the progress as it advances.
   * @param url the download URL
   * @param destZip the downloaded file location.
   */
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
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Download.downloadWithProgress(String, File)");
    }
    catch (IOException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Download.downloadWithProgress(String, File)");
    }
  }

  /**
   * Updates the download progress using proper byte and time measures.
   * @param progressPercent the progress, in percent.
   * @param fileSize the download size.
   * @param downloadedSize the size of the file downloaded so far.
   * @param startTime the timestamp which defines when the download started.
   * @param currentTime the current timestamp to compare how many miliseconds has passed.
   */
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

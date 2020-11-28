package net.dex.dexcraft.launcher.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;



/**
 * Class for installing provisioned packages (extraction)
 */
public class Install
{
  private Alerts alerts = new Alerts();
  private Logger logger = new Logger();
  private ZipFile zipFile = null;
  private String installingFileName = "";
  private String totalFilesQuantity = "";
  private String installingFilePosition = "";
  private String progressPercent = "";
  private NumberFormat formatter = new DecimalFormat("#0.00");

  /**
   * Get the name of the file it is been extracted.
   * @return the name of the file.
   */
  public String getInstallingFileName() { return this.installingFileName; }

  /**
   * Set the name of the file it is been extracted.
   * @param fileName the name of the file.
   */
  private void setInstallingFileName(String fileName) { this.installingFileName = fileName; }

  /**
   * Get the total of files which be installed.
   * @return the quantity of files to be installed.
   */
  public String getTotalFilesQuantity() { return this.totalFilesQuantity; }

  /**
   * Set the total of files which be installed.
   * @param quantity the quantity of files to be installed.
   */
  private void setTotalFilesQuantity(String quantity) { this.totalFilesQuantity = quantity; }

  /**
   * Get the position of the current intalled file on the list.
   * @return the current ordinal position of the installed file.
   */
  public String getInstallingFilePosition() { return this.installingFilePosition; }

  /**
   * Set the position of the current intalled file on the list.
   * @param position the current ordinal position of the installed file.
   */
  private void setInstallingFilePosition(String position) { this.installingFilePosition = position; }

  /**
   * Get the current progress in percentual.
   * @return the current progress percent.
   */
  public String getProgressPercent() { return this.progressPercent; }

  /**
   * Set the current progress in percentual.
   * @param percent the current progress percent.
   */
  private void setProgressPercent(String percent) { this.progressPercent = percent; }

  /**
   * Logger basic constructor
   */
  private void setLogging()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  /**
   * Method for installing a component which was downloaded
   * under a zip file.
   * @param zipResource the installing source
   * @param destinationDir the installation directory
   */
  public void downloadedZipResource(File zipResource, File destinationDir)
  {
    setLogging();
    if (!zipResource.exists())
    {
      logger.log("***ERRO***", "EXCEÇÃO EM Install.downloadedZipResource(File, File) - ARQUIVO DE RECURSO NÃO ENCONTRADO.");
      alerts.tryAgain();
    }
    else
    {
      try
      {
        logger.log("INFO", "Iniciando instalação do arquivo solicitado...");
        zipFile = new ZipFile(zipResource);
        int i = 0;
        int fileQuantity = 0;
        Enumeration<ZipArchiveEntry> zipEntries = zipFile.getEntriesInPhysicalOrder();
        while (zipEntries.hasMoreElements())
        {
          ZipArchiveEntry entry = zipEntries.nextElement();
          if (!entry.isDirectory())
          {
            fileQuantity++;
          }
        }
        zipEntries = zipFile.getEntriesInPhysicalOrder();
        while (zipEntries.hasMoreElements())
        {
          ZipArchiveEntry entry = zipEntries.nextElement();
          File outFile = new File(destinationDir,entry.getName());
          if (!outFile.getParentFile().exists())
          {
            outFile.mkdirs();
          }
          if (entry.isDirectory())
          {
            outFile.mkdir();
          }
          else
          {
            i++;
            double divisor = (double)fileQuantity / i ;
            double progress = 100 / divisor;
            String progressOutput = formatter.format(progress);
            String fileQuantityOutput = Integer.toString(fileQuantity);
            String entryName = entry.getName();
            entryName = entryName.substring(entryName.lastIndexOf("/")+1, entryName.length());
            setInstallingFileName(entryName);
            setTotalFilesQuantity(fileQuantityOutput);
            setInstallingFilePosition(Integer.toString(i));
            setProgressPercent(progressOutput);
            logger.log("INFO", "Instalando: " + getInstallingFileName());
            logger.log("INFO", "Progresso: " + i + " / " + getTotalFilesQuantity() +"..."+ getProgressPercent() + "%");
            InputStream zipStream = null;
            OutputStream outFileStream = null;
            zipStream = zipFile.getInputStream(entry);
            outFileStream = new FileOutputStream(outFile);
            try
            {
              IOUtils.copy(zipStream,outFileStream);
            }
            finally
            {
              IOUtils.closeQuietly(zipStream);
              IOUtils.closeQuietly(outFileStream);
            }
          }
        }
      }
      catch (IOException ex)
      {
        alerts.exceptionHandler(ex, "EXCEÇÃO EM Install.downloadedZipResource(File, File)");
        Close.close(1);
      }
      finally
      {
        ZipFile.closeQuietly(zipFile);
      }
    }
  }


// EXAMPLE OF HOW TO DEVELOP THE INSTALL THREAD WITH MONITORING AND OUTPUT

//  public static void main(String[] args)
//  {
//    File zipResource = new File("C:/Origem/Destino.zip");
//    File destinationDir = new File("C:/Destino");
//    Install test = new Install();
//    Thread testDownload = new Thread(()->
//    {
//      test.downloadedZipResource(zipResource, destinationDir);
//    });
//    testDownload.start();
//    String checkFile = " ";
//    while(testDownload.isAlive())
//    {
//      if (!(test.getInstallingFileName()).equals(""))
//      {
//        if (!test.getInstallingFileName().equals(checkFile))
//        {
//          System.out.println("Instalando " + test.getInstallingFileName());
//          System.out.println("Posição " + test.getInstallingFilePosition() + " / " + test.getTotalFilesQuantity());
//          System.out.println("Progresso: " + test.getProgressPercent() + "%");
//          checkFile = test.getInstallingFileName();
//        }
//      }
//      try
//      {
//        Thread.sleep(10);
//      }
//      catch (InterruptedException ex)
//      {
//        //
//      }
//    }
//  }

}

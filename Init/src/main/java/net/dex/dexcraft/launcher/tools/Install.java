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
 *
 *
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


  public String getInstallingFileName() { return this.installingFileName; }

  private void setInstallingFileName(String fileName) { this.installingFileName = fileName; }

  public String getTotalFilesQuantity() { return this.totalFilesQuantity; }

  private void setTotalFilesQuantity(String quantity) { this.totalFilesQuantity = quantity; }

  public String getInstallingFilePosition() { return this.installingFilePosition; }

  private void setInstallingFilePosition(String position) { this.installingFilePosition = position; }

  public String getProgressPercent() { return this.progressPercent; }

  private void setProgressPercent(String percent) { this.progressPercent = percent; }

  private void setLogging()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }


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
        logger.log(ex, "***ERRO***", "EXCEÇÃO EM Install.downloadedZipResource(File, File)");
        alerts.exceptionHandler(ex, "ERRO DURANTE A INSTALAÇÃO");
        Cache.closeOnError();
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

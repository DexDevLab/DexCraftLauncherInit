package net.dex.dexcraft.launcher.tools;


import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.Cache;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.Logger;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;



/**
 *
 *
 */
public class Install
{
  private Alerts alerts = new Alerts();
  private static Logger logger = new Logger();
  private ZipFile zipFile;
  private List fileHeaderList;
  private String installingFileName = "";
  private String totalFilesQuantity = "";
  private String installingFilePosition = "";
  private String progressPercent = "";
  private NumberFormat formatter = new DecimalFormat("#0.0");


  public String getInstallingFileName() { return this.installingFileName; }

  private void setInstallingFileName(String fileName) { this.installingFileName = fileName; }

  public String getTotalFilesQuantity() { return this.totalFilesQuantity; }

  private void setTotalFilesQuantity(String quantity) { this.totalFilesQuantity = quantity; }

  public String getInstallingFilePosition() { return this.installingFilePosition; }

  private void setInstallingFilePosition(String position) { this.installingFilePosition = position; }

  public String getProgressPercent() { return this.progressPercent; }

  private void setProgressPercent(String percent) { this.progressPercent = percent; }

  private static void setLogging()
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
        fileHeaderList = zipFile.getFileHeaders();
        for (int i = 0; i < fileHeaderList.size(); i++)
        {
          double fileQuantity = (double) fileHeaderList.size();
          double percentBase = fileQuantity / 100;
          double progress = ((i+1) / percentBase);
          String progressOutput = formatter.format(progress);
          String fileQuantityOutput = Long.toString(Math.round(fileQuantity));
          FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
          File f = new File (fileHeader.getFileName());
          String fileName = (f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1));
          setInstallingFileName(fileName);
          setTotalFilesQuantity(fileQuantityOutput);
          setInstallingFilePosition(Integer.toString(i+1));
          setProgressPercent(progressOutput);
          logger.log("INFO", "Instalando: " + fileName);
          logger.log("INFO", "Progresso: " + (i + 1) + " / " + fileHeaderList.size() +"..."+(progressOutput) + "%");
          zipFile.extractFile(fileHeader, destinationDir.toString());
        }
      }
      catch (ZipException e)
      {
        logger.log(e, "***ERRO***", "EXCEÇÃO EM Install.downloadedZipResource(File, File)");
        alerts.exceptionHandler(e, "ERRO DURANTE A INSTALAÇÃO");
        Cache.closeOnError();
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

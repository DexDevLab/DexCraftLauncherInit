package net.dex.dexcraft.launcher.check;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.Logger;
import net.dex.dexcraft.launcher.tools.ScriptFileReader;
import org.apache.commons.io.IOUtils;


/**
 *
 *
 */
public class ReqCheck
{
  public static void ReqCheck()
  {
    Alerts alerts = new Alerts();
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    ProcessBuilder getMem = new ProcessBuilder("cmd", "/c", "wmic ComputerSystem get TotalPhysicalMemory");
    try
    {
      boolean isx64 = true;
      logger.log("INFO", "Coletando dados da quantidade de RAM no computador...");
      String getMemOutput = IOUtils.toString(getMem.start().getInputStream(), StandardCharsets.UTF_8);
      getMemOutput = getMemOutput.replace("TotalPhysicalMemory", "").trim();
      long divisor = 998400000L;
      long memorySize = Long.parseLong(getMemOutput);
      double memorySizeResult = (double) memorySize/divisor;
      float resultdec = (float) memorySizeResult;
      int ramresult = Math.round(resultdec);
      logger.log("INFO", "O computador possui " + ramresult + "GB de RAM instalados.");
      ScriptFileReader sfr = new ScriptFileReader();
      long reqMin = Long.parseLong(sfr.getOutputEntry(DexCraftFiles.coreFile, "ReqsMinimumRAM"));
      double reqMinD = (reqMin / 1000);
      String arch = System.getenv("ProgramFiles(x86)");
      logger.log("INFO", "Coletando informações sobre o Sistema Operacional...");
      if (arch == null)
      {
       logger.log("INFO", "Detectado Windows de 32 bits.");
       isx64 = false;
      }
      else
      {
        logger.log("INFO", "Detectado Windows de 64 bits.");
      }
      if ((ramresult < reqMinD) | (isx64 == false))
      {
        logger.log("***ERRO***", " O COMPUTADOR NÃO ATENDE AOS REQUISITOS MÍNIMOS DE HARDWARE E SOFTWARE.");
        alerts.noReq();
      }
    }
    catch (IOException ex)
    {
      logger.log(ex, "***ERRO***", "EXCEÇÃO EM ReqCheck.ReqCheck()");
      alerts.tryAgain();
    }

  }
}

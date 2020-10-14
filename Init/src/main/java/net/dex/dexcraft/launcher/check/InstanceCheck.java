package net.dex.dexcraft.launcher.check;


import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.FileIO;
import net.dex.dexcraft.launcher.tools.Logger;


/**
 *
 */
public class InstanceCheck
{

  public static void InstanceCheck()
  {
    Logger logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
    logger.log("INFO", "Verificando se já existe uma instância do programa na memória...");
    if (DexCraftFiles.instanceLock.exists())
    {
      logger.log("INFO", "Foi encontrada uma instância do programa na memória.");
      Alerts alerts = new Alerts();
      alerts.doubleInstance();
    }
    logger.log("INFO", "Não foi encontrada uma instância do programa na memória.");
    FileIO fio = new FileIO();
    fio.criar(DexCraftFiles.instanceLock);
  }

}

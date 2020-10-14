package net.dex.dexcraft.launcher.tools;


import java.io.File;

/**
 *
 *
 */
public class Cache
{
  private static Logger logger = new Logger();
  private static Clean clean = new Clean();


  private static void setLogging()
  {
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  public static void closeOnError()
  {
    setLogging();
    logger.log("INFO", "Limpando Cache e Encerrando...");
    clean.excluir(DexCraftFiles.adminCheck, false);
    clean.excluir(DexCraftFiles.runFolder, true);
    clean.excluir(DexCraftFiles.tempFolder, true);
    clean.excluir(DexCraftFiles.logLock, false);
    System.exit(0);
  }

  public static void close()
  {
    setLogging();
    logger.log("INFO", "Limpando Cache e Encerrando...");
    clean.excluir(DexCraftFiles.tempFolder, true);
    clean.excluir(DexCraftFiles.adminCheck, false);
    System.exit(0);
  }

  public static void open()
  {
    setLogging();
    logger.log("INFO", "Preparando Cache...");
    clean.excluir(DexCraftFiles.tempFolder, true);
    clean.excluir(DexCraftFiles.instanceDCBSLock, false);
  }


  private static class Clean extends FileIO
  {
    @Override
    public void excluir(File source, boolean includeParentDir)
    {
      if (source.exists())
      {
        super.excluir(source, includeParentDir);
      }
      else
      {
        logger.log("INFO", "SOURCE \"" + source.toString() + "\" não foi encontrado.");
      }
    }
  }

}

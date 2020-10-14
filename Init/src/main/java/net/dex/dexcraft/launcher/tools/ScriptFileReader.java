package net.dex.dexcraft.launcher.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import net.dex.dexcraft.launcher.tools.Alerts;
import net.dex.dexcraft.launcher.tools.DexCraftFiles;
import net.dex.dexcraft.launcher.tools.Logger;


/**
 *
 *
 */
public class ScriptFileReader
{

  private static Logger logger;
  private Alerts alerts;

  public ScriptFileReader()
  {
    alerts = new Alerts();
    logger = new Logger();
    logger.setLogLock(DexCraftFiles.logLock);
    logger.setMessageFormat("yyyy/MM/dd HH:mm:ss");
    logger.setLogNameFormat("yyyy-MM-dd--HH.mm.ss");
    logger.setLogDir(DexCraftFiles.logFolder);
  }

  private File scriptFile;
  private ArrayList<String> outputList;
  private String outputEntry;
  private ArrayList<String> scriptArray;
  private int getIndex = 0;


  private void setScriptFile(File file) { this.scriptFile = file; }

  private File getScriptFile() { return this.scriptFile; }

  private void readScript(File script)
  {
    try
    {
      scriptArray = new ArrayList<>();
      setScriptFile(script);
      FileInputStream fis= new FileInputStream(getScriptFile().toString());
      Scanner sc=new Scanner(fis);
      while(sc.hasNextLine())
      {
        scriptArray.add(sc.nextLine().trim());
      }
      sc.close();
    }
    catch(IOException ex)
    {
      logger.log(ex, "***ERRO***", "EXCEÇÃO em ScriptFileReader.readScript(File) - ERRO NA LEITURA");
      alerts.exceptionHandler(ex, "EXCEÇÃO em ScriptFileReader.readScript(File)");
    }
  }


  public String getOutputEntry(File script, String category)
  {
    readScript(script);
    outputEntry = "";
    scriptArray.forEach((line)->
    {
      if (line.contentEquals(category))
      {
        outputEntry = scriptArray.get(getIndex+2);
      }
      getIndex++;
    });
    getIndex = 0;
    scriptArray.clear();
    return outputEntry;
  }

  public ArrayList<String> getOutputList(File script, String category)
  {
    readScript(script);
    outputList = new ArrayList<>();
    scriptArray.forEach((line)->
    {
      if (line.contentEquals(category))
      {
        while(!(scriptArray.get(getIndex+2).equals("}")))
        {
          outputList.add(scriptArray.get(getIndex+2));
          getIndex++;
        }
      }
      getIndex++;
    });
    getIndex = 0;
    scriptArray.clear();
    return outputList;
  }

}

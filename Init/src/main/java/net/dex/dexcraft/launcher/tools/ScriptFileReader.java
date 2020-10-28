package net.dex.dexcraft.launcher.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.io.*;


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
  private int entriesIndex = 0;
  private boolean secondBracket = false;
  private int indexToSkip = 0;
  private String tabulation = "\t";


  private void setScriptFile(File file) { this.scriptFile = file; }

  private File getScriptFile() { return this.scriptFile; }

  private void readScript(File script)
  {
    try
    {
      scriptArray = new ArrayList<>();
      setScriptFile(script);
      FileInputStream fis= new FileInputStream(getScriptFile().toString());
      Scanner sc = new Scanner(fis);
      while(sc.hasNextLine())
      {
        scriptArray.add(sc.nextLine().trim());
      }
      sc.close();
      fis.close();
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

  public void replaceEntry(File script, String category, String entry)
  {
    readScript(script);
    outputList = new ArrayList<>();
    scriptArray.forEach((line)->
    {
      if (line.contains(category))
      {
        indexToSkip = getIndex+2;
      }
      if ( (indexToSkip == 0) || (getIndex != indexToSkip) )
      {
        if(secondBracket)
        {
          if (line.equals("}"))
          {
            secondBracket = false;
            tabulation = "\t";
          }
          else if (!line.equals("{"))
          {
            tabulation = "\t\t";
          }
        }
        else
        {
          if( (getIndex == 0) || (scriptArray.size() == getIndex+1) )
          {
            tabulation = "";
          }
          else
          {
            secondBracket = true;
            tabulation = "\t";
          }
        }
        outputList.add(tabulation + line);
      }
      else
      {
        outputList.add("\t\t" + entry);
      }
      getIndex++;
    });
    getIndex = 0;
    scriptArray.clear();
    fileReplacer(script);
  }

  public void replaceEntry(File script, String category, ArrayList<String> entries)
  {
    readScript(script);
    outputList = new ArrayList<>();
    scriptArray.forEach((line)->
    {
      if (line.contains(category))
      {
        indexToSkip = getIndex+2;
      }
      if ( (indexToSkip == 0) || (getIndex != indexToSkip) )
      {
        if(secondBracket)
        {
          if (line.equals("}"))
          {
            secondBracket = false;
            tabulation = "\t";
          }
          else if (!line.equals("{"))
          {
            tabulation = "\t\t";
          }
        }
        else
        {
          if( (getIndex == 0) || (scriptArray.size() == getIndex+1) )
          {
            tabulation = "";
          }
          else
          {
            secondBracket = true;
            tabulation = "\t";
          }
        }
        outputList.add(tabulation + line);
      }
      else
      {
        outputList.add("\t\t" + entries.get(entriesIndex));
        entriesIndex++;
        if(entries.size() > entriesIndex)
        {
          indexToSkip++;
        }
      }
      getIndex++;
    });
    getIndex = 0;
    entriesIndex = 0;
    indexToSkip = 0;
    scriptArray.clear();
    fileReplacer(script);
  }

  private void fileReplacer(File script)
  {
    try
    {
      File newFile = new File(script.toString() + ".new");
      FileUtils.touch(newFile);
      final PrintWriter pw = new PrintWriter(new FileWriter(newFile));
      outputList.forEach((line) ->
      {
        pw.println(line);
      });
      pw.close();
      FileUtils.deleteQuietly(script);
      newFile.renameTo(script);
    }
    catch (IOException ex)
    {
      System.out.println("ERRO");
    }
    outputList.clear();
  }

}

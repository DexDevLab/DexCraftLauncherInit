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
 * A Class to read and modify my
 * own script file type.
 */
public class ScriptFileReader
{

  private static Logger logger;
  private Alerts alerts;

  /**
   * Constructor (custom alerts and logging).
   */
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


  /**
   * Set the script file which be manipulated.
   * @param file the script file.
   */
  private void setScriptFile(File file) { this.scriptFile = file; }

  /**
   * Get the script file which be manipulated.
   * @return the script file.
   */
  private File getScriptFile() { return this.scriptFile; }


  /**
   * Perform a full reading of the script file.
   * @param script the script file to be read
   */
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
      alerts.exceptionHandler(ex, "EXCEÇÃO EM ScriptFileReader.readScript(File)");
    }
  }

  /**
   * Returns a single value from the only field of a category.
   * @param script the script file to be read
   * @param category the category where the entry is located
   * @return the entry.
   */
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

  /**
   * Returns an entire list of the values from a category.
   * @param script the script file.
   * @param category the category to be read.
   * @return the entries of a category
   */
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

  /**
   * Replaces a single value of a category to another.
   * @param script the script file
   * @param category the category to be read
   * @param entry the entry to replace the current on the category.
   */
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

  /**
   * Replaces a all the values of a category to another.
   * @param script the script file
   * @param category the category to be read
   * @param entries the entries to be replaced to. Each value from
   * this list will be changed in order by index.<br> If the quantity of
   * entries of this list is bigger than the current number of fields
   * on the category, it will overwrite other categories or entries in the
   * script file. That might cause errors.
   */
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

  /**
   * Removes the old script file containing the old values
   * and makes avaiable the new script file containing the
   * new values.
   * @param script the script file
   * @see #replaceEntry(java.io.File, java.lang.String, java.util.ArrayList)
   * @see #replaceEntry(java.io.File, java.lang.String, java.lang.String)
   */
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
      System.out.println("");
      System.out.println("[***ERRO***] - EXCEÇÃO em Logger.fileReplacer(File) - " + ex.getMessage());
      System.out.println("");
    }
    outputList.clear();
  }

}

package net.dex.dexcraft.commons.dto;


import static net.dex.dexcraft.commons.Commons.alerts;
import static net.dex.dexcraft.commons.Commons.logger;
import net.dex.dexcraft.commons.dao.JsonDAO;
import net.dex.dexcraft.commons.tools.DexCraftFiles;


/**
 * DTO for SQL connection.
 */
public class SqlDTO
{

  // JSON Utility instance
  private static JsonDAO json = new JsonDAO();

  //****************Database server variables********************//
  private static String dbClass;
  private static String dbDriver;
  private static String dbName;
  private static String dbAddress;
  private static String dbPort;
  private static String dbUser;
  private static String dbPassword;

  //*******************************************PARSERS*******************************//
  /**
   * PARSE Database Server class from JSON file.
   * @param dbclass the JSON read method.
   */
  private static void parseDBClass(String dbclass)
  {
    dbClass = dbclass;
  }

  /**
   * PARSE Database Server driver from JSON file.
   * @param driver the JSON read method.
   */
  private static void parseDBDriver(String driver)
  {
    dbDriver = driver;
  }

  /**
   * PARSE Database Server table name from JSON file.
   * @param name the JSON read method.
   */
  private static void parseDBName(String name)
  {
    dbName = name;
  }

  /**
   * PARSE Database Server address from JSON file.
   * @param addr the JSON read method.
   */
  private static void parseDBAddress(String addr)
  {
    dbAddress = addr;
  }

  /**
   * PARSE Database Server port from JSON file.
   * @param port the JSON read method.
   */
  private static void parseDBPort(String port)
  {
    dbPort = port;
  }

   /**
   * PARSE Database Server username from JSON file.
   * @param user the JSON read method.
   */
  private static void parseDBUser(String user)
  {
    dbUser = user;
  }

  /**
   * PARSE Database Server password from JSON file.
   * @param pass the JSON read method.
   */
  private static void parseDBPassword(String pass)
  {
   dbPassword = pass;
  }

  //*******************************************GETTERS*******************************//
  /**
   * GET Database Class stored in DTO variable.
   * @return the class name.
   */
  public static String getDBClass()
  {
    return dbClass;
  }

   /**
   * GET Database Driver stored in DTO variable.
   * @return the driver name.
   */
  public static String getDBDriver()
  {
    return dbDriver;
  }

   /**
   * GET Database Table Name stored in DTO variable.
   * @return the table name.
   */
  public static String getDBName()
  {
    return dbName;
  }

   /**
   * GET Database Server Address stored in DTO variable.
   * @return the server address.
   */
  public static String getDBAddress()
  {
    return dbAddress;
  }

   /**
   * GET Database Server Port stored in DTO variable.
   * @return the port.
   */
  public static String getDBPort()
  {
    return dbPort;
  }

  /**
   * GET Database Server Username stored in DTO variable.
   * @return the username.
   */
  public static String getDBUser()
  {
    return dbUser;
  }

   /**
   * GET Database Server Password stored in DTO variable.
   * @return the password
   */
  public static String getDBPassword()
  {
    return dbPassword;
  }

  //*******************************************SETTERS*******************************//
  // SQL Server doesn't need SETTER methods since
  //  the data into the CoreFile won't be changed, and the Launcher
  //  Properties JSON file doesn't have to store them.


   /**
   * Parse assets from JSON file into DTO.
   */
  public static void parseSQLAssets()
  {
    // Interrupt Launcher if asset source is absent
    if (!DexCraftFiles.coreFile.exists())
    {
      logger.log("***ERRO***", "COREFILE NÃO ENCONTRADO.");
      alerts.noCoreFile();
    }
    else
    {
      parseDBClass(json.readValue(DexCraftFiles.coreFile, "DBServer", "DBClass"));
      parseDBDriver(json.readValue(DexCraftFiles.coreFile, "DBServer", "DBDriver"));
      parseDBName(json.readValue(DexCraftFiles.coreFile, "DBServer", "DBName"));
      parseDBAddress(json.readValue(DexCraftFiles.coreFile, "DBServer", "ServerWebAddress"));
      parseDBPort(json.readValue(DexCraftFiles.coreFile, "DBServer", "ServerPort"));
      parseDBUser(json.readValue(DexCraftFiles.coreFile, "DBServer", "ServerUser"));
      parseDBPassword(json.readValue(DexCraftFiles.coreFile, "DBServer", "ServerPassword"));

      logger.log("INFO",  getDBDriver().toUpperCase() + ": " + "Assets carregados.");
    }
  }

}

package net.dex.dexcraft.commons.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static net.dex.dexcraft.commons.Commons.alerts;
import static net.dex.dexcraft.commons.Commons.logger;


/**
 * SqlDAO Utility Class.
 */
public class SqlDAO
{

  private String dbClass;
  private String dbDriver;
  private String dbName;
  private String dbAddress;
  private String dbPort;
  private String dbUser;
  private String dbPassword;

  private String dbURL;
  private String createTableStatement;
  private String insertPlayerStatement;
  private String selectAllStatement;
  private String selectOneStatement;
  private String removePlayerStatement;
  private String editPlayerStatement;
  private Connection con;
  private boolean playerExists;
  private List<String> dblist = new ArrayList<>();
  private String result;

  private String dbLogPrefix;


  /**
   * Class constructor.
   * @param className the name of the Database Class.
   */
  public SqlDAO(String className)
  {
    try
    {
      Class.forName(className);

      this.createTableStatement = "CREATE TABLE IF NOT EXISTS players (\n"
                                  + "username varchar(20) PRIMARY KEY,\n"
                                  + "password varchar(20) NOT NULL,"
                                  + "backupTimestamp varchar(100) NOT NULL\n"
                                  +");";
      this.insertPlayerStatement = "INSERT INTO players("
                                  +"username, password, backupTimestamp) VALUES("
                                  +"?,?,?)";
      this.selectAllStatement = "SELECT * FROM players";
      this.selectOneStatement = "SELECT * FROM players WHERE username= ?";
      this.removePlayerStatement = "DELETE FROM players WHERE username = ?";
      this.editPlayerStatement = "UPDATE players SET password = ?, "
                                 + "backupTimestamp = ? "
                                 + "WHERE username = ?";
    }
    catch (ClassNotFoundException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database()");
    }
  }

  /**
   * GET the database URL.
   * @return the database URL.
   */
  private String getDBUrl()
  {
    return this.dbURL = "jdbc:" + getDBDriver() + "://" + getDBAddress() +":" + getDBPort() + "/" + getDBName() ;
  }

  /**
   * GET the prefix for logging.
   * @return the prefix for logging.
   */
  private String getDBLogPrefix()
  {
    return dbLogPrefix = getDBDriver().toUpperCase() + ": ";
  }

  /**
   * GET the Database driver.
   * @return the Database driver.
   */
  private String getDBDriver()
  {
    return dbDriver;
  }

  /**
   * SET the Database driver.
   * @param dbDriver the Database driver.
   */
  public void setDBDriver(String dbDriver)
  {
    this.dbDriver = dbDriver;
  }

  /**
   * GET the Database name.
   * @return the Database name.
   */
  private String getDBName()
  {
    return dbName;
  }

  /**
   * SET the Database name.
   * @param dbName the Database name.
   */
  public void setDBName(String dbName)
  {
    this.dbName = dbName;
  }

  /**
   * GET the Database address.
   * @return the Database address.
   */
  private String getDBAddress()
  {
    return dbAddress;
  }

  /**
   * SET the Database address.
   * @param dbAddress the Database address.
   */
  public void setDBAddress(String dbAddress)
  {
    this.dbAddress = dbAddress;
  }

  /**
   * GET the Database acess port.
   * @return the Database acess port.
   */
  private String getDBPort()
  {
    return dbPort;
  }

  /**
   * SET the Database acess port.
   * @param dbPort the Database acess port.
   */
  public void setDBPort(String dbPort)
  {
    this.dbPort = dbPort;
  }

  /**
   * GET the Database access user.
   * @return the Database access user.
   */
  private String getDBUser()
  {
    return dbUser;
  }

  /**
   * SET the Database access password.
   * @param dbUser the Database access password.
   */
  public void setDBUser(String dbUser)
  {
    this.dbUser = dbUser;
  }

  /**
   * GET the Database access password.
   * @return the Database access password.
   */
  private String getDBPassword()
  {
    return dbPassword;
  }

  /**
   * SET the Database access password.
   * @param dbPassword the Database access password.
   */
  public void setDBPassword(String dbPassword)
  {
    this.dbPassword = dbPassword;
  }


  /**
   * Stabilish a connection with the server.
   */
  public void connect()
  {
    try
    {
      con = DriverManager.getConnection(getDBUrl(), getDBUser(), getDBPassword());
      logger.log("INFO", getDBLogPrefix() + "Banco de dados conectado.");
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.connect()");
    }
  }


  /**
   * Disconnect properly a connection.
   */
  public void disconnect()
  {
    try
    {
      con.close();
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.disconnect()");
    }
  }


  /**
   * Creates Table in file if not existent.
   */
  public void createTable()
  {
    try
    {
      Statement stm = con.createStatement();
      stm.execute(createTableStatement);
      logger.log("INFO", getDBLogPrefix() + "Statement executado.");
      stm.close();
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.createTable()");
    }
  }

  /**
   * Add a new player to database.
   * @param user the player's username
   * @param pass the player's password
   * @param timestamp the last backup timestamp
   */
  public void insertPlayer(String user, String pass, String timestamp)
  {
    try
    {
      if (!playerExists(user))
      {
        PreparedStatement pstm = con.prepareStatement(insertPlayerStatement);
        pstm.setString(1,user);
        pstm.setString(2,pass);
        pstm.setString(3,timestamp);
        pstm.executeUpdate();
        logger.log("INFO", getDBLogPrefix() + "Statement executado.");
        pstm.close();
        logger.log("INFO", getDBLogPrefix() + "Dados do jogador inseridos corretamente no banco de dados.");
      }
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.insertPlayer(String, String, String)");
    }
  }

  /**
   * Check if player is present on database.
   * @param user the player's username
   * @return if the player is already listed on database
   */
  public boolean playerExists(String user)
  {
    this.playerExists = false;
    List<String> dbList = selectAll();
    dbList.forEach((item ->
    {
      if (item.contains(user))
      {
        this.playerExists = true;
      }
    }));
    return this.playerExists;
  }

  /**
   * Retrieves specific information about the selected table.
   * @param info the type of info that want to be accessed.<br>
   * It is the same as a column of a table.
   * @param user the username of the info would be provided.
   * @return the data queried (the datacell value).
   */
  public String getInfo(String info, String user)
  {
    result = "";
    try
    {
      PreparedStatement pstm = con.prepareStatement(selectOneStatement);
      pstm.setString(1,user);
      ResultSet rs = pstm.executeQuery();
      while (rs.next())
      {
        result = rs.getString(info);
      }
      rs.close();
      pstm.close();
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.getInfo(String, String)");
    }
    return result;
  }

  /**
   * List all players on database.
   * @return the list of players
   */
  public List<String> selectAll()
  {
    dblist.clear();
    dblist = new ArrayList<>();
    try
    {
      Statement stm = con.createStatement();
      ResultSet rs = stm.executeQuery(selectAllStatement);
      logger.log("INFO", getDBLogPrefix() + "Query executada.");
      while (rs.next())
      {
        dblist.add(rs.getString("username") + ", " + rs.getString("password") + ", " + rs.getString("backupTimestamp"));
      }
      logger.log("INFO", getDBLogPrefix() + "Statement executado.");
      rs.close();
      stm.close();
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.selectAll()");
    }
    return dblist;
  }

  /**
   * Remove a player entry on database.
   * @param user the player to remove from database.
   */
  public void removePlayer(String user)
  {
    try
    {
      Statement stm = con.createStatement();
      stm.executeUpdate(removePlayerStatement + "'" + user + "'");
      logger.log("INFO", getDBLogPrefix() + "Statement executado.");
      stm.close();
    }
    catch (SQLException e)
    {
      alerts.exceptionHandler(e, "EXCEÇÃO EM Database.removePlayer(String)");
    }
  }

  /**
   * Edit player data on database.
   * @param user the player's username.
   * @param pass the player's password.
   * @param timestamp the player's last backup timestamp.
   */
  public void editPlayer(String user, String pass, String timestamp)
  {
    try
    {
      PreparedStatement pstm = con.prepareStatement(editPlayerStatement);
      pstm.setString(1,pass);
      pstm.setString(2,timestamp);
      pstm.setString(3,user);
      pstm.executeUpdate();
      logger.log("INFO", getDBLogPrefix() + "Statement executado.");
      pstm.close();
    }
    catch (SQLException ex)
    {
      alerts.exceptionHandler(ex, "EXCEÇÃO EM Database.editPlayer(String, String, String)");
    }
  }


//  USAGE EXAMPLE
//  public static void main(String[] args)
//  {
//    File dbFile = new File("C:/players.db");
//    SQLite sq = new SQLite(dbFile.toString());
//    sq.createTable();
//    sq.insertPlayer("Teste", "1234", "01234567890");
//    sq.insertPlayer("Teste2", "12345", "01234567890");
//    sq.insertPlayer("Teste3", "12345", "01234567890");
//    sq.removePlayer("Teste");
//    System.out.println(sq.selectAll());
//    sq.editPlayer("Teste3", "1111111", "22222");
//    System.out.println(sq.selectAll());
//  }
}

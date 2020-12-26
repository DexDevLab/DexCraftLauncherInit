package net.dex.dexcraft.commons.dao;


/**
 * DAO Utility Class for FTP connection.
 */
public class FtpDAO
{

  private String address;
  private int port;
  private String user;
  private String password;
  private String workingDir;

  /**
   * GET the FTP Server's address.
   * @return the server address.
   */
  public String getAddress()
  {
    return address;
  }

  /**
   * SET the FTP Server's address.
   * @param address the server address.
   */
  public void setAddress(String address)
  {
    this.address = address;
  }

  /**
   * GET the FTP Server's port.
   * @return the server port.
   */
  public int getPort()
  {
    return port;
  }

  /**
   * SET the FTP Server's port.
   * @param port the server oort.
   */
  public void setPort(int port)
  {
    this.port = port;
  }

  /**
   * GET the FTP connection user
   * @return the user
   */
  public String getUser()
  {
    return user;
  }

  /**
   * SET the FTP Server's user
   * @param user the connection user
   */
  public void setUser(String user)
  {
    this.user = user;
  }

  /**
   * GET the FTP connection password.
   * @return the server password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * SET the FTP Server's password
   * @param password the connection password.
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * GET the FTP connection's working directory
   * @return the working directory.
   */
  public String getWorkingDir()
  {
    return workingDir;
  }

  /**
   * SET the FTP connection's working directory
   * @param workingDir the working directory
   */
  public void setWorkingDir(String workingDir)
  {
    this.workingDir = workingDir;
  }


//FTPClient uploadBkp = new FTPClient();
//          uploadBkp.connect(serverAddress,Integer.parseInt(serverPort));
//          uploadBkp.login(serverUser, serverPassword);
//          uploadBkp.enterLocalPassiveMode();
//          uploadBkp.changeWorkingDirectory (serverPlayerBkp + "/");
//          uploadBkp.makeDirectory(loginuser + ".new");
//          updateTitle("Enviando arquivos. Aguarde...");
//          Thread uploadInstance = new Thread(() ->
//          {
//            try
//            {
//              fileCheck1 = uploadSingleFile(uploadBkp, bkpzip.toString(), serverPlayerBkp + "/" + loginuser + ".new" + "/bkp.zip");
//            }
//            catch (IOException ex)
//            {
//              L.log(null,"ERRO", "ERRO CRÍTICO EM stepDoBkp() - FALHA AO ENVIAR ARQUIVOS");
//            }
//          });
//          uploadInstance.start();
//          monitorUpload(uploadBkp, bkpzip);
//          percent = 1;
//          value = 1;
//          while (percent <101)
//          {
//            try
//            {
//              softProgress(1, percent, 10);
//              Thread.sleep(1000);
//              if (percent == 100) { percent = 101;}
//            }
//            catch (InterruptedException ex)
//            {
//              L.log(ex, "ERRO", "EXCEÇÃO EM UploadProgress THREAD - FALHA NA INTERRUPÇÃO");
//            }
//          }
//          percent = 1;
//          value = -1;
//          updateMessage("");
//          updateProgress(value, 100);
//          try
//          {
//            if (uploadBkp.isConnected())
//            {
//              uploadBkp.logout();
//              uploadBkp.disconnect();
//            }
//          }
//          catch (IOException ex)
//          {
//            L.log(ex, "ERRO", "EXCEÇÃO EM stepValidateCredentials() - IMPOSSÍVEL DESCONECTAR A CONEXÃO FtpDAO EM ABERTO");
//          }

}

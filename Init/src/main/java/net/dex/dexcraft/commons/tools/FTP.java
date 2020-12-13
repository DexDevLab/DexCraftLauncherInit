package net.dex.dexcraft.commons.tools;


/**
 *
 */
public class FTP
{

  private String address;
  private int port;
  private String user;
  private String password;
  private String workingDir;

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }


  public int getPort()
  {
    return port;
  }

  public void setPort(int port)
  {
    this.port = port;
  }

  public String getUser()
  {
    return user;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getWorkingDir()
  {
    return workingDir;
  }

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
//            L.log(ex, "ERRO", "EXCEÇÃO EM stepValidateCredentials() - IMPOSSÍVEL DESCONECTAR A CONEXÃO FTP EM ABERTO");
//          }

}

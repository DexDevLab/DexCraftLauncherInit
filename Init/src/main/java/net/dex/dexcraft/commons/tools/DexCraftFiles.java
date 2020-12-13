package net.dex.dexcraft.commons.tools;


import java.io.File;


/**
 * Class containing all File references and objects.
 */
public class DexCraftFiles
{
  // Main program folders
  /** Root directory. */
  public static File mainFolder = new File("C:/DexCraft");
  /** Launcher logs location. */
  public static File logFolder = new File(mainFolder + "/logs");
  /** Temporary folder for common tasks. */
  public static File tempFolder = new File (mainFolder + "/temp");
  /** Folder containing the entire Launcher installed files, programs and data. */
  public static File launcherFolder = new File (mainFolder + "/launcher");

  // Launcher shortcuts
  /** Launcher shortcut source location. */
  public static File shortcutSrc = new File(launcherFolder + "/DexCraft Launcher.lnk");
  /** Shortcut destination to Windows default Program Folder. */
  public static File shortcutProgramFolder = new File (System.getenv("APPDATA") + "/Microsoft/Windows/Start Menu/Programs/DexCraft Launcher.lnk");
  /** Shortcut destination to user's Desktop. */
  public static File shortcutUserDesktop = new File ("C:/Users/"+ System.getenv("USERNAME") + "/Desktop/DexCraft Launcher.lnk");
  /** Shortcut destination to Default User's Desktop. */
  public static File shortcutDefaultDesktop = new File ("C:/Users/Default/Desktop/DexCraft Launcher.lnk");

  // Lockers and checkers
  /** File which identifies if the program is running as Administrator. */
  public static File adminCheck = new File ("C:/admin.dc");
  /**
   * File which identifies if the program is logging. Prevents logging on
   * different log files since each Class calls new instances of the same
   * Logger Class.
   */
  public static File logLock = new File (logFolder + "/log.dc");

  /**
   * The temporary file downloaded only for connection speed tests.
   */
  public static File downloadTestFile = new File(tempFolder + "/10M.iso");

  // Launcher main resources folders and files //
  /** Launcher's resources install directory. */
  public static File resFolder = new File(launcherFolder + "/res");
  /** Launcher's resources downloaded package to install. */
  public static File resZip = new File (tempFolder + "/resources.zip");

  // CoreFile assets
  /** File containing CoreFile download URL. */
  public static File coreFileLinkFile = new File (launcherFolder + "/cfurl.json");
  /** CoreFile location after downloaded properly. */
  public static File coreFile = new File(launcherFolder+ "/corecfg.json");


  /** DexCraft Launcher main file asset. This contains important
      information about the Launcher in current execution, as if
      the offline mode was enabled, instance lockers etc.**/
  public static File launcherProperties = new File (launcherFolder + "/DexCraftLauncher.json");
  /** Launcher's version update downloaded package to install. */
  public static File updateLauncherZip = new File (tempFolder + "/launcher.zip");
  /** DCBS's version update downloaded package to install. */
  public static File updateDCBSZip = new File (tempFolder + "/dcbs.zip");
  /** Init's version update downloaded package to install. */
  public static File updateInitZip = new File (tempFolder + "/init.zip");
}

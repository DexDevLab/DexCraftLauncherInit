package net.dex.dexcraft.launcher.tools;


import java.io.File;


/**
 *
 *
 */
public class DexCraftFiles
{
  // Main program folders
  public static File mainFolder = new File("C:/DexCraft");
  public static File logFolder = new File(mainFolder + "/logs");
  public static File tempFolder = new File (mainFolder + "/temp");
  public static File launcherFolder = new File (mainFolder + "/launcher");
  public static File runFolder = new File(launcherFolder + "/run");

  // Launcher shortcuts
  public static File shortcutSrc = new File(launcherFolder + "/DexCraft Launcher.lnk");
  public static File shortcutProgramFolder = new File (System.getenv("APPDATA") + "/Microsoft/Windows/Start Menu/Programs/DexCraft Launcher.lnk");
  public static File shortcutUserDesktop = new File ("C:/Users/"+ System.getenv("USERNAME") + "/Desktop/DexCraft Launcher.lnk");
  public static File shortcutDefaultDesktop = new File ("C:/Users/Default/Desktop/DexCraft Launcher.lnk");

  // Lockers and checkers
  public static File adminCheck = new File ("C:/admin.dc");
  public static File instanceLock = new File (runFolder + "/instance.dc");
  public static File instanceDCBSLock = new File (runFolder + "/instance.dcbs");
  public static File logLock = new File (logFolder + "/log.dc");
  public static File offlineModeFile = new File (launcherFolder + "/offlinemode.dc");

  // Launcher main resources folders and files //
  public static File resFolder = new File(launcherFolder + "/res");
  public static File resZip = new File (tempFolder + "/resources.zip");

  // CoreFile assets
  public static File coreFileLinkFile = new File (launcherFolder + "/cfurl.dc");
  public static File coreFile = new File(launcherFolder+ "/src/corecfg.dc");

  // DexCraft Launcher assets
  public static File versionFile = new File (launcherFolder + "/versions.dc");
  public static File updateLauncherZip = new File (tempFolder + "/launcher.zip");
  public static File updateDCBSZip = new File (tempFolder + "/dcbs.zip");
}

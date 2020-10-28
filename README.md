# License
This program and its files, its codes, implementation and functions are under the GNU AFFERO GENERAL PUBLIC LICENSE Version 3.

# DexCraft Launcher Init
This repository contains the DexCraft Launcher's Initializer, which I call DexCraft Launcher Init, or just Init for short.
This program initialize the Launcher, preparing files, downloading needed files, and installing basic components.

## Project purposes
All the thing started back on 2018 when I managed my own Minecraft Servers. At first I've only had one, and it was pretty difficult to me since on that time I just didn't have any ideas about how Minecraft (mostly the servers) worked.
My greatest dilema was everytime when I needed to change something (mostly .cfg files from some mod), and the changes needed to be done on client-side too in order to make effect. Well, everytime I needed to adjust something, I've had to send the file for each player, and help them to install the file properly, in the correct folder etc.
It was an exaustive journey, with files missing, edited by some naughty players sometimes, or corrupting other files or mods by accident. So I just needed to do an alternative.
Back then, at the last months of 2018, I started to do my most important project, where I learn so much, and keep learning until now.

## Initial purpose
DexCraft Launcher (and all its subprograms, like the Init) started with some simple ideas:

 **1.** Create an easy, automated, practical way to provide updates (since client-side .cfg edits to JRE updates)
 **2.** Securely save players' Journeymap Mod's maps and configuration in order they can have it later (in new installations or other computers)
 **3.** Easily install Minecraft clients, as simple as possible, in an interactive way of some sort.


## Versioning notes
It's important to say that the versioning was all wrong at the beginning since my inexperience in the matter. Plus, the code initially was so mixed up with my servers credentials that would be dangerous for me publish this project, and at that time, I couldn't make a private repo.
 

##  Current implementation
Nowadays, the Launcher is bigger, better and much more funcional as it was in the first intent. Now, DexCraft Launcher has much more funcionalities:

 **1.** UI with progress graphs, splash screens, percentage, menus and options, wallpaper background and sound (in progress), trying to be clean and cool up to the most;
 **2.** Login screen, which allows the player "create an account" for syncronizing data which isn't normally synced in Minecraft (JVM arguments, game options, graphic options, textures, render distance, FOV, keybinds etc), besides, allow the player to save their singleplayer worlds and having it everywhere;
 **3.** Allows the player easily installs soundpacks, textures and fancy extras to the game, in one click;
 **4.** Restore the Client if something just went wrong all of sudden.
 
## DexCraft Launcher's main applications
DexCraft Launcher is an application composed of 3 programs as it follows:
 
 **1. Initializer (Init)** -  The first application to run. This application is the one which is called after installation (before made manually using a zip file and a batch file, and now via an exe application), and also its the application which runs on the Launcher shortcuts. To the player, Init is just a splash screen, but this program has the following main tasks:
 ---------- Check if the System has the minimum requirements to run any Minecraft games from server at least at low profile configuration;
 ---------- Check if the System has internet connection to run the games and provides the option to play offline (singleplayer, without the multiplayer options);
 ---------- Check and update the Launcher version installed (or install if it isn't present);
 ---------- Run the Launcher.
 **2. Launcher (DCL)** -  The second application to run, DexCraft Launcher, or just DCL. This program has the biggest window, with the major funcionalities. It's from Launcher the player logs-in, prepare their configuration to sync, do backups or restores of it, installs extras, textures and soundpacks, apply JVM presets... all of it from a simple menu on the upper bar on the window. Internally, the Launcher also does:
 ---------- Check if the System is able to syncronize data, or it will just work offline;
 ---------- Check and update the Background Services to work properly;
 ---------- Update JRE version installed on System;
 ---------- Transmit the game profile data to the internal launcher in order to run Minecraft;
 ---------- Run the Background Services.
  **3. Background Services (DCL)** -  The third and last application to run, DexCraft Background Services, or just DCBS. This humble program will be next to the windows clock, on notification bar, in a form of a fancy icon. It will sync your data periodically as you play, assuring you have your journeymap's map updated and can be used in another machine if you properly logs in.
 ---------- Verify if the player logged in DexCraft Launcher properly in order to sync;
 ---------- Verify if you are currently playing the game and syncs it at specific minutes;

## DexCraft Launcher's points of attention
There's a lot to do in this project, and here I'm gonna point up some things are needed to do:

 - Javadoc. I did some archaic documentation on previous versions (not published ones) of the code, but I need to do a concise, balanced and effective documentation.
 - UML models. I want do to an UML model of the software, and thats a "must" on my future plans.
 - Customized progress bar. I'm currenly using a good progress bar, but I want a better one, and I want to do it by myself.
 - Use a better, more secure way of login and transmitting passwords (both players' and servers'). A database, crypto, just don't know. I made an archaic, simple way, creating a password cryptographed by a conversion common table but it isn't professional enough.
 - Get rid totally of Shiginima Launcher. Yes, I have Shiginima Launcher internally on my Launcher. And if you stop to think, my Launcher ISN'T a Launcher at all; it's just a program to do some fancy things before opening the Shiginima Launcher. I want my program load Minecraft by itself, but I can't find how...

## DexCraft Launcher's main files and objects
DexCraft Launcher needs a lot of specific files to work. You can find all of them in their proper classes, but I'm tell about some of them:
 **- Core File** - "CoreFile", "corefile" or just "cf" it's a common file containing essential data to make the Launcher work. The initial idea was using a JSON, but I couldn't make a way to build a fancy, editable and human-readable JSON, so I created my own "text document script type". Example of this text format:
 
> { 	 
		> Information 	 
		{ 	 
			value 1 	 
			value 2 	 
			value 3 	 
		} 	 
		Another Information 	
		 { 	 
			 value 1 	 
		 } 	 
	}

See the class ScriptFileReader for details.

**- cfurl** - "CoreFile URL" contain the URL to download the Core File. Without it, the program doesn't work. This file isn't provided on the repository because it could be harm the security of my server since the CoreFile may contain the servers passwords.

**- "locks"** - In order to allow the working of a lot of funcionalities, I needed to put empty files as "locks", to check if some task is running, or to make logging possible using the same txt file.
	 

# Logbook #
 
 
 ## v2.0.0-201027-432 ##
 I just don't know what happened to my libraries, but maven suddenly start refusing my zip library (net.lingala.zip4j) I was using. So I took it off and used a new one: Apache Commons Compress 1.20! Needed to do a lot of fixes on almost all classes because of it, and I still have no idea why does this happened so suddenly.
 *UpdateCheck.java - Implementation to use a single local file showing all the file versions needed - before, there was 1 file to inform each of the packages (1 file storing client version, 1 to store launcher version etc).
 *Init.java - Now DexCraft Init also checks for Background Services' updates! Changed the class to use a new implemented method to validate packages (see Validate.java).
 *Validate.java  - Added logging() method just to keep the standards. I create the method provisionedComponent(), which can be used for every single check, download and update, since they use the same standard (zip file mentioned in the version file and in the Core File with download url).
 *Alerts.java - Removed stupid boolean redundant logic  I did and just don't know why.
 *DexCraftFiles.java - Changed some variables names. Changed offline mode lock file location. Removed version files variables are not used anymore. Added DexCraft Background Services update zip variable.
 *Download.java - Changed download progress logic for a more accurate result.
 *FileIO.java - Imports update.
 *Install.java - Changed its entire logic to work correctly with Apache Commons Compress.
 *Logger - Imports update.
 *ScriptFileReader - Imports update.
 *Preloader.fxml - I don't know why, but I needed to change fxml id version.
 
 ## v2.0.0-201018-358 ##
Added the functionality to edit single entries on the script files (check "DexCraft Launcher's main files and objects" topic for more references), or edit the entire category of a script file, if needed.

## v2.0.0-201013-357 ##
 Repository creation.
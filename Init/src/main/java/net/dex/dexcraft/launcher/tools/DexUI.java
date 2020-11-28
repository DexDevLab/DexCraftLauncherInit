package net.dex.dexcraft.launcher.tools;


import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


/**
 * UI Tool to interact with the user,
 * as far from default thread as possible.
 */
public class DexUI
{
  private double globalProgressValue = 0;
  private ProgressBar pbar;
  private Label lb;

  /**
   * Define the UI's Progress Bar.
   * @param pb the Progress Bar
   */
  public void setProgressBar(ProgressBar pb)
  {
    this.pbar = pb;
  }

  /**
   * Define the UI's main label.<br>
   * The main label is a label which informs
   * to the user the most part of the processes.
   * @param lb the main label
   */
  public void setMainLabel(Label lb)
  {
    this.lb = lb;
  }

  /**
   * Updates the main label on UI.
   * @param text the text to be shown.
   */
  public void changeMainLabel(String text)
  {
    Platform.runLater(() -> {lb.setText(text);});
  }

  /**
   * Change the Progress Bar values and updates on UI.
   * @param isValuePercent true if the value is on percent
   * format or not
   * @param value the value of the progress to be shown
   * @param milis the transition interval (in miliseconds)
   * for each percent increased. With this parameter,
   * it is possible to give a smooth transition during
   * progress change.
   */
  public void changeProgress(boolean isValuePercent, double value, long milis)
  {
    try
    {
      // Time between progress bar changes. Increase
        // it to have a bigger gap between progress t
        // transitions.
      Thread.sleep(50);
      double progressValue = 0;
      if (isValuePercent)
      {
        progressValue = (value / 100);
      }
      else
      {
        progressValue = value;
      }
      double actualValue = globalProgressValue;
      if (progressValue < actualValue)
      {
        Platform.runLater(() -> {pbar.setProgress(-1);});
      }
      while (actualValue < progressValue)
      {
        actualValue = pbar.getProgress();
        final double adjust = actualValue;
        if (adjust < 0)
        {
          Platform.runLater(() ->
          {
            pbar.setProgress(globalProgressValue);
          });
        }
        else
        {
          Platform.runLater(() ->
          {
            pbar.setProgress(adjust + 0.01);
          });
        }
        Thread.sleep(milis);
      }
      final double resultValue = progressValue;
      Platform.runLater(() -> {pbar.setProgress(resultValue);});
      //Use the next 2 commented lines below to create an animation
        //after reaching the desired percent value (optional)
//      Thread.sleep(700);
      globalProgressValue = pbar.getProgress();
//      Platform.runLater(() -> {pbar.setProgress(-1);});
    }
    catch (InterruptedException ex)
    {
      System.out.println("");
      System.out.println("[***ERRO***] - EXCEÇÃO em DexUI.changeProgress(double, long) - " + ex.getMessage());
      System.out.println("");
    }
  }

  /**
   * Reset the Progress Bar when needed.
   */
  public void resetProgress()
  {
    globalProgressValue = 0.0;
    changeProgress(true, 0.1, 10);
  }

}

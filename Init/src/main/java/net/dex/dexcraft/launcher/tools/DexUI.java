package net.dex.dexcraft.launcher.tools;


import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


/**
 *
 *
 */
public class DexUI
{
  private double globalProgressValue = 0;
  private ProgressBar pbar;
  private Label lb;

  public void setProgressBar(ProgressBar pb)
  {
    this.pbar = pb;
  }

  public void setMainLabel(Label lb)
  {
    this.lb = lb;
  }

  public void changeMainLabel(String text)
  {
    Platform.runLater(() -> {lb.setText(text);});
  }

  public void changeProgress(boolean isValuePercent, double value, long milis)
  {
    try
    {
      Thread.sleep(1000);
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
//      Thread.sleep(700);
      globalProgressValue = pbar.getProgress();
//      Platform.runLater(() -> {pbar.setProgress(-1);});
    }
    catch (InterruptedException ex)
    {
      System.out.println("EXCEÇÃO EM DexUI.changeProgress(double, long) - " + ex.getMessage());
    }
  }

  public void resetProgress()
  {
    globalProgressValue = 0.0;
    changeProgress(true, 0.1, 10);
  }

}

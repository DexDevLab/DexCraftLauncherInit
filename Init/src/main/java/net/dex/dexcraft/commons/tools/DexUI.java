package net.dex.dexcraft.commons.tools;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;


/**
 * UI Tool to interact with the user,
 * as far from default thread as possible.
 */
public class DexUI
{
  private double globalProgressValue = 0;
  private ProgressBar pbar;
  private Label mainLb;
  private Label secLb;
  private TextField mainTf;
  private PasswordField mainPf;
  private Button mainBt;

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
    this.mainLb = lb;
  }

  /**
   * Updates the main label on UI.
   * @param text the text to be shown.
   */
  public void changeMainLabel(String text)
  {
    Platform.runLater(() -> {mainLb.setText(text);});
  }

  /**
   * Change if the main label is visible or not.
   * @param isVisible if the label will keep visible (true) or not (false).
   */
  public void changeMainLabelVisibility(boolean isVisible)
  {
    Platform.runLater(() -> {mainLb.setVisible(isVisible);});
  }

  /**
   * Define the UI's secondary label.<br>
   * The secondary label is a label which displays
   * minor info.
   * @param lb the secondary label
   */
  public void setSecondaryLabel(Label lb)
  {
    this.secLb = lb;
  }

  /**
   * Updates the main label on UI.
   * @param text the text to be shown.
   */
  public void changeSecondaryLabel(String text)
  {
    Platform.runLater(() -> {secLb.setText(text);});
  }

  /**
   * Define the UI's main text field.
   * @param tf the Text Field
   */
  public void setMainTextField(TextField tf)
  {
    this.mainTf = tf;
  }

  /**
   * Updates the main text on UI.
   * @param text the text to be shown.
   */
  public void changeMainTextField(String text)
  {
    Platform.runLater(() -> {mainTf.setText(text);});
  }

  /**
   * Define the UI's main password field.
   * @param pf the Password Field.
   */
  public void setMainPasswordField(PasswordField pf)
  {
    this.mainPf = pf;
  }

  /**
   * Updates the main password field on UI.
   * @param text the text to be shown.
   */
  public void changeMainPasswordField(String text)
  {
    Platform.runLater(() -> {mainPf.setText(text);});
  }

  /**
   * Define the UI's main button.
   * @param bt the Button
   */
  public void setMainButton(Button bt)
  {
    mainBt = bt;
  }

  public void setMainButtonDisable(boolean isDisabled)
  {
    Platform.runLater(() -> {mainBt.setDisable(isDisabled);});
  }




  public void setStyle(String obj, String style)
  {
    switch (obj)
    {
      case "MainLabel":
        Platform.runLater(() -> {mainLb.setStyle(style);});
        break;
      case "SecLabel":
        Platform.runLater(() -> {secLb.setStyle(style);});
        break;
      case "MainTextField":
        Platform.runLater(() -> {mainTf.setStyle(style);});
        break;
      case "MainPasswordField":
        Platform.runLater(() -> {mainPf.setStyle(style);});
        break;
      default:
        break;
    }
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
        // it to have a bigger gap between progress
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

  /**
   * Constructor for Tooltips.
   * @param text the text to show on Tooltip.
   * @return the Tooltip itself.
   */
  public Tooltip tooltipBuilder(String text)
  {
    Tooltip tooltip = new Tooltip();
    tooltip.setWrapText(true);
    tooltip.setFont(new Font("MS Outlook", 13));
    tooltip.setText(text);
    return tooltip;
  }

}

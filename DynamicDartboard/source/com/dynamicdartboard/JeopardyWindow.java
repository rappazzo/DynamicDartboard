package com.dynamicdartboard;
import java.awt.*;

public class JeopardyWindow extends Window implements Runnable, Animation {
   Label line0;
   Label line1;
   Label line2;
   Label line3;

   public JeopardyWindow(Window parent) {
      super(parent);
   }

   public void init(String name) {
      setSize(1024 - 2 * DartBoard.getScreenWidthOffset(), 668 - 2 * DartBoard.getScreenHeightOffset());
      setLocation(DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      setLayout(null);
      Font font = new Font("Garamond", Font.BOLD, 55);
      line0 = new Label("FINAL E-DART", Label.CENTER);
      line1 = new Label("The worst E-Dart ", Label.CENTER);
      line2 = new Label("player of all time.", Label.CENTER);
      line3 = new Label("Who is " + name + "?", Label.CENTER);

      line0.setFont(font.deriveFont(Font.ITALIC));
      line1.setFont(font);
      line2.setFont(font);
      line3.setFont(font);
      line0.setForeground(Color.white);
      line1.setForeground(Color.white);
      line2.setForeground(Color.white);
      line3.setForeground(Color.white);
      line0.setBounds(0, (668 - 2 * DartBoard.getScreenHeightOffset()) / 6, 1024 - 2 * DartBoard.getScreenWidthOffset(), (668 - 2 * DartBoard.getScreenHeightOffset()) / 6);
      line1.setBounds(0, 2 * (668 - 2 * DartBoard.getScreenHeightOffset()) / 6, 1024 - 2 * DartBoard.getScreenWidthOffset(), (668 - 2 * DartBoard.getScreenHeightOffset()) / 6);
      line2.setBounds(0, 3 * (668 - 2 * DartBoard.getScreenHeightOffset()) / 6, 1024 - 2 * DartBoard.getScreenWidthOffset(), (668 - 2 * DartBoard.getScreenHeightOffset()) / 6);
      line3.setBounds(0, 4 * (668 - 2 * DartBoard.getScreenHeightOffset()) / 6, 1024 - 2 * DartBoard.getScreenWidthOffset(), (668 - 2 * DartBoard.getScreenHeightOffset()) / 6);
      add(line0);
      add(line1);
      add(line2);
      add(line3);
      setVisible(true);
      toFront();

   }

   public void run() {
      try {
         SoundManager.play("jeopardy");
         Thread.sleep(1000);
         boolean answerdelayed = true;
         boolean running = true;
         while (running) {
            Color line0Color = line0.getForeground();
            Color line1Color = line1.getForeground();
            Color line2Color = line2.getForeground();
            Color line3Color = line3.getForeground();

            if (line0Color.getRed() > 5) {
               line0.setForeground(new Color(line0Color.getRed() - 5, line0Color.getGreen(), line0Color.getBlue() - 5));
            } else if (line2Color.getRed() > 5) {
               line1.setForeground(new Color(line1Color.getRed() - 5, line1Color.getGreen() - 5, line1Color.getBlue()));
               line2.setForeground(new Color(line2Color.getRed() - 5, line2Color.getGreen() - 5, line2Color.getBlue()));
            } else if (line3Color.getGreen() > 5) {
               if (answerdelayed) {
                  answerdelayed = false;
                  Thread.sleep(3700);
               } else {
                  line3.setForeground(new Color(line3Color.getRed(), line3Color.getGreen() - 5, line3Color.getBlue() - 5));
               }
            } else {
               if (running) {
                  running = false;
                  Thread.sleep(3300);
               }
            }
            Thread.sleep(50);
         }

         stop();
      } catch (InterruptedException e) {
      }
      SoundManager.stop();
   }

   public void play() {
   }

   public void play(String name) {
      if (name == null) {
         return;
      }

      init(name);
      run();
   }

   public void stop() {
      this.dispose();
   }
}
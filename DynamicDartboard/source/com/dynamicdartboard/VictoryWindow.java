package com.dynamicdartboard;
import java.awt.*;

public class VictoryWindow extends Window implements Runnable, Animation {
   Label line0;
   Label line1;

   public VictoryWindow(Window parent) {
      super(parent);
   }

   public void init() {
      setSize(1024 - 2 * DartBoard.W_OFFSET, 668 - 2 * DartBoard.H_OFFSET);
      setLocation(DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      setLayout(null);
      line0 = new Label("YOU'RE", Label.CENTER);
      line1 = new Label("#1", Label.CENTER);
      line0.setFont(new Font("Garamond", Font.ITALIC, 100));
      line1.setFont(new Font("Garamond", Font.ITALIC, 175));
      line0.setForeground(Color.white);
      line1.setForeground(Color.white);
      line0.setBounds(0, (668 - 2 * DartBoard.H_OFFSET) / 4, 1024 - 2 * DartBoard.W_OFFSET, (668 - 2 * DartBoard.H_OFFSET) / 4);
      line1.setBounds(0, 2 * (668 - 2 * DartBoard.H_OFFSET) / 4, 1024 - 2 * DartBoard.W_OFFSET, (668 - 2 * DartBoard.H_OFFSET) / 4);
      add(line0);
      add(line1);
      setVisible(true);
      toFront();

   }

   public void run() {
      try {
         SoundManager.play("victory");
         Thread.sleep(1000);
         int i = 0;
         while (i < 102) {
            int red = (int)(Math.random() * 255);
            int green = (int)(Math.random() * 255);
            int blue = (int)(Math.random() * 255);
            Color color = new Color(red, green, blue);

            line0.setForeground(color);
            line1.setForeground(color);
            i++;
            Thread.sleep(100);
         }

         stop();
      } catch (InterruptedException e) {
      }
      SoundManager.stop();
   }

   public void play() {
      init();
      run();
   }

   public void play(String name) {
      play();
   }

   public void stop() {
      this.dispose();
   }
}

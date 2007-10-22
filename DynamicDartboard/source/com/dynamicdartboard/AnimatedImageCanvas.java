package com.dynamicdartboard;
import java.awt.*;

public class AnimatedImageCanvas extends Canvas {

   transient Image[] image;
   int index = 0;

   public AnimatedImageCanvas(String[] file, int w, int h) {
      image = new Image[file.length];
      for (int i = 0; i < file.length; i++) {
         image[i] = Toolkit.getDefaultToolkit().getImage(file[i]);
         image[i] = image[i].getScaledInstance(w, h, Image.SCALE_REPLICATE);
      }
      setSize(w, h);
   }

   public void paint(Graphics g) {
      if (g.drawImage(image[index], 0, 0, this)) {
         index++;
         index %= image.length;
//do not call repaint here. infi loop??
         //repaint(1000);
      }
   }

   public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
      repaint(2000);
      return true;
   }
}
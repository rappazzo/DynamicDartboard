package com.dynamicdartboard;
import java.awt.*;

public class ImageCanvas extends Canvas {

   transient Image image;

   public ImageCanvas(String file, int w, int h) {
      image = Toolkit.getDefaultToolkit().getImage(file);
      image = image.getScaledInstance(w, h, Image.SCALE_REPLICATE);
      setSize(w, h);
   }

   public void paint(Graphics g) {
      g.drawImage(image, 0, 0, this);
   }

   public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
      repaint();
      return true;
   }
}
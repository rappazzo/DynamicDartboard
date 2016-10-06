package com.dynamicdartboard;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class ImageCanvas extends Canvas {

   public static final transient long IMAGE_INTERVAL = 750;

   transient Image[] images;
   int index = 0;

   /**
    * Constructors
    */
   public ImageCanvas(String prefix, String[] imgNames, Dimension dimensions) {
      this(prefix, imgNames, (int)dimensions.getWidth(), (int)dimensions.getHeight());
   }
   public ImageCanvas(String prefix, String[] imgNames, int w, int h) {
      images = new Image[imgNames.length];
      for (int i = 0; i < imgNames.length; i++) {
    	 URL imgURL = this.getClass().getClassLoader().getResource(prefix + imgNames[i]);
         images[i] = Toolkit.getDefaultToolkit().getImage(imgURL);
         images[i] = images[i].getScaledInstance(w, h, Image.SCALE_REPLICATE);
      }
      setSize(w, h);
      if (images.length > 1) {
         new AnimationController().start();
      }
   }

   @Override
public void paint(Graphics g) {
      if (g.drawImage(images[index], 0, 0, this)) {
         index++;
         index %= images.length;
      }
   }

   @Override
public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
      repaint(images.length == 1 ? 1 : 2000);
      return true;
   }

   class AnimationController extends Thread {
      @Override
      public void run() {
         while (true) {
            try {
               sleep(IMAGE_INTERVAL);
               repaint();
            } catch (Throwable e) {
               //ignore
            }
         }
      }
   }

}
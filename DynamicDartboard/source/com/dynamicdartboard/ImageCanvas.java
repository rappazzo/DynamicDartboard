package com.dynamicdartboard;
import java.awt.*;

public class ImageCanvas extends Canvas {

   public static final transient long IMAGE_INTERVAL = 750;
   
   transient Image[] images;
   int index = 0;

   /**
    * Constructors
    */
   public ImageCanvas(String filePrefix, String[] file, Dimension dimensions) {
      this(filePrefix, file, (int)dimensions.getWidth(), (int)dimensions.getHeight());
   }
   public ImageCanvas(String filePrefix, String[] file, int w, int h) {
      images = new Image[file.length];
      for (int i = 0; i < file.length; i++) {
         images[i] = Toolkit.getDefaultToolkit().getImage(filePrefix + file[i]);
         images[i] = images[i].getScaledInstance(w, h, Image.SCALE_REPLICATE);
      }
      setSize(w, h);
      if (images.length > 1) {
         new AnimationController().start();
      }
   }

   public void paint(Graphics g) {
      if (g.drawImage(images[index], 0, 0, this)) {
         index++;
         index %= images.length;
      }
   }

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
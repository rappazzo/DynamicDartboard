package com.dynamicdartboard;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BounceWindow extends Window implements Animation {
   CustomLabel labels[];

   public BounceWindow(Window parent) {
      super(parent);
   }

   public BounceWindow(Window parent, String text) {
      super(parent);
      init(text);
   }

   public void init(String name) {
      int maxWidth = DartBoard.getInstance().getInstance().getScreenWidth() - 2 * DartBoard.getInstance().getInstance().getScreenWidthOffset();
      int maxHeight = DartBoard.getInstance().getInstance().getScreenHeight() - 2 * DartBoard.getInstance().getInstance().getScreenHeightOffset();
      setSize(maxWidth, maxHeight);
      setLocation(DartBoard.getInstance().getInstance().getScreenWidthOffset(), DartBoard.getInstance().getInstance().getScreenHeightOffset());
      setLayout(null);
      Font font = new Font("Garamond", Font.BOLD, 50);
      labels = new CustomLabel[name.length()];

      CustomLabel label = CustomLabel.getDefaultLabel(font);
      FontMetrics fm = label.getFontMetrics(font);
      List<StringBuilder> lines = new ArrayList<StringBuilder>();
      StringBuilder buf = new StringBuilder("");
      lines.add(buf);
      int mainTotal = 0;
      for (StringTokenizer tk = new StringTokenizer(name, " "); tk.hasMoreTokens();) {
         String cString = tk.nextToken();
         int total = fm.stringWidth(cString) + cString.length() * 8;
         mainTotal += total + fm.charWidth(' ');
         if ((mainTotal) >= maxWidth) {
            buf = new StringBuilder("");
            lines.add(buf);
            mainTotal = total;
         }
         if (tk.hasMoreTokens()) {
            buf.append(cString + " ");
         } else {
            buf.append(cString);
         }
      }
      int divider = lines.size() + 2;
      int lc = 0;
      Color color = new Color(255, 255, 255, 0);
      for (int j = 0; j < lines.size(); j++) {
         buf = (StringBuilder)lines.get(j);
         int width = 0;
         int ladded = lc;
         char chars[] = buf.toString().toCharArray();
         for (int cc = 0; cc < chars.length; cc++) {
            labels[lc] = new CustomLabel(chars[cc] + "", width, (j + 1) * maxHeight / divider);
            labels[lc].setFont(font);
            labels[lc].setForeground(Color.blue);
            labels[lc].setBackground(color);
            labels[lc].setSize(fm.charWidth(chars[cc]) + 3, fm.getHeight());
            lc++;
            width += fm.charWidth(chars[cc]) + 8;
         }
         width = (maxWidth - width) / 2;
         for (int k = ladded; k < lc; k++) {
            labels[k].reCenter(width);
         }
      }

      for (int c = 0; c < labels.length; c++) {
         if (labels[c] != null) {
            int rx = (int)(Math.random() * (maxWidth - 150)) + 75;
            int ry = (int)(Math.random() * (maxHeight - 150)) + 75;
            labels[c].setLocation(rx, ry);
            add(labels[c]);
         } else {
            System.out.println("label #" + c + " is null");
         }
      }
   }

   public void run() {
      try {
         while (!isAllFinished()) {
            for (int i = 0; i < labels.length; i++) {
               labels[i].scroll(15);
               //               System.out.println( labels[i].getText() + " "+labels[i].getX() + " "+ labels[i].getY());
            }
            //            invalidate();
            Thread.sleep(100);
         }
         Thread.sleep(2000);
      } catch (InterruptedException e) {
      }
      stop();
   }

   public boolean isAllFinished() {
      boolean rv = true;
      for (int i = 0; i < labels.length; i++) {
         if (!labels[i].finished()) {
            rv = false;
         }
      }
      return rv;
   }

   public void play() {
      setVisible(true);
      toFront();
      boolean delay = true;
      while (delay) {
         if (delay) {
            delay = false;
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
         }
      }
      run();
   }

   public void play(String text) {
      play();
   }

   public void stop() {
      this.dispose();
   }
}

class CustomLabel extends Label {
   int finalX, finalY;
   private static CustomLabel thisLabel;

   public CustomLabel(String text, int x, int y) {
      super(text, Label.CENTER);
      finalX = x;
      finalY = y;
      setLocation(x, y);
   }

   private CustomLabel() {
      super();
   }

   public void init(int x, int y, int width, int height) {
      setBounds(x, y, width, height);
   }

   public static CustomLabel getDefaultLabel(Font font) {
      if (thisLabel == null) {
         thisLabel = new CustomLabel();
      }
      thisLabel.setFont(font);
      return thisLabel;
   }

   public boolean finished() {
      boolean rv = true;
      if (getX() != finalX) {
         rv = false;
      }
      if (getY() != finalY) {
         rv = false;
      }
      return rv;
   }

   public void scroll(int speed) {
      int cx = getX();
      int cy = getY();
      int deltaX = 0;
      int deltaY = 0;
      int newX = cx, newY = cy;

      double m = getSlope(finalX, finalY, cx, cy);
      if ((Math.abs(finalX - cx) + Math.abs(finalY - cy)) <= speed) {
         newX = finalX;
         newY = finalY;
      } else {
         if (cx > finalX) {
            deltaX = -1;
         } else {
            deltaX = 1;
         }
         if (cy > finalY) {
            deltaY = -1;
         } else {
            deltaY = 1;
         }
         for (int i = 0; i < speed; i++) {
            if ((double)Math.abs(m) > 1.0) {
               newY += deltaY;
               deltaX = getOther(m, newY, finalY, finalX);
               i += Math.abs(newX - deltaX);
               newX = deltaX;
            } else {
               newX += deltaX;
               deltaY = getOther(m, newX, finalX, finalY);
               i += Math.abs(newY - deltaY);
               newY = deltaY;
            }
         }
      }
      //System.out.println("Fx:"+finalX+" Fy:"+finalY+" Nx:"+newX +" Ny:"+newY);         
      setLocation(newX, newY);
   }

   public int getOther(double m, int a1, int a0, int b0) {
      int b1 = (int)Math.round(m * (a1 - a0) + b0);
      return b1;
   }

   protected double getSlope(int x, int y, int x2, int y2) {
      return ((double)(y2 - y)) / (x2 - x);
   }

   public void reCenter(int x) {
      finalX += x;
      setLocation(finalX, finalY);
   }

}

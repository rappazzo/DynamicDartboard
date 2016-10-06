package com.dynamicdartboard;
import java.awt.*;
import java.util.*;
import java.util.List;

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

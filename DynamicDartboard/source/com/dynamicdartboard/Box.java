package com.dynamicdartboard;
import java.awt.*;

import java.io.*;

/*
 * represents a single 'box' on the board
 */

public class Box implements Serializable {
   int column;
   int row;

   public static final short NORTHWEST = 0;
   public static final short NORTHEAST = 1;
   public static final short EAST = 2;
   public static final short SOUTHEAST = 3;
   public static final short SOUTHWEST = 4;
   public static final short WEST = 5;

   private static int width = 50;
   private static int height = 50;

   public Box(int c, int r) {
      column = c;
      row = r;
   }

   public int getColumn() {
      return column;
   }

   public int getRow() {
      return row;
   }

   public void setColumn(int column) {
      this.column = column;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getX() {
      return (row%2)*(getWidth()/2) + (column * getWidth());
   }

   public int getY() {
      return 3*(row * getHeight())/4 ;
   }

   public static int getWidth() {
      return width;
   }

   public static int getHeight() {
      return height;
   }

   public static void setWidth(int w) {
      if (w < 20) {
         w = 20;
      }
      width = w;
   }

   public static void setHeight(int h) {
      if (h < 20) {
         h = 20;
      }
      height = h;
   }

   public void drawBox(Graphics g, Color bg) {
      Color color = g.getColor();
      g.setColor(bg);
      g.fillPolygon(getxPoints(), getyPoints(), 6) ;
      g.setColor(color);
   }

   public int[] getxPoints() {
      int[] x = new int[6];
      x[0] = getX();
      x[1] = getX() + getWidth()/2;
      x[2] = getX() + getWidth();
      x[3] = getX() + getWidth();
      x[4] = getX() + getWidth()/2;
      x[5] = getX();
      return x;
   }

   public int[] getyPoints() {
     int[] y = new int[6];
      y[0] = getY() + getHeight()/4;
      y[1] = getY();
      y[2] = getY() + getHeight()/4;
      y[3] = getY() + 3*getHeight()/4;
      y[4] = getY() + getHeight();
      y[5] = getY() + 3*getHeight()/4;
      return y;
   }

   

   public void drawBorder(Graphics g, boolean[] side, Color bg) {
      if (side[NORTHWEST]) {
         g.setColor(Color.black);
      } else {
         g.setColor(bg);
      }
      g.drawLine(getX(), getY()+getHeight()/4, getX() + getWidth()/2, getY());
      if (side[NORTHEAST]) {
         g.setColor(Color.black);
      } else {
         g.setColor(bg);
      }
      g.drawLine( getX() + getWidth()/2, getY(), getX() + getWidth(), getY()+ getHeight()/4);
      
      if (side[EAST]) {
         g.setColor(Color.black);
      } else {
         g.setColor(bg);
      }
         g.drawLine(getX() + getWidth(), getY()+ getHeight()/4, getX() + getWidth(), getY() + 3*getHeight()/4);
      if (side[SOUTHEAST]) {
         g.setColor(Color.black);
      } else {
         g.setColor(bg);
      }
         g.drawLine( getX() + getWidth(), getY() + 3*getHeight()/4, getX() + getWidth()/2, getY() + getHeight());
      if (side[SOUTHWEST]) {
         g.setColor(Color.black);
      } else {
         g.setColor(bg);
      }
         g.drawLine(getX() + getWidth()/2, getY() + getHeight(), getX(), getY() + 3*getHeight()/4);
      if (side[WEST]) {
         g.setColor(Color.black);
      } else {
         g.setColor(bg);
      }
      g.drawLine( getX(), getY() + 3*getHeight()/4, getX(), getY()+getHeight()/4);
      
   }

   public void drawFace(Graphics g, String face, Color color) {
      g.setColor(color);
      FontMetrics fm = g.getFontMetrics();
      int sh = fm.getHeight();
      int sw = fm.stringWidth(face);
      int x = getX() + ((getWidth() - sw) / 2);
      int y = getY() + ((getHeight() + sh) / 2);
      g.drawString(face, x, y);
   }

   public boolean equals(Object o) {
      try {
         Box b = (Box)o;
         if (b.getColumn() == getColumn() && b.getRow() == getRow()) {
            return true;
         } else {
            return false;
         }
      } catch (ClassCastException e) {
         return false;
      }
   }
}
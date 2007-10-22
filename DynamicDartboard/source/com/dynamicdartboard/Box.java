package com.dynamicdartboard;
import java.awt.*;

import java.io.*;

/*
 * represents a single 'box' on the board
 */

public class Box implements Serializable {
   int column;
   int row;

   public static final short NORTH = 0;
   public static final short WEST = 1;
   public static final short SOUTH = 2;
   public static final short EAST = 3;

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
      return (column * getWidth());
   }

   public int getY() {
      return (row * getHeight());
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
      g.fillRect(getX(), getY(), getWidth(), getHeight());
      g.setColor(color);
   }

   public void drawBorder(Graphics g, boolean[] side) {
      g.setColor(Color.black);
      if (side[NORTH]) {
         g.drawLine(getX(), getY(), getX() + getWidth(), getY());
      }
      if (side[WEST]) {
         g.drawLine(getX(), getY(), getX(), getY() + getHeight());
      }
      if (side[SOUTH]) {
         g.drawLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight());
      }
      if (side[EAST]) {
         g.drawLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight());
      }
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
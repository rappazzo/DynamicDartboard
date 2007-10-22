package com.dynamicdartboard;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.io.*;

/*
 * Represents a number on the board
**/
public class BoardNumber implements Serializable {
   private Integer number;
   private List<Box> box;
   private Color color;
   private boolean merged = false;
   private transient Animation animation = null;

   public BoardNumber () {
   }

   public BoardNumber (Integer number) {
      this.number = number;
   }

   public BoardNumber (Integer number, Animation animation) {
      this.number = number;
      this.animation = animation;
   }

   public Integer getNumber() {
      return number;
   }
   
   public void setNumber(Integer number) {
      this.number = number;
   }
   
   public void setAnimation(Animation a) {
      animation = a;
   }
   
   public Animation getAnimation() {
      return animation;
   }
   
   public List<Box> getBoxes() {
      return box;
   }
   
   public void addBox(Box b) {
      if(box == null) {
         box = new ArrayList<Box>();
      }
      box.add(b);
   }
   
   public void setMerged(boolean flag) {
      this.merged = flag;
   }
   
   public boolean isMerged() {
      return merged;
   }
   
   public void setBoxes(List<Box> list) {
      box = list;
   }
   
   public Color getColor() {
      if(color == null) {
         color = Color.orange;
      }
      return color;
   }
   
   public void setColor( Color color) {
      this.color = color;
   }
   
   public void drawNumber(Graphics g) {
      List boxes = getBoxes();
      if(boxes == null) {
         return;
      }
      int rr, gg, bb;
      rr = getColor().getRed();
      gg = getColor().getGreen();
      bb = getColor().getBlue();
      rr = 255 - rr;
      gg = 255 - gg;
      bb = 255 - bb;
      Color faceColor = new Color(rr, gg, bb);
      for(Iterator i = boxes.iterator(); i.hasNext(); ){
         Box box = (Box)i.next();
         box.drawBox(g, getColor());
         box.drawBorder(g, getBorders(box));
      }
      ((Box)boxes.get(0)).drawFace(g, getNumber().toString(), faceColor);
   }
   
   public boolean[] getBorders(Box box) {
      boolean rv[] = new boolean[4];
      Box item = new Box (box.getColumn(), box.getRow());
      item.setRow(box.getRow() - 1);
      if(!ownsBox(item)) { 
         rv[Box.NORTH] = true;
      } else {
         rv[Box.NORTH] = false;
      }
      item.setColumn(box.getColumn() - 1);
      item.setRow(box.getRow());
      if(!ownsBox(item)) { 
         rv[Box.WEST] = true;
      } else {
         rv[Box.WEST] = false;
      }
      item.setColumn(box.getColumn());
      item.setRow(box.getRow() + 1);
      if(!ownsBox(item)) { 
         rv[Box.SOUTH] = true;
      } else {
         rv[Box.SOUTH] = false;
      }
      item.setColumn(box.getColumn() + 1);
      item.setRow(box.getRow());
      if(!ownsBox(item)) { 
         rv[Box.EAST] = true;
      } else {
         rv[Box.EAST] = false;
      }
      return rv;
   }
   
   public boolean ownsBox(Box b) {
      if(box == null) {
         return false;
      }
      Iterator i = box.iterator();
      while(i.hasNext()) {
         if(b.equals(i.next())) {
            return true;
         }
      }
      return false;
   }
   
   public int compareTo(BoardNumber bn) {
      if(bn == null) {
         return 1;
      }
      return getNumber().intValue() - bn.getNumber().intValue();
   }
 
}
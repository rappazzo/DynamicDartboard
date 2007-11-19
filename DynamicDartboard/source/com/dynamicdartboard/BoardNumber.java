package com.dynamicdartboard;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.io.*;
import com.dynamicdartboard.actionhandler.*;

/*
 * Represents a number on the board
**/
public class BoardNumber implements Serializable {
   private Integer number;
   private String displayOverride = null;
   private List<Box> box;
   private Color color;
   private boolean merged = false;
   private transient ActionHandler actionHandler = new ActionHandler() {
      public void handle(BoardNumber hit) {
         DartBoard db = DartBoard.getInstance();
         db.getCommandWindow().assignNumber(hit.getNumber().intValue());
         db.getBoard().removeNumber(hit.getNumber());
      }
   };

   public BoardNumber () {
   }

   public BoardNumber (Integer number) {
      this.number = number;
   }

   public Integer getNumber() {
      return number;
   }
   
   public void setNumber(Integer number) {
      this.number = number;
   }
   
   public void setDisplayOverride(String displayOverride) {
      this.displayOverride = displayOverride;
   }
   
   public String getDisplay() {
      return (this.displayOverride != null ? displayOverride : String.valueOf(getNumber()));
   }
   
   public void setActionHandler(ActionHandler actionHandler) {
      if (actionHandler != null) {
         this.actionHandler = actionHandler;
      }
   }
   
   public ActionHandler getActionHandler() {
      return actionHandler;
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
         color = Color.ORANGE;
      }
      return color;
   }
   
   public void setColor( Color color) {
      this.color = color;
   }
   
   public void drawNumber(Graphics g) {
      List<Box> boxes = getBoxes();
      if(boxes == null) {
         return;
      }
      Color faceColor = ColorSequence.blackOrWhiteNegativeOf(getColor());
      for(Iterator i = boxes.iterator(); i.hasNext(); ){
         Box box = (Box)i.next();
         box.drawBox(g, getColor());
         box.drawBorder(g, getBorders(box));
      }
      boxes.get(0).drawFace(g, getDisplay(), faceColor);
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
   
   class Shimmer extends Thread {
      private static final long SHIMMER_INTERVAL = 100;

      @Override
      public void run() {
         while (true) {
            try {
               sleep(SHIMMER_INTERVAL);
               //redraw the font with the new shimmer color
            } catch (Throwable e) {
               //ignore
            }
         }
      }
   }
 
}
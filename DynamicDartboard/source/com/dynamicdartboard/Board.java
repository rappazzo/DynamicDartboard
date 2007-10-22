package com.dynamicdartboard;
import java.util.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Window;
import java.io.*;

/*
 * represents the board
**/

public class Board implements Serializable{
   private Map<Integer, BoardNumber> numberMap;
   public int COLUMNS = 11;
   public int ROWS = 11;
   private Window owner = null;
   
   public Board() {
      init(COLUMNS, ROWS);
   }
   
   public Board(int columns, int rows, Window owner) {
      COLUMNS = columns;
      ROWS = rows;
      this.owner = owner;
      init(columns, rows);
   }
   
   public void init(int columns, int rows) {
      generateNumbers(columns * rows);
      List<Box> boxes = generateBoxes(columns, rows);
      populateNumberBoxes(boxes);
   }
   
   public int getColumns() {
      return COLUMNS;
   }
   
   public void setColumns(int c) {
      COLUMNS = c;
   }
   
   public int getRows() {
      return ROWS;
   }
   
   public void setRows(int r) {
      ROWS = r;
   }
   
   public double getPercent(Integer num) {
      int total = COLUMNS * ROWS;
      double val = ((double)num.intValue())/total * 100.0;
      return val;
   }
      
   protected void generateNumbers(int size) {
      numberMap = new HashMap<Integer, BoardNumber>(size);
      int r = 255, g = 204, b = 255;
      
      for(int i = 1; i <= size; i++) {
         BoardNumber number = new BoardNumber(new Integer(i));
         if(i == 1) {
            number.setAnimation(new VictoryWindow(owner));
         } 
         if(i == 99) {
            number.setAnimation(new BounceWindow(owner, "YOU SUCK BIG TIME"));
         } 

         if(b <= 0) {
            b = 255;
            g -= 51;
         } else {
            b -= 51;
         }
         if(g < 0) {
            g = 255;
            r -= 51;
         }
         if(r < 0) {
            r = 255;
         }
         
         number.setColor(new Color(r, g, b));
         numberMap.put(number.getNumber(), number);
      }
   }
   
   protected List<Box> generateBoxes(int column, int row) {
      List<Box> boxes = new ArrayList<Box>(column * row);
      for(int y = 0; y < row; y++) {
         for( int x = 0; x < column; x++) {
            boxes.add(new Box(x, y));
         }
      }
      return boxes;
   }
   
   protected void populateNumberBoxes(List<Box> boxes) {
      int pick;
      int size = boxes.size();
      for(int i = 1; i <= size; i++) {
         pick = (int)(Math.random() * boxes.size());
         if(pick >= boxes.size()) {
            pick = boxes.size() - 1;
         }
         numberMap.get(Integer.valueOf(i)).addBox((Box)boxes.get(pick));
         boxes.remove(pick);
      }
   }
   
   public void drawBoard(Graphics g) {
      for(BoardNumber boardNumber : numberMap.values()) {
         boardNumber.drawNumber(g);
      }
   }
   
   public boolean removeNumber( Integer number ) {
      System.out.println("trying to remove::"+number);
      BoardNumber num = (BoardNumber)numberMap.get(number);
      if(num == null) {
         System.out.println("number not in board");
         return false;
      }
      if(numberMap.values().size() == 1) {
         System.out.println("last Number.. not removing");
         return false ;
      }
      numberMap.remove(number);
      distributeBoxes(num.getBoxes());
      clearMergedFlag();
      return true;
   }

   public void replaceNumber(Integer source, Integer replace) {
      BoardNumber bn = (BoardNumber)numberMap.remove(source);
      if(bn != null) {
         bn.setNumber(replace);
         if(numberMap.get(replace) != null) {
            removeNumber(replace);
         }
         numberMap.put(replace, bn);
      }
   }
   
   public BoardNumber removeBox( int x, int y ) {
      Box box = new Box(x, y);
      BoardNumber hit = null;
      for(Iterator i = numberMap.values().iterator(); i.hasNext(); ){
         BoardNumber item = (BoardNumber)i.next();
         if(item.ownsBox(box) ) {
            hit = item;
            break;
         }
      }
      if(hit != null) {
         if(removeNumber(hit.getNumber())) {
            return hit;
         }
      }
      return null;
   }
   
   public void clearMergedFlag() {
      for(Iterator i = numberMap.values().iterator(); i.hasNext(); ) {
         BoardNumber item = (BoardNumber)i.next();
         item.setMerged(false);
      }
   }
      
   public void distributeBoxes(List boxes) {
      if(boxes == null) {
         return;
      }
      List<Box> temp = new ArrayList<Box>();
      while(boxes.size() > 0) {
         for(Iterator i = boxes.iterator(); i.hasNext(); ) {
            Box box = (Box)i.next();
            BoardNumber bestMatch = getBestMatch(box);
            if(bestMatch != null) {
               bestMatch.addBox(box);
               bestMatch.setMerged(true);
            } else {
               temp.add(box);
            }
         }
         boxes = temp;
         temp = new ArrayList<Box>();
      }
   }
   
   public BoardNumber getBestMatch(Box box) {
      int c = box.getColumn();
      int r = box.getRow();
      BoardNumber best = null;
      BoardNumber north = getBoxOwner(new Box(c , r - 1));
      BoardNumber west  = getBoxOwner(new Box(c - 1, r));
      BoardNumber south = getBoxOwner(new Box(c , r + 1));
      BoardNumber east  = getBoxOwner(new Box(c + 1, r));
      if(north != null) {
         best = north;
      } else if(west != null) {
         best = west;
      } else if(south != null) {
         best = south;
      } else if(east != null) {
         best = east;
      }
      if(best == null) {
         return null;
      }
      best = compareBest(best, north);
      best = compareBest(best, west);
      best = compareBest(best, south);
      best = compareBest(best, east);
      return best;
   }

   public BoardNumber compareBest(BoardNumber source, BoardNumber compareTo) {
      if(compareTo == null) {
         return source;
      }
      if(source.isMerged() && !compareTo.isMerged()) {
         return compareTo;
      }
      if(!source.isMerged() && compareTo.isMerged()) {
         return source;
      }
      if(source.compareTo(compareTo) < 0 ) {
         return compareTo;
      } else {
         return source;
      }
   }

      
   public BoardNumber getBoxOwner(Box box) {
      for(Iterator i = numberMap.values().iterator(); i.hasNext(); ) {
         BoardNumber item = (BoardNumber)i.next();
         if(item.ownsBox(box)) {
            return item;
         }
      }
      return null;
   }
}
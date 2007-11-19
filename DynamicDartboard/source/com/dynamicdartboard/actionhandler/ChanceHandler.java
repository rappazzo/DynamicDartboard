package com.dynamicdartboard.actionhandler;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import com.dynamicdartboard.*;

/**
 * @author mrappazz
 *
 *
 **/
public class ChanceHandler implements ActionHandler {
   
   public static final String CHANCE_WINDOW = "ChanceWindow";

   /**
    * 
    */
   public ChanceHandler() {
   }

   /* (non-Javadoc)
    * @see com.dynamicdartboard.actionhandler.ActionHandler#handle(java.lang.Object[])
    */
   public void handle(BoardNumber hit) {
      ChanceWindow chanceWindow = new ChanceWindow(hit);

   }
   
   private static class ChanceWindow extends Window implements MouseListener {
      
      //the bullseye should be made up of 5? rings of increasing breadth
      private static final int NUMBER_OF_RINGS = 5;
      private int[][] bullseyePoints = new int[NUMBER_OF_RINGS][2];
      private int[] bullseyeRadii = new int[NUMBER_OF_RINGS];
      private Color[] colors = new Color[NUMBER_OF_RINGS];
      private int[] center = null;
      private List<Integer> values = new ArrayList();
      private static final int X = 0;
      private static final int Y = 1;
      
      public ChanceWindow(BoardNumber hit) {
         super(DartBoard.getInstance());
         DartBoard db = DartBoard.getInstance();
         setName(CHANCE_WINDOW);
         setSize(db.getScreenWidth() - 2 * db.getScreenWidthOffset(), db.getScreenHeight() - 2 * db.getScreenHeightOffset() - (int)db.getCommandWindow().getSize().getHeight());
         setLocation(db.getScreenWidthOffset(), db.getScreenHeightOffset());
         setLayout(null);
         center = new int[]{
            (int)getSize().getWidth() / 2, 
            (int)getSize().getHeight() / 2
         };
         setupBullseye(hit.getNumber());
         setVisible(true);
         toFront();
         addMouseListener(this);
      }
      

      private void setupBullseye(int originalValue) {
         DartBoard db = DartBoard.getInstance();
         Board board = db.getBoard();
         ColorSequence colorSeq = new ColorSequence().setDecrement(150);
         Random rnd = new Random();
         List<Integer> numberPool = new ArrayList(board.getAvaliableNumbers());
         numberPool.remove(Integer.valueOf(originalValue));
         Collections.shuffle(numberPool);
         int radiusBase = (int)Math.min(getSize().getWidth(), getSize().getHeight()) * NUMBER_OF_RINGS / 100;
         for (int i = 0; i < NUMBER_OF_RINGS; i++) {
            bullseyeRadii[i] = radiusBase * 2 * (i + 1);
            bullseyePoints[i][X] = (int)(center[X] - bullseyeRadii[i]);
            bullseyePoints[i][Y] = (int)(center[Y] - bullseyeRadii[i]);
            colors[i] = colorSeq.next();
            //add a value from the pool of available board numbers 
            values.add(numberPool.get(i));
         }
         //replace the first element with the original value (replace to keep the size consistent)
         values.set(0, Integer.valueOf(originalValue));
         Collections.sort(values);
      }


      public void paint(Graphics g) {
         g.setColor(Color.BLACK);
         g.drawRect(0, 0, (int)getSize().getWidth() - 1, (int)getSize().getHeight() - 1);
         //draw from the outside in
         g.setFont(new Font("Arial", Font.BOLD, 18));
         int radiusBase = (int)Math.min(getSize().getWidth(), getSize().getHeight()) * NUMBER_OF_RINGS / 100;
         for (int i = NUMBER_OF_RINGS - 1; i >= 0; i--) {
            //draw the circle
            g.setColor(colors[i]);
            g.fillOval(bullseyePoints[i][X], bullseyePoints[i][Y], bullseyeRadii[i] * 2, bullseyeRadii[i] * 2);
            g.setColor(Color.BLACK);
            g.drawOval(bullseyePoints[i][X], bullseyePoints[i][Y], bullseyeRadii[i] * 2, bullseyeRadii[i] * 2);
            g.setColor(ColorSequence.blackOrWhiteNegativeOf(colors[i]));
            //9 = 1/2 of the font size
            g.drawString(String.valueOf(values.get(i)), bullseyePoints[i][X] + radiusBase, center[Y] + 9); 
         }
      }
      
      public void mouseReleased(MouseEvent mEvt) {
         if (CHANCE_WINDOW.equals(mEvt.getComponent().getName())) {
            int x = mEvt.getX();
            int y = mEvt.getY();
            if (x <= 0 || y <= 0) {
               return;
            }
            //calculate the distance from the center
            //d = sqrt((x2 - x1)^2 + (y2 - y1)^2).
            double distance = Math.sqrt(
               (x - center[X])*(x - center[X]) +
               (y - center[Y])*(y - center[Y])
            );
            //check from the outside in
            for (int i = 0; i < NUMBER_OF_RINGS; i++) {
               if (distance < bullseyeRadii[i]) {
                  //set the value, and close the window
                  DartBoard db = DartBoard.getInstance();
                  Board board = db.getBoard();
                  Integer value = values.get(i);
                  board.removeNumber(value);
                  db.getCommandWindow().assignNumber(value.intValue());
                  db.save(value.intValue());
                  this.setVisible(false);
                  this.toBack();
                  this.dispose();
                  break;
               }
            }
         }
      }

      //Unused mouse events which are implemented as part of the MouseListener Interface
      public void mousePressed(MouseEvent mEvt) {}
      public void mouseEntered(MouseEvent mEvt) {}
      public void mouseExited(MouseEvent mEvt) {}
      public void mouseClicked(MouseEvent mEvt) {}
      
   }

}

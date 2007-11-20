/***
 **  @(#) TradeCard.com 1.0
 **
 **  Copyright (c) 2007 TradeCard, Inc. All Rights Reserved.
 **
 **
 **  THIS COMPUTER SOFTWARE IS THE PROPERTY OF TradeCard, Inc.
 **
 **  Permission is granted to use this software as specified by the TradeCard
 **  COMMERCIAL LICENSE AGREEMENT.  You may use this software only for
 **  commercial purposes, as specified in the details of the license.
 **  TRADECARD SHALL NOT BE LIABLE FOR ANY  DAMAGES SUFFERED BY
 **  THE LICENSEE AS A RESULT OF USING OR MODIFYING THIS SOFTWARE IN ANY WAY.
 **
 **  YOU MAY NOT DISTRIBUTE ANY SOURCE CODE OR OBJECT CODE FROM THE TradeCard.com
 **  TOOLKIT AT ANY TIME. VIOLATORS WILL BE PROSECUTED TO THE FULLEST EXTENT
 **  OF UNITED STATES LAW.
 **
 **  @version 1.0
 **  @author Copyright (c) 2007 TradeCard, Inc. All Rights Reserved.
 **
 **/
package com.dynamicdartboard;

import java.awt.*;
import java.util.*;

/**
 * @author mrappazz
 *
 *
 **/
public class ColorSequence implements Enumeration<Color>, Iterator<Color> {
   
   private int red = 255;
   private int green = 204;
   private int blue = 255;
   private int decrement = 51;
   Random rnd;
   
   protected Color lastColor = null;

   /**
    * 
    */
   public ColorSequence() {
      rnd = new Random();
      randomize();
   }
   
   public ColorSequence setDecrement(int decrement) {
      if (decrement > 0) {
         this.decrement = decrement;
      }
      return this;
   }
   
   public int getDecrement() {
      return this.decrement;
   }
   
   public void randomize() {
      red = rnd.nextInt(255);
      green = rnd.nextInt(255);
      blue = rnd.nextInt(255);
   }

   public boolean hasMoreElements() {
      return hasNext();
   }
   
   public boolean hasNext() {
      return true;
   }
   
   public Color nextElement() {
      return next();
   }

   public Color next() {
      //when we are reseting a color, that color is less than zero.  
      //Adding the remainder (ie, "+ (blue % 255)") will effectively be a sutraction 
      if (blue >= 0) {
         blue -= getDecrement();
      }
      if (blue < 0) {
         blue = 255 + (blue % 255);
         green -= getDecrement();
      }
      if(green < 0) {
         green = 255 + (green % 255);
         red -= getDecrement();
      }
      if(red < 0) {
         red = 255 + (red % 255);
      }
      lastColor = new Color(red, green, blue);
      return lastColor;
   }
   
   public Color getNegativeOfLastColor() {
      return negativeOf(lastColor);
   }
   
   public static Color negativeOf(Color color) {
      Color bwNegative = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
      return bwNegative;
   }
   
   public Color getBlackOrWhiteNegativeOfLastColor() {
      return blackOrWhiteNegativeOf(lastColor);
   }
   
   public static Color blackOrWhiteNegativeOf(Color color) {
      int colorSum = color.getRed() + color.getGreen() + color.getBlue();
      return colorSum <= 500 ? Color.WHITE : Color.BLACK;
   }

   public void remove() {
      throw new UnsupportedOperationException("Remove does not make sense for a color sequence");
   }

}

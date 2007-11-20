package com.dynamicdartboard;

import java.awt.*;
import java.util.*;

/**
 * @author mrappazz
 *
 *
 **/
public class GreyscaleColorSequence extends ColorSequence {

   private int grey = 0;
   private int direction = 1;
   Random rnd;

   /**
    * 
    */
   public GreyscaleColorSequence() {
      //no need to call super, it just randomizes the color
      setDecrement(20);
   }
   
   public ColorSequence setToWhite() {
      grey = Color.WHITE.getRed();
      return this;
   }
   
   public ColorSequence setToBlack() {
      grey = Color.BLACK.getRed();
      return this;
   }
   
   public ColorSequence setDirection(int direction) {
      this.direction = (direction >= 0 ? 1 : -1);
      return this;
   }
   
   public Color next() {
      grey = grey + direction * getDecrement();
      if (grey > 255) {
         grey = grey % 255;
      }
      if (grey < 0) {
         grey = 255 + (grey % 255);
      }
      lastColor = new Color(grey, grey, grey);
      return lastColor;
   }

}

package com.dynamicdartboard;
import java.applet.*;

public class AudioPlayer implements Runnable {
   AudioClip clip;
   
   public void init() {
   }
   
   public void start() {
   }
   
   public void run() {
      if(clip != null) {
          clip.play();
      }
   }
   
   public synchronized void play(AudioClip c) {
      clip = c;
      new Thread(this).start();
   }
   
   public void stop() {
      if(clip != null) {
         clip.stop();
      }
   }
   
}
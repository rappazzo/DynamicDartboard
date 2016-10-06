package com.dynamicdartboard;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class SoundManager {
   static Map<String, AudioClip> sounds = new HashMap<>();
   static Map<String, String> soundFiles = new HashMap<>();
   private static AudioPlayer player = new AudioPlayer();
   static List<String> goodSounds = new ArrayList<>();
   static List<String> badSounds = new ArrayList<>();
   static List<String> neutralSounds = new ArrayList<>();
   static List<String> specialSounds = new ArrayList<>();

   public static void playRandom(String type) {
      List<String> list = null;
      if (type.equals("good")) {
         list = goodSounds;
      } else if (type.equals("bad")) {
         list = badSounds;
      } else if (type.equals("neutral")) {
         list = neutralSounds;
      } else if (type.equals("special")) {
         list = specialSounds;
      }
      if (list != null) {
         int pick = (int)(Math.random() * list.size());
         String hit = (String)list.get(pick);
         play(hit);
      }
   }

   public static void play(String name) {
      AudioClip clip = sounds.get(name);
      if (clip == null) {
         String file = soundFiles.get(name);
         if (file == null) {
            return;
         }
         URL url = SoundManager.class.getClass().getClassLoader().getResource("sounds/"+file);
         clip = Applet.newAudioClip(url);
         sounds.put(name, clip);
      }
      if (clip != null) {
         player.play(clip);
      }
   }

   public static void stop() {
      player.stop();
   }

   public static void loadSounds() {
      for (String name : soundFiles.keySet()) {
         String file = soundFiles.get(name);
         if (file == null) {
            continue;
         }
         URL url = SoundManager.class.getClass().getClassLoader().getResource("sounds/"+file);
         AudioClip clip = Applet.newAudioClip(url);
         sounds.put(name, clip);
      }
   }

   public static void playAll() {
      for (Iterator i = sounds.keySet().iterator(); i.hasNext();) {
         String name = (String)i.next();
         System.out.println(name);
         ((AudioClip)sounds.get(name)).play();
         try {
            Thread.sleep(2000);
         } catch (Exception e) {
         }
      }
   }

   public static void readSoundList() {
      BufferedReader fin = null;
      List<String> currentList = null;
      try {
         fin = new BufferedReader(new InputStreamReader(SoundManager.class.getClass().getClassLoader().getResourceAsStream("sounds/sounds.txt")));
         soundFiles = new HashMap<>();
         String sound = fin.readLine();
         while (sound != null) {
            if (sound.equals("-good-")) {
               currentList = goodSounds;
            } else if (sound.equals("-bad-")) {
               currentList = badSounds;
            } else if (sound.equals("-neutral-")) {
               currentList = neutralSounds;
            } else if (sound.equals("-special-")) {
               currentList = specialSounds;
            } else if (!sound.equals("")) {
               StringTokenizer tk = new StringTokenizer(sound, ",");
               String name = tk.nextToken().trim();
               soundFiles.put(name, tk.nextToken().trim());
               if (currentList != null) {
                  currentList.add(name);
               }
            }

            sound = fin.readLine();
         }
      } catch (Exception e) {
         System.out.println("exception:" + e);
      }
      try {
         if (fin != null) {
            fin.close();
         }
      } catch (IOException e) {
      }
   }

}

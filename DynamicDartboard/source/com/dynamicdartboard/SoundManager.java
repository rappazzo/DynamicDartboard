package com.dynamicdartboard;
import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class SoundManager {
   static Map<String, AudioClip> sounds = new HashMap<String, AudioClip>();
   static Map<String, String> soundFiles = new HashMap<String, String>();
   private static AudioPlayer player = new AudioPlayer();
   static List<String> goodSounds = new ArrayList<String>();
   static List<String> badSounds = new ArrayList<String>();
   static List<String> neutralSounds = new ArrayList<String>();
   static List<String> specialSounds = new ArrayList<String>();

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
         try {
            URL url = new File(file).toURI().toURL();
            clip = Applet.newAudioClip(url);
            sounds.put(name, clip);
         } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
         }
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
         try {
            URL url = new File(file).toURI().toURL();
            AudioClip clip = Applet.newAudioClip(url);
            sounds.put(name, clip);
         } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
         }
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
         fin = new BufferedReader(new InputStreamReader(new FileInputStream("../resources/sounds/sounds.txt")));
         soundFiles = new HashMap<String, String>();
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

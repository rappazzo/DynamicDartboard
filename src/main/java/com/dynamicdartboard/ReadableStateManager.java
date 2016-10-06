package com.dynamicdartboard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author mrappazz
 *
 *
 **/
public class ReadableStateManager {

   private static String dir = "bin/";
   private static final String SCORES = "scores.txt";
   private static final String REMAINING = "remaining.txt";

   public static void setDirectory(String dir) {
      if (dir != null && !dir.equals("")) {
         if (!dir.endsWith("/") || !dir.endsWith("\\")) {
            dir = dir + "/";
         }
         ReadableStateManager.dir = dir;
      }
      File dirFile = new File(ReadableStateManager.dir);
      if (!dirFile.exists()) {
         dirFile.mkdirs();
      }
   }

   public static void persistScores(SortedMap<Integer, String> scores) {
      try {
         OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(dir + SCORES)));
         Iterator<Map.Entry<Integer, String>> it = scores.entrySet().iterator();
         while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            out.append(entry.getKey().toString());
            out.append("\t");
            out.append(entry.getValue());
            if (it.hasNext()) {
               out.append("\n");
            }
         }
         out.close();
      } catch (IOException e) {
         System.out.println("ERROR Persisting " + SCORES.toString() + " - " + e.getMessage());
      }
   }

   public static void persistToGo(List<String> toGo) {
      try {
         OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(dir + REMAINING)));
         Iterator<String> it = toGo.iterator();
         while (it.hasNext()) {
            String line = it.next();
            out.append(line);
            if (it.hasNext()) {
               out.append("\n");
            }
         }
         out.close();
      } catch (IOException e) {
         System.out.println("ERROR Persisting " + REMAINING.toString() + " - " + e.getMessage());
      }
   }

   private static void writeSortedString(Collection<String> unsorted, File target) {
   }

}

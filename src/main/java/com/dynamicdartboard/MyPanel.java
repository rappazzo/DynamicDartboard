package com.dynamicdartboard;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MyPanel extends Panel {

   List<LinePanel> names;
   int width;
   int height;
   String selected = null;
   CommandWindow commandWindow;
   String myID;

   public MyPanel(int width, int height, CommandWindow commandWindow, String id) {
      names = new ArrayList<LinePanel>();
      this.width = width - 20;
      this.height = height - 10;
      this.commandWindow = commandWindow;
      setLayout(null);
      this.myID = id;
   }

   public String getID() {
      return myID;
   }

   public List<LinePanel> getPanels() {
      return names;
   }

   public String getHighestName() {
      LinePanel highest = null;
      if (names != null && names.size() > 0) {
         highest = (LinePanel)names.get(0);
         for (Iterator i = names.iterator(); i.hasNext();) {
            LinePanel item = (LinePanel)i.next();
            if (item.getNumber() > highest.getNumber()) {
               highest = item;
            }
         }
         if (highest != null) {
            return highest.getName();
         }
      }
      return null;
   }

   public void fillPanel(Collection<String> names) {
      LinePanel t = null;
      int i = 0;
      for (String next : names) {
         t = new LinePanel(next, this);
         t.setBounds(0, i, width, 18);
         t.init();
         i += 20;
         add(t);
         this.names.add(t);
      }
      setSize(width, i + 20);
   }

   public void refreshPanel() {
      removeAll();
      int c = 0;
      for (Iterator i = names.iterator(); i.hasNext();) {
         LinePanel item = (LinePanel)i.next();
         item.setBounds(0, c, width, 18);
         c += 20;
         add(item);
      }
      setSize(width, c + 20);

   }

   public void getOrderedList(MyPanel source) {
      List<LinePanel> sourcePanels = source.getPanels();
      if (sourcePanels == null || sourcePanels.size() < 1) {
         return;
      }
      names.clear();
      LinePanel[] s = sourcePanels.toArray(new LinePanel[sourcePanels.size()]);
      for (int i = commandWindow.getColumns() * commandWindow.getRows(); names.size() < s.length && i > 0; i--) {
         for (int j = 0; j < s.length; j++) {
            if (s[j].getNumber() == i) {
               names.add(s[j]);
               break;
            }
         }
      }
   }

   public void select(String name) {
      LinePanel item;
      if (selected != null) {
         item = getLinePanel(selected);
         if (item != null) {
            item.setLabelColor(Color.blue);
         }
      }
      selected = name;
      item = getLinePanel(selected);
      if (item != null) {
         item.setLabelColor(Color.magenta);
         commandWindow.setNextUpText(selected, myID);
      }
   }

   public String getSelected() {
      return selected;
   }

   public String nextName() {
      if (names == null) {
         return null;
      }
      for (Iterator i = names.iterator(); i.hasNext();) {
         LinePanel item = (LinePanel)i.next();
         if (selected == null) {
            select(item.getName());
            return getSelected();
         }
         if (selected.equals(item.getName())) {
            if (i.hasNext()) {
               item = (LinePanel)i.next();
               select(item.getName());
               return getSelected();
            }
         }
      }
      try {
         select(((LinePanel)names.iterator().next()).getName());
         return getSelected();
      } catch (Exception e) {
         return null;
      }
   }

   public void addName(LinePanel panel) {
      names.add(panel);
   }

   public void addName(String name) {
      LinePanel panel = new LinePanel(name, this);
      panel.init();
      names.add(panel);
   }

   public LinePanel removeName(LinePanel panel) {
      select(panel.getName());
      nextName();
      int i = names.indexOf(panel);
      return (LinePanel)names.remove(i);
   }

   public LinePanel getLinePanel(String name) {
      for (int i = 0; i < names.size(); i++) {
         LinePanel item = (LinePanel)names.get(i);
         if (item.getName().equals(name)) {
            return (LinePanel)names.get(i);
         }
      }
      return null;
   }

   public LinePanel removeName(String name) {
      select(name);
      nextName();
      for (int i = 0; i < names.size(); i++) {
         LinePanel item = (LinePanel)names.get(i);
         if (item.getName().equals(name)) {
            return (LinePanel)names.remove(i);
         }
      }
      return null;
   }
}

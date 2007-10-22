package com.dynamicdartboard;
import java.awt.*;
import java.awt.event.*;

class LinePanel extends Panel implements MouseListener {
   String name;
   int number = 0;
   Label label = null;
   MyPanel parent;

   public void setBounds(int x, int y, int w, int h) {
      super.setBounds(x, y, w, h);
      if (label != null) {
         label.setBounds(0, 0, w, h);
      }
   }

   public void setParent(MyPanel parent) {
      this.parent = parent;
   }

   public LinePanel(String name, MyPanel parent) {
      this.name = name;
      label = new Label(name, Label.CENTER);
      addMouseListener(this);
      this.parent = parent;
   }

   public void setNumber(int number) {
      this.number = number;
      if (number != 0) {
         label.setText(name + ": " + number);
      } else {
         label.setText(name);
      }
   }

   public int getNumber() {
      return number;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
      if (number != 0) {
         label.setText(name + ": " + number);
      } else {
         label.setText(name);
      }
   }

   public void mouseClicked(MouseEvent mEvt) {
      Component c = mEvt.getComponent();
      parent.select(name);
   }

   public void mousePressed(MouseEvent mEvt) {
   }

   public void mouseReleased(MouseEvent mEvt) {
   }

   public void mouseEntered(MouseEvent mEvt) {
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      setLabelColor(Color.orange);
      label.validate();
   }

   public void mouseExited(MouseEvent mEvt) {
      setCursor(Cursor.getDefaultCursor());
      if (name.equals(parent.getSelected())) {
         setLabelColor(Color.magenta);
      } else {
         setLabelColor(Color.blue);
      }
   }

   public void setLabelColor(Color color) {
      label.setForeground(color);
      label.validate();
   }

   public void init() {
      setLayout(null);
      if (label != null) {
         add(label);
      }
      label.setForeground(Color.blue);
      label.addMouseListener(this);
   }
}

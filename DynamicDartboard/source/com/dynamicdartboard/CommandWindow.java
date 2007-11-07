package com.dynamicdartboard;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class CommandWindow extends Window implements MouseListener, Serializable {
   DartBoard dartBoard;
   ScrollPane leftsp;
   ScrollPane rightsp;
   ScrollPane ordersp;
   MyPanel leftPanel;
   MyPanel rightPanel;
   MyPanel orderPanel;

   transient BufferedReader fin;
   Label nextUp;
   Label setLabel;
   Label addLabel;
   TextField nameField;
   TextField numberField;
   Label orderListLabel;
   Frame grabBagOrderFrame;

   static final int WIDTH = 200;
   static final int HEIGHT = 100;

   public CommandWindow(Frame frame, DartBoard dartBoard) {
      super(frame);
      this.dartBoard = dartBoard;
   }

   public void init() {
      setSize(1024, HEIGHT);
      setLocation(0, 668);
      setLayout(null);
      leftsp = new ScrollPane();
      rightsp = new ScrollPane();
      leftPanel = new MyPanel(WIDTH, HEIGHT, this, "leftPanel");
      rightPanel = new MyPanel(WIDTH, HEIGHT, this, "rightPanel");
      leftsp.add(leftPanel);
      rightsp.add(rightPanel);
      leftsp.setBounds(DartBoard.getScreenWidthOffset() / 2, 0, WIDTH, HEIGHT - 5);
      rightsp.setBounds(1024 - WIDTH - DartBoard.getScreenWidthOffset() / 2, 0, WIDTH, HEIGHT - 5);
      add(leftsp);
      add(rightsp);
      nextUp = new Label();
      nextUp.setFont(new Font("Arial", Font.BOLD, 25));
      nextUp.setForeground(Color.red);
      nextUp.setAlignment(Label.CENTER);
      nextUp.setBounds((int)((1024 - WIDTH * 2) / 2), 0, (int)(WIDTH * 2), 40);
      add(nextUp);
      setLabel = new Label("Set", Label.CENTER);
      setLabel.setName("Set");
      setLabel.setFont(new Font("Arial", Font.BOLD, 16));
      setLabel.setForeground(Color.blue);
      setLabel.setBounds(1024 - WIDTH - DartBoard.getScreenWidthOffset() / 2 - 40, HEIGHT - 25, 30, 20);
      addLabel = new Label("Add", Label.CENTER);
      addLabel.setName("Add");
      addLabel.setFont(new Font("Arial", Font.BOLD, 16));
      addLabel.setForeground(Color.blue);
      addLabel.setBounds(DartBoard.getScreenWidthOffset() / 2 + WIDTH + 10, HEIGHT - 25, 30, 20);

      add(setLabel);
      add(addLabel);
      setLabel.addMouseListener(this);
      addLabel.addMouseListener(this);

      orderListLabel = new Label("Grab Bag Order", Label.CENTER);
      orderListLabel.setName("Grab Bag Order");
      orderListLabel.setFont(new Font("Arial", Font.BOLD, 16));
      orderListLabel.setForeground(Color.blue);
      orderListLabel.setBounds((int)((1024 - WIDTH) / 2), HEIGHT - 25, WIDTH, 20);
      add(orderListLabel);
      orderListLabel.addMouseListener(this);

      nameField = new TextField();
      nameField.setBounds(DartBoard.getScreenWidthOffset() / 2 + WIDTH + 10, HEIGHT - 55, 50, 20);
      nameField.setName("nameField");
      add(nameField);

      numberField = new TextField();
      numberField.setBounds(1024 - WIDTH - DartBoard.getScreenWidthOffset() / 2 - 40, HEIGHT - 55, 30, 20);
      numberField.setName("numberField");
      add(numberField);

      try {
         fin = new BufferedReader(new InputStreamReader(new FileInputStream("resources/names.txt")));
         leftPanel.fillPanel(fin);
         fin.close();
      } catch (IOException e) {

      }
      pack();
   }

   public int getColumns() {
      return dartBoard.getColumns();
   }

   public int getRows() {
      return dartBoard.getRows();
   }

   public void assignNumber(int number) {
      String name = leftPanel.getSelected();
      if (name == null) {
         name = leftPanel.nextName();
      }
      if (name == null) {
         return;
      }

      LinePanel item = leftPanel.removeName(name);
      if (item != null) {
         leftPanel.refreshPanel();
         rightPanel.addName(item);
         item.setNumber(number);
         item.setParent(rightPanel);
         rightPanel.refreshPanel();
         leftsp.validate();
         rightsp.validate();
      }
   }

   public void setNextUpText(String name, String id) {

      if (id.equals("leftPanel")) {
         if (throwersLeft() <= 1) {
            nextUp.setText("THE END");
         } else {
            nextUp.setText("Next up: " + name);
         }
         nextUp.validate();
      }
   }

   public void addNewName() {
      String name = nameField.getText();
      if (name == null || name.equals("")) {
         return;
      }
      LinePanel item = leftPanel.getLinePanel(name);
      if (item == null) {
         leftPanel.addName(name);
         leftPanel.refreshPanel();
         leftsp.validate();
         nameField.setText("");
         nameField.validate();
      }
   }

   public void setNewNumber() {
      try {
         int num = Integer.parseInt(numberField.getText());
         if (num > 0) {
            String selected = rightPanel.getSelected();
            if (selected == null) {
               return;
            }
            LinePanel panel = rightPanel.getLinePanel(selected);
            int oldNumber = panel.getNumber();
            if (oldNumber > 0) {
               dartBoard.replaceNumber(num, oldNumber);
            } else {
               dartBoard.removeNumber(String.valueOf(num));
            }
            panel.setNumber(num);
            numberField.setText("");
            numberField.validate();
            panel.validate();
         }
      } catch (Exception e) {
         System.out.println("Exception when setting new number");
      }
   }

   public void showGrabBagOrder() {
      if (grabBagOrderFrame == null) {
         grabBagOrderFrame = new Frame();
         grabBagOrderFrame.setSize(400, 650);
         grabBagOrderFrame.setLocation(250, 80);
         WindowAdapter winadapt = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               if (grabBagOrderFrame != null) {
                  rightPanel.refreshPanel();
                  rightsp.validate();
                  grabBagOrderFrame.dispose();
               }
            }
         };
         grabBagOrderFrame.addWindowListener(winadapt);
         grabBagOrderFrame.setResizable(false);
         if (ordersp == null) {
            ordersp = new ScrollPane();
            ordersp.setBounds(20, 20, 380, 600);
            grabBagOrderFrame.add(ordersp);
            if (orderPanel == null) {
               orderPanel = new MyPanel(360, 580, this, "orderPanel");
               ordersp.add(orderPanel);
            }
         }
      }
      orderPanel.getOrderedList(rightPanel);
      orderPanel.refreshPanel();
      grabBagOrderFrame.setVisible(true);
      grabBagOrderFrame.validate();
   }

   public String getSelected() {
      String name = leftPanel.getSelected();
      if (name == null) {
         name = leftPanel.nextName();
      }
      return name;
   }

   public int throwersLeft() {
      if (leftPanel.getPanels() == null) {
         return 0;
      }
      return leftPanel.getPanels().size();
   }

   public String getHighestName() {
      return rightPanel.getHighestName();
   }

   public void mouseClicked(MouseEvent mEvt) {
      Component c = mEvt.getComponent();
      if (c.getName().equals("Add")) {
         addNewName();
      } else if (c.getName().equals("Set")) {
         setNewNumber();
      } else if (c.getName().equals("Grab Bag Order")) {
         showGrabBagOrder();
      }
   }

   public void mousePressed(MouseEvent mEvt) {
   }

   public void mouseReleased(MouseEvent mEvt) {
   }

   public void mouseEntered(MouseEvent mEvt) {
      Component c = mEvt.getComponent();
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      c.setForeground(Color.orange);
      c.validate();
   }

   public void mouseExited(MouseEvent mEvt) {
      Component c = mEvt.getComponent();
      setCursor(Cursor.getDefaultCursor());
      c.setForeground(Color.blue);
      c.validate();
   }

}

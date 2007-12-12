package com.dynamicdartboard;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class CommandWindow extends Window implements MouseListener, Serializable {
   DartBoard dartBoard;
   ScrollPane peopleToGoSP;
   ScrollPane scoresSP;
   ScrollPane ordersp;
   MyPanel peopleToGoPanel;
   MyPanel scoresPanel;
   MyPanel orderPanel;

   Label nextUp;
   Label setLabel;
   Label addLabel;
   TextField nameField;
   TextField numberField;
   Label orderListLabel;
   Frame grabBagOrderFrame;
   List<String> toGoList = new ArrayList();
   SortedMap<Integer, String> scores = new TreeMap();

   static final int WIDTH = 200;
   static final int HEIGHT = 100;

   public CommandWindow(Frame frame, DartBoard dartBoard) {
      super(frame);
      this.dartBoard = dartBoard;
   }

   public void init() {
      DartBoard db = DartBoard.getInstance();
      setSize(db.getScreenWidth(), HEIGHT);
      setLocation(db.getLocationOffset(), DartBoard.getInstance().getScreenHeight() - HEIGHT);
      setLayout(null);
      peopleToGoSP = new ScrollPane();
      scoresSP = new ScrollPane();
      peopleToGoPanel = new MyPanel(WIDTH, HEIGHT, this, "peopleToGoPanel");
      scoresPanel = new MyPanel(WIDTH, HEIGHT, this, "scoresPanel");
      peopleToGoSP.add(peopleToGoPanel);
      scoresSP.add(scoresPanel);
      peopleToGoSP.setBounds(DartBoard.getInstance().getScreenWidthOffset() / 2, 0, WIDTH, HEIGHT - 5);
      scoresSP.setBounds(DartBoard.getInstance().getScreenWidth() - WIDTH - DartBoard.getInstance().getScreenWidthOffset() / 2, 0, WIDTH, HEIGHT - 5);
      add(peopleToGoSP);
      add(scoresSP);
      nextUp = new Label();
      nextUp.setFont(new Font("Arial", Font.BOLD, 25));
      nextUp.setForeground(Color.red);
      nextUp.setAlignment(Label.CENTER);
      nextUp.setBounds((int)((DartBoard.getInstance().getScreenWidth() - WIDTH * 2) / 2), 0, (int)(WIDTH * 2), 40);
      add(nextUp);
      setLabel = new Label("Set", Label.CENTER);
      setLabel.setName("Set");
      setLabel.setFont(new Font("Arial", Font.BOLD, 16));
      setLabel.setForeground(Color.blue);
      setLabel.setBounds(DartBoard.getInstance().getScreenWidth() - WIDTH - DartBoard.getInstance().getScreenWidthOffset() / 2 - 40, HEIGHT - 25, 30, 20);
      addLabel = new Label("Add", Label.CENTER);
      addLabel.setName("Add");
      addLabel.setFont(new Font("Arial", Font.BOLD, 16));
      addLabel.setForeground(Color.blue);
      addLabel.setBounds(DartBoard.getInstance().getScreenWidthOffset() / 2 + WIDTH + 10, HEIGHT - 25, 30, 20);

      add(setLabel);
      add(addLabel);
      setLabel.addMouseListener(this);
      addLabel.addMouseListener(this);

      orderListLabel = new Label("Grab Bag Order", Label.CENTER);
      orderListLabel.setName("Grab Bag Order");
      orderListLabel.setFont(new Font("Arial", Font.BOLD, 16));
      orderListLabel.setForeground(Color.blue);
      orderListLabel.setBounds((int)((DartBoard.getInstance().getScreenWidth() - WIDTH) / 2), HEIGHT - 25, WIDTH, 20);
      add(orderListLabel);
      orderListLabel.addMouseListener(this);

      nameField = new TextField();
      nameField.setBounds(DartBoard.getInstance().getScreenWidthOffset() / 2 + WIDTH + 10, HEIGHT - 55, 50, 20);
      nameField.setName("nameField");
      add(nameField);

      numberField = new TextField();
      numberField.setBounds(DartBoard.getInstance().getScreenWidth() - WIDTH - DartBoard.getInstance().getScreenWidthOffset() / 2 - 40, HEIGHT - 55, 30, 20);
      numberField.setName("numberField");
      add(numberField);

      peopleToGoPanel.fillPanel(db.getUserNames());
      toGoList.addAll(db.getUserNames());
      saveLists();
      pack();
   }

   public int getColumns() {
      return dartBoard.getColumns();
   }

   public int getRows() {
      return dartBoard.getRows();
   }

   public void assignNumber(int number) {
      String name = peopleToGoPanel.getSelected();
      if (name == null) {
         name = peopleToGoPanel.nextName();
      }
      if (name == null) {
         return;
      }

      LinePanel item = peopleToGoPanel.removeName(name);
      if (item != null) {
         peopleToGoPanel.refreshPanel();
         scoresPanel.addName(item);
         item.setNumber(number);
         item.setParent(scoresPanel);
         scoresPanel.refreshPanel();
         peopleToGoSP.validate();
         scoresSP.validate();
      }
      toGoList.remove(name);
      scores.put(Integer.valueOf(number), name);
      saveLists();
   }

   private void saveLists() {
      ReadableStateManager.persistToGo(toGoList);
      ReadableStateManager.persistScores(scores);
   }

   public void setNextUpText(String name, String id) {

      if (id.equals("peopleToGoPanel")) {
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
      LinePanel item = peopleToGoPanel.getLinePanel(name);
      if (item == null) {
         peopleToGoPanel.addName(name);
         peopleToGoPanel.refreshPanel();
         peopleToGoSP.validate();
         nameField.setText("");
         nameField.validate();
      }
   }

   public void setNewNumber() {
      try {
         int num = Integer.parseInt(numberField.getText());
         if (num > 0) {
            String selected = scoresPanel.getSelected();
            if (selected == null) {
               return;
            }
            LinePanel panel = scoresPanel.getLinePanel(selected);
            int oldNumber = panel.getNumber();
            if (oldNumber > 0) {
               replaceNumber(num, oldNumber);
            } else {
               removeNumber(String.valueOf(num));
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

   private void removeNumber(String number) {
      try {
         dartBoard.getBoard().removeNumber(Integer.valueOf(number));
         dartBoard.repaint();
      } catch (NumberFormatException e) {
         System.out.println("That is not a number!");
      }
   }

   private void replaceNumber(int source, int replace) {
      try {
         dartBoard.getBoard().replaceNumber(Integer.valueOf(source), Integer.valueOf(replace));
         dartBoard.repaint();
      } catch (NumberFormatException e) {
         System.out.println("That is not a number!");
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
                  scoresPanel.refreshPanel();
                  scoresSP.validate();
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
      orderPanel.getOrderedList(scoresPanel);
      orderPanel.refreshPanel();
      grabBagOrderFrame.setVisible(true);
      grabBagOrderFrame.validate();
   }

   public String getSelected() {
      String name = peopleToGoPanel.getSelected();
      if (name == null) {
         name = peopleToGoPanel.nextName();
      }
      return name;
   }

   public int throwersLeft() {
      if (peopleToGoPanel.getPanels() == null) {
         return 0;
      }
      return peopleToGoPanel.getPanels().size();
   }

   public String getHighestName() {
      return scoresPanel.getHighestName();
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

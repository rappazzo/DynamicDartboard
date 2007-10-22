package com.dynamicdartboard;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class DartBoard extends Window implements MouseListener, Serializable {
   Board board;
   CommandWindow commandWindow;
   Frame parent;
   Window leftWindow;
   Window rightWindow;
   Window topWindow;
   Window bottomWindow;
   ImageCanvas bells;
   ImageCanvas giftBox;
   ImageCanvas ornament;
   ImageCanvas bells2;
   ImageCanvas giftBox2;
   ImageCanvas ornament2;
   ImageCanvas bellsR;
   ImageCanvas giftBoxR;
   ImageCanvas ornamentR;
   ImageCanvas bellsR2;
   ImageCanvas giftBoxR2;
   ImageCanvas ornamentR2;
   AnimatedImageCanvas topCanvas;
   ImageCanvas bottomCanvas;

   public static final int W_OFFSET = 120;
   public static final int H_OFFSET = 40;

   transient static WindowAdapter winadapt = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
         System.exit(0);
      }
   };

   public DartBoard(Frame frame) {
      super(frame);
      parent = frame;
   }

   public int getColumns() {
      return board.getColumns();
   }

   public int getRows() {
      return board.getRows();
   }

   public void init(int columns, int rows) {
      board = new Board(columns, rows, this);
      addWindowListener(winadapt);
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      commandWindow = new CommandWindow(parent, this);
      commandWindow.init();
      commandWindow.addWindowListener(winadapt);
      commandWindow.setVisible(true);
      leftWindow = new Window(parent);
      rightWindow = new Window(parent);
      leftWindow.setSize(DartBoard.W_OFFSET, 668);
      leftWindow.setLocation(0, 0);
      rightWindow.setSize(DartBoard.W_OFFSET, 668);
      rightWindow.setLocation(1024 - DartBoard.W_OFFSET, 0);
      bells = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBox = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornament = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      bells2 = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBox2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornament2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      bellsR = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBoxR = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornamentR = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      bellsR2 = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBoxR2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornamentR2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      topCanvas = new AnimatedImageCanvas(new String[]{"resources/images/lites1.gif", "resources/images/lites2.gif", "resources/images/lites3.gif"}, 1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      bottomCanvas = new ImageCanvas("resources/images/holly_li.gif", 1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);

      leftWindow.setLayout(new FlowLayout());
      leftWindow.add(bells);
      leftWindow.add(giftBox);
      leftWindow.add(ornament);
      leftWindow.add(bells2);
      leftWindow.add(giftBox2);
      leftWindow.add(ornament2);
      rightWindow.setLayout(new FlowLayout());
      rightWindow.add(bellsR);
      rightWindow.add(giftBoxR);
      rightWindow.add(ornamentR);
      rightWindow.add(bellsR2);
      rightWindow.add(giftBoxR2);
      rightWindow.add(ornamentR2);
      leftWindow.setVisible(true);
      rightWindow.setVisible(true);
      leftWindow.addWindowListener(winadapt);
      rightWindow.addWindowListener(winadapt);

      topWindow = new Window(parent);
      bottomWindow = new Window(parent);
      topWindow.setLayout(new BorderLayout());
      bottomWindow.setLayout(new BorderLayout());
      topWindow.setSize(1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      topWindow.setLocation(DartBoard.W_OFFSET, 0);
      bottomWindow.setSize(1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      bottomWindow.setLocation(DartBoard.W_OFFSET, 668 - DartBoard.H_OFFSET);
      topWindow.addWindowListener(winadapt);
      bottomWindow.addWindowListener(winadapt);

      topWindow.add(topCanvas, BorderLayout.CENTER);
      bottomWindow.add(bottomCanvas, BorderLayout.CENTER);
      topWindow.setVisible(true);
      bottomWindow.setVisible(true);

   }

   public void start() {
      addMouseListener(this);
   }

   public void paint(Graphics g) {
      Font font = new Font("Arial", Font.BOLD, 18);
      Dimension d = getSize();
      int w = (int)(d.getWidth() / board.getColumns());
      int h = (int)(d.getHeight() / board.getRows());
      Box.setWidth(w);
      Box.setHeight(h);
      g.setFont(font);
      setBackground(Color.white);
      if (commandWindow != null && commandWindow.throwersLeft() > 0) {
         board.drawBoard(g);
      }
   }

   public void removeNumber(String number) {
      try {
         board.removeNumber(new Integer(number));
         repaint();
      } catch (NumberFormatException e) {
         System.out.println("That is not a number!");
      }
   }

   public void replaceNumber(int source, int replace) {
      try {
         board.replaceNumber(new Integer(source), new Integer(replace));
         repaint();
      } catch (NumberFormatException e) {
         System.out.println("That is not a number!");
      }
   }

   protected void processMouseEvent(MouseEvent e) {
      if (e.getID() == MouseEvent.MOUSE_RELEASED && "DartBoard".equals(e.getComponent().getName()) && commandWindow.throwersLeft() > 0) {
         int x = e.getX();
         int y = e.getY();
         if (x <= 0 || y <= 0) {
            return;
         }
         x = x / Box.getWidth();
         y = y / Box.getHeight();
         BoardNumber hit = board.removeBox(x, y);
         repaint();
         if (hit != null && hit.getNumber().intValue() != 0) {
            Animation animation = hit.getAnimation();
            if (animation != null) {
               animation.play(commandWindow.getSelected());
            } else {
               Integer num = hit.getNumber();
               double percent = board.getPercent(num);

               if (percent <= 25) {
                  SoundManager.playRandom("good");
               } else if (percent <= 75) {
                  SoundManager.playRandom("neutral");
               } else {
                  SoundManager.playRandom("bad");
               }
            }
            commandWindow.assignNumber(hit.getNumber().intValue());
            save(hit.getNumber().intValue());
            if (commandWindow.throwersLeft() == 0) {
               try {
                  Thread.sleep(2000);
               } catch (InterruptedException se) {
               }
               String loser = commandWindow.getHighestName();
               if (loser != null) {
                  new JeopardyWindow(parent).play(loser);
               }
            }
         }
      }
   }

   public void save(int x) {
      try {
         FileOutputStream fout = new FileOutputStream("saves\\dartBoard." + x);
         ObjectOutputStream out = new ObjectOutputStream(fout);
         out.writeObject(this);
         out.close();
      } catch (Exception e) {
         System.out.println(e);
      }
   }

   public static DartBoard load(String filename) {
      try {
         FileInputStream fin = new FileInputStream(filename);
         ObjectInputStream in = new ObjectInputStream(fin);
         DartBoard db = (DartBoard)in.readObject();
         in.close();
         return db;
      } catch (Exception e) {
         System.out.println(e);
      }
      return null;
   }

   public void mousePressed(MouseEvent mEvt) {
   }

   public void mouseReleased(MouseEvent mEvt) {
   }

   public void mouseEntered(MouseEvent mEvt) {
   }

   public void mouseExited(MouseEvent mEvt) {
   }

   public void mouseClicked(MouseEvent mEvt) {
   }

   public void reInit() {
      leftWindow.removeAll();
      rightWindow.removeAll();
      topWindow.removeAll();
      bottomWindow.removeAll();
      bells = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBox = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornament = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      bells2 = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBox2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornament2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      bellsR = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBoxR = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornamentR = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      bellsR2 = new ImageCanvas("resources/images/bells.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      giftBoxR2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      ornamentR2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.W_OFFSET - 20, DartBoard.W_OFFSET - 20);
      topCanvas = new AnimatedImageCanvas(new String[]{"resources/images/lites1.gif", "resources/images/lites2.gif", "resources/images/lites3.gif"}, 1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      bottomCanvas = new ImageCanvas("resources/images/holly_li.gif", 1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      addWindowListener(winadapt);
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      commandWindow.addWindowListener(winadapt);
      commandWindow.setVisible(true);
      leftWindow.setSize(DartBoard.W_OFFSET, 668);
      leftWindow.setLocation(0, 0);
      rightWindow.setSize(DartBoard.W_OFFSET, 668);
      rightWindow.setLocation(1024 - DartBoard.W_OFFSET, 0);
      leftWindow.setLayout(new FlowLayout());
      leftWindow.add(bells);
      leftWindow.add(giftBox);
      leftWindow.add(ornament);
      leftWindow.add(bells2);
      leftWindow.add(giftBox2);
      leftWindow.add(ornament2);
      rightWindow.setLayout(new FlowLayout());
      rightWindow.add(bellsR);
      rightWindow.add(giftBoxR);
      rightWindow.add(ornamentR);
      rightWindow.add(bellsR2);
      rightWindow.add(giftBoxR2);
      rightWindow.add(ornamentR2);
      topWindow.setLayout(new BorderLayout());
      bottomWindow.setLayout(new BorderLayout());
      topWindow.setSize(1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      topWindow.setLocation(DartBoard.W_OFFSET, 0);
      bottomWindow.setSize(1024 - 2 * DartBoard.W_OFFSET, DartBoard.H_OFFSET);
      bottomWindow.setLocation(DartBoard.W_OFFSET, 668 - DartBoard.H_OFFSET);
      topWindow.addWindowListener(winadapt);
      bottomWindow.addWindowListener(winadapt);
      topWindow.add(topCanvas, BorderLayout.CENTER);
      bottomWindow.add(bottomCanvas, BorderLayout.CENTER);
      setVisible(true);
      leftWindow.setVisible(true);
      rightWindow.setVisible(true);
      topWindow.setVisible(true);
      bottomWindow.setVisible(true);
      toFront();
   }

   public static void main(String args[]) {
      try {
         DartBoard dartBoard = null;
         if (args.length == 1) {
            String file = args[0];
            dartBoard = load(file);
            dartBoard.reInit();
         } else if (args.length == 2) {
            int columns = Integer.parseInt(args[0]);
            int rows = Integer.parseInt(args[1]);
            Frame parent = new Frame();
            dartBoard = new DartBoard(parent);
            dartBoard.setSize(1024 - 2 * DartBoard.W_OFFSET, 668 - 2 * DartBoard.H_OFFSET);
            dartBoard.setLocation(DartBoard.W_OFFSET, DartBoard.H_OFFSET);
            dartBoard.setName("DartBoard");
            dartBoard.init(columns, rows);
            dartBoard.start();
            parent.addWindowListener(winadapt);
            dartBoard.setVisible(true);
            dartBoard.toFront();

         } else {
            System.out.println("Usage:  java DartBoard <column count> <row count>\ni.e: java DartBoard 12 11\n");
            return;
         }
         SoundManager.readSoundList();
         SoundManager.loadSounds();
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println(e + "\nUsage:  java DartBoard <column count> <row count>\ni.e: java DartBoard 12 11\n");
         return;
      }
   }
}
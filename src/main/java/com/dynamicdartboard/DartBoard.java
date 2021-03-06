package com.dynamicdartboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dynamicdartboard.actionhandler.ActionHandler;

public class DartBoard extends Window implements MouseListener, Serializable {

   public static final String DARTBOARD = "DartBoard";

   private NavigableSet<String> userNames = null;
   private Map<String, String> options = null;

   private Integer screenWidth = null;
   private Integer screenHeight = null;
   private Integer widthOffset = null;
   private Integer heightOffset = null;
   private int locationOffset = 0;

   Board board;
   CommandWindow commandWindow;
   Frame parent;
   Map<String, Window> windowMap = new HashMap();

   public static final String TOP = "top";
   public static final String BOTTOM = "bottom";
   public static final String LEFT = "left";
   public static final String RIGHT = "right";

   private static final int WIDTH_OFFSET_PERCENTAGE = 12;
   private static final int HEIGHT_OFFSET_PERCENTAGE = 6;
   public static final String DEFAULT_IMAGE_RESOURCE_NAME = "images/decorations.ini";

   private static DartBoard INSTANCE = null;

   transient static WindowAdapter winadapt = new WindowAdapter() {
      @Override
	public void windowClosing(WindowEvent e) {
         System.exit(0);
      }
   };

   /**
    * get the singletone instance
    */
   public static DartBoard getInstance() {
      return INSTANCE;
   }

   /**
    * create a new dartboard
    */
   public static DartBoard create(Frame frame) {
      if (INSTANCE != null) {
         throw new IllegalStateException("Cannot Create a new dart board instance.  There can be only one!");
      }
      INSTANCE = new DartBoard(frame);
      return INSTANCE;
   }

   /**
    * load an existing dartboard
    */
   public static DartBoard load(String filename) {
      if (INSTANCE != null) {
         throw new IllegalStateException("Cannot Create a new dart board instance.  There can be only one!");
      }
      try {
         ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
         DartBoard db = (DartBoard)in.readObject();
         in.close();
         INSTANCE = db;
         return db;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   /**
    * Constructor
    */
   private DartBoard(Frame frame) {
      super(frame);
      parent = frame;
      parent.addWindowListener(winadapt);
   }

   /**
    * get/set the (usable) screen width
    */
   public int getScreenWidth() {
      if (screenWidth == null) {
         Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
         screenWidth = Integer.valueOf((int)dimension.getWidth());
      }
      return screenWidth.intValue();
   }

   public void setScreenWidth(int newScreenWidth) {
      if (newScreenWidth > 100) {
         screenWidth = newScreenWidth;
      }
   }

   public int getScreenWidthOffset() {
      if (widthOffset == null) {
         if (screenWidth == null) {
            throw new IllegalStateException("Cannot get the screen width offset when the screen width is not yet set.");
         }
         widthOffset = (getScreenWidth() * WIDTH_OFFSET_PERCENTAGE) / 100;
      }
      return widthOffset.intValue();
   }

   /**
    * get/set the (usable) screen height
    */
   public int getScreenHeight() {
      if (screenHeight == null) {
         Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
         screenHeight = Integer.valueOf((int)dimension.getHeight());
      }
      return screenHeight.intValue();
   }

   public void setScreenHeight(int newScreenHeight) {
      if (newScreenHeight > 100) {
         this.screenHeight = newScreenHeight;
      }
   }

   public int getScreenHeightOffset() {
      if (heightOffset == null) {
         if (screenHeight == null) {
            throw new IllegalStateException("Cannot get the screen height offset when the screen height is not yet set.");
         }
         heightOffset = (getScreenHeight() * HEIGHT_OFFSET_PERCENTAGE) / 100;
      }
      return heightOffset.intValue();
   }

   public int getLocationOffset() {
      return locationOffset;
   }

   public void setLocationOffset(int offset) {
      if (offset >= 0) {
         this.locationOffset = offset;
      }
   }

   /**
    * get/set/add the list of names to be used in the dartboard
    */
   public void setUserNames(Collection<String> names) {
      this.userNames = names instanceof TreeSet ? (TreeSet)names : new TreeSet(names);
   }

   public void addUserName(String name) {
      if (this.userNames == null) {
         this.userNames = new TreeSet();
      }
      this.userNames.add(name);
   }

   public Collection<String> getUserNames() {
      return this.userNames;
   }

   /**
    * get/set the options for the dartboard
    */
   public Map<String, String> getOptions() {
      return this.options;
   }

   public void setOptions(Map<String, String> options) {
      this.options = options;
   }

   public String getOptionValue(String optionName) {
      return this.options != null ? this.options.get(optionName) : null;
   }

   public Board getBoard() {
      return this.board;
   }

   public CommandWindow getCommandWindow() {
      return this.commandWindow;
   }

   public int getColumns() {
      return getBoard().getColumns();
   }

   public int getRows() {
      return getBoard().getRows();
   }

   public void reInit() {
      windowMap.get(LEFT).removeAll();
      windowMap.get(RIGHT).removeAll();
      windowMap.get(TOP).removeAll();
      windowMap.get(BOTTOM).removeAll();
      setupWindows();
   }

   public void init(int rows, int columns) {
      board = new Board(columns, rows, this);
      commandWindow = new CommandWindow(parent, this);
      windowMap.put(LEFT, new Window(parent));
      windowMap.put(RIGHT, new Window(parent));
      windowMap.put(TOP, new Window(parent));
      windowMap.put(BOTTOM, new Window(parent));
      commandWindow.init();
      Window left = windowMap.get(LEFT);
      Window right = windowMap.get(RIGHT);
      left.addWindowListener(winadapt);
      right.addWindowListener(winadapt);
      setupWindows();
   }

   private void setupWindows() {
      addWindowListener(winadapt);
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      commandWindow.addWindowListener(winadapt);
      commandWindow.setVisible(true);
      Window left = windowMap.get(LEFT);
      left.setLayout(new FlowLayout());
      left.setSize(getScreenWidthOffset(), getScreenHeight() - CommandWindow.HEIGHT);
      left.setLocation(getLocationOffset(), 0);
      Window right = windowMap.get(RIGHT);
      right.setLayout(new FlowLayout());
      right.setSize(getScreenWidthOffset(), getScreenHeight() - CommandWindow.HEIGHT);
      right.setLocation(getLocationOffset() + getScreenWidth() - getScreenWidthOffset(), 0);
      Window top = windowMap.get(TOP);
      top.setLayout(new BorderLayout());
      top.setSize(getScreenWidth() - 2 * getScreenWidthOffset(), getScreenHeightOffset());
      top.setLocation(getLocationOffset() + getScreenWidthOffset(), 0);
      top.addWindowListener(winadapt);
      Window bottom = windowMap.get(BOTTOM);
      bottom.setLayout(new BorderLayout());
      bottom.setSize(getScreenWidth() - 2 * getScreenWidthOffset(), getScreenHeightOffset());
      bottom.setLocation(getLocationOffset() + getScreenWidthOffset(), getScreenHeight() - getScreenHeightOffset() - CommandWindow.HEIGHT);
      bottom.addWindowListener(winadapt);

      Map<String, Dimension> windowDimension = new HashMap();
      windowDimension.put(LEFT, new Dimension(getScreenWidthOffset() - 20, getScreenWidthOffset() - 20));
      windowDimension.put(RIGHT, new Dimension(getScreenWidthOffset() - 20, getScreenWidthOffset() - 20));
      windowDimension.put(TOP, new Dimension(getScreenWidth() - 2 * getScreenWidthOffset(), getScreenHeightOffset()));
      windowDimension.put(BOTTOM, new Dimension(getScreenWidth() - 2 * getScreenWidthOffset(), getScreenHeightOffset()));

      try {
    	 InputStream decIn = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_IMAGE_RESOURCE_NAME);
         if (decIn != null) {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(decIn, "UTF-8"));
            Pattern sectionPattern = Pattern.compile("\\[(\\w+)(\\s(\\w+)=\"?(\\w+)\"?)?\\]");
            String section = null;
            String sectionAlign = null;
            String lastRead = null;
            do {
               lastRead = reader.readLine();
               if (lastRead != null && !lastRead.equals("")) {
                  Matcher matcher = sectionPattern.matcher(lastRead);
                  if (matcher.find()) {
                     section = matcher.group(1);
                     sectionAlign = matcher.group(4);
                  } else if (section != null) {
                     //image data
                     String[] images = lastRead.split(",");
                     windowMap.get(section).add(new ImageCanvas("images/", images, windowDimension.get(section)), sectionAlign);
                  }
               }
            } while (lastRead != null);
         }
      } catch (IOException e) {
         //names = null;
      }
      left.setVisible(true);
      right.setVisible(true);
      top.setVisible(true);
      bottom.setVisible(true);
      setVisible(true);
      toFront();
   }

   public void start() {
      addMouseListener(this);
   }

   @Override
public void paint(Graphics g) {
      Font font = new Font("Arial", Font.BOLD, 18);
      Dimension d = getSize();
      Board board = getBoard();
      int w = (int)(d.getWidth() / board.getColumns());
      //we should account for the rows being offset by a half width.
      int wOffset = w / (2 * board.getColumns()) + 1;
      int h = (int)(d.getHeight() / (board.getRows() - 1));
      int hOffset = h / (3 * board.getRows()) + 1;
      Box.setWidth(w - wOffset);
      Box.setHeight(h - hOffset);
      g.setFont(font);
      setBackground(Color.white);
      if (commandWindow != null && commandWindow.throwersLeft() > 0) {
         board.drawBoard(g);
      }
   }

   public void save(int x) {
      try {
    	 File saveDir = new File(DartBoardSetup.getDartboardPath() + "bin/saves/");
    	 if (!saveDir.exists()) {
    		 saveDir.mkdirs();
    	 }
         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(saveDir, String.valueOf(x))));
         out.writeObject(this);
         out.close();
      } catch (Exception e) {
         System.out.println(e);
      }
   }

   @Override
public void mouseReleased(MouseEvent mEvt) {
      if (DARTBOARD.equals(mEvt.getComponent().getName()) && commandWindow.throwersLeft() > 0) {
         int x = mEvt.getX();
         int y = mEvt.getY();

         if (x <= 0 || y <= 0) {
            return;
         }
         int r = 0;
         if (y < Box.getHeight() / 4.0) {
            r = (int)(Box.getHeight() / 2.0) + y;
            y = -1;
         } else {
            y = (int)(y - Box.getHeight() / 4.0);
            r = (int)(y % (3.0 * Box.getHeight() / 4));
            y = (int)(y / (3.0 * Box.getHeight() / 4));
         }
         if (Math.abs(y) % 2 == 1) {
            x = x - Box.getWidth() / 2;
         }
         if (x < 0) {
            return;
         }
         int rx = x % Box.getWidth();
         x = x / Box.getWidth();
         if (r > Box.getHeight() / 2.0) {
            double M = (Box.getHeight() / 4.0) / (Box.getWidth() / 2.0); //get default slope line
            if (rx < Box.getWidth() / 2.0) {
               double m = (r - (Box.getHeight() / 2.0)) / (rx);
               if (m < M) {
                  y--;
               } else if (Math.abs(y) % 2 == 0) {
                  x--;
               }

            } else {
               double m = (r - (Box.getHeight() / 2.0)) / (Box.getWidth() - rx);
               if (m < M) {
                  y--;
               } else if (Math.abs(y) % 2 == 1) {
                  x++;
               }
            }
            y++;

         }
         BoardNumber hit = getBoard().findBoardNumber(x, y);
//if (hit != null && hit.getNumber().intValue() == 15) {
//   System.exit(0);
//} else
         if (hit != null && hit.getNumber().intValue() != 0) {
            ActionHandler actionHandler = hit.getActionHandler();
            actionHandler.handle(hit);
            //BEGIN sounds
            Integer num = hit.getNumber();
            double percent = getBoard().getPercent(num);

            if (percent <= 25) {
               SoundManager.playRandom("good");
            } else if (percent <= 75) {
               SoundManager.playRandom("neutral");
            } else {
               SoundManager.playRandom("bad");
            }
            //END sounds
            save(hit.getNumber().intValue());
            if (commandWindow.throwersLeft() == 0) {
               try {
                  //$MR TODO: Add a special handler
                  Thread.sleep(2000);
               } catch (InterruptedException se) {
               }
            }
         }
         repaint();
      }
   }

   //Unused mouse events which are implemented as part of the MouseListener Interface
   @Override
public void mousePressed(MouseEvent mEvt) {
   }

   @Override
public void mouseEntered(MouseEvent mEvt) {
   }

   @Override
public void mouseExited(MouseEvent mEvt) {
   }

   @Override
public void mouseClicked(MouseEvent mEvt) {
   }

   public static void main(String[] args) {
      DartBoardSetup.run(args);
   }

}
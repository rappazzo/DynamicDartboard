package com.dynamicdartboard;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

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
   
   
   private static Integer screenWidth = null;
   private static Integer screenHeight = null;
   private static Integer widthOffset = null;
   private static Integer heightOffset = null;
   private static final int WIDTH_OFFSET_PERCENTAGE = 12;
   private static final int HEIGHT_OFFSET_PERCENTAGE = 6;

   transient static WindowAdapter winadapt = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
         System.exit(0);
      }
   };

   public DartBoard(Frame frame) {
      super(frame);
      parent = frame;
   }
   
   public static int getScreenWidth() {
      if (screenWidth == null) {
         Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
         screenWidth = Integer.valueOf((int)dimension.getWidth());
      }
      return screenWidth.intValue();
   }
   
   public static void setScreenWidth(int newScreenWidth) {
      if (newScreenWidth > 100) {
         screenWidth = newScreenWidth;
      }
   }
   
   public static int getScreenWidthOffset() {
      if (widthOffset == null) {
         if (screenWidth == null) {
            throw new IllegalStateException("Cannot get the screen width offset when the screen width is not yet set.");
         }
         widthOffset = (getScreenWidth() * WIDTH_OFFSET_PERCENTAGE) / 100;
      }
      return widthOffset.intValue();
   }
   
   public static int getScreenHeight() {
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

   public static int getScreenHeightOffset() {
      if (heightOffset == null) {
         if (screenHeight == null) {
            throw new IllegalStateException("Cannot get the screen height offset when the screen height is not yet set.");
         }
         heightOffset = (getScreenHeight() * HEIGHT_OFFSET_PERCENTAGE) / 100;
      }
      return heightOffset.intValue();
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
      leftWindow.setSize(DartBoard.getScreenWidthOffset(), getScreenHeight());
      leftWindow.setLocation(0, 0);
      rightWindow.setSize(DartBoard.getScreenWidthOffset(), getScreenHeight());
      rightWindow.setLocation(getScreenWidth() - DartBoard.getScreenWidthOffset(), 0);
      bells = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBox = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornament = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      bells2 = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBox2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornament2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      bellsR = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBoxR = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornamentR = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      bellsR2 = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBoxR2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornamentR2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      topCanvas = new AnimatedImageCanvas(new String[]{"resources/images/lites1.gif", "resources/images/lites2.gif", "resources/images/lites3.gif"}, getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      bottomCanvas = new ImageCanvas("resources/images/holly_li.gif", getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());

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
      topWindow.setSize(getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      topWindow.setLocation(DartBoard.getScreenWidthOffset(), 0);
      bottomWindow.setSize(getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      bottomWindow.setLocation(DartBoard.getScreenWidthOffset(), getScreenHeight() - DartBoard.getScreenHeightOffset());
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
      bells = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBox = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornament = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      bells2 = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBox2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornament2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      bellsR = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBoxR = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornamentR = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      bellsR2 = new ImageCanvas("resources/images/bells.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      giftBoxR2 = new ImageCanvas("resources/images/giftbx.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      ornamentR2 = new ImageCanvas("resources/images/ornament.gif", DartBoard.getScreenWidthOffset() - 20, DartBoard.getScreenWidthOffset() - 20);
      topCanvas = new AnimatedImageCanvas(new String[]{"resources/images/lites1.gif", "resources/images/lites2.gif", "resources/images/lites3.gif"}, getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      bottomCanvas = new ImageCanvas("resources/images/holly_li.gif", getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      addWindowListener(winadapt);
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      commandWindow.addWindowListener(winadapt);
      commandWindow.setVisible(true);
      leftWindow.setSize(DartBoard.getScreenWidthOffset(), getScreenHeight());
      leftWindow.setLocation(0, 0);
      rightWindow.setSize(DartBoard.getScreenWidthOffset(), getScreenHeight());
      rightWindow.setLocation(getScreenWidth() - DartBoard.getScreenWidthOffset(), 0);
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
      topWindow.setSize(getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      topWindow.setLocation(DartBoard.getScreenWidthOffset(), 0);
      bottomWindow.setSize(getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
      bottomWindow.setLocation(DartBoard.getScreenWidthOffset(), getScreenHeight() - DartBoard.getScreenHeightOffset());
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
      StringBuilder usage = new StringBuilder();
      usage.append("Usage:  java DartBoard [options]\n");
      usage.append("  Options:\n");
      usage.append("    -w --width <##> - specify the screen width (full width is default)\n");
      usage.append("    -h --height <## > - specify the screen height (full height is default)\n");
      usage.append("    -f --file <file name> - specify a file to resume a game in progress\n");
      usage.append("    -r --rows <##> - specify the number of rows\n");
      usage.append("    -c --columns <##> - specify the number of columns\n");
      usage.append("    -l --list <file name> - specify a file with the list of names to start from.\n");
      usage.append("    -h --help - show this help text.\n\n");
      usage.append(" A dartboard will be started in one of these possible option sets (in the following order of priority):\n");
      usage.append("    (1) Resume from a previous session (this will ignore the ROWS, COLUMNS, and LIST options).\n");
      usage.append("    (2) Specify the number of ROWS and COLUMNS (LIST is optional).\n");
      usage.append("    (3) Specify the number of ROWS only (LIST is required and columns is determined by the list entries).\n");
      usage.append("    (4) Specify the number of COLUMNS only (LIST is required and rows is determined by the list entries).\n");
      usage.append("    (5) Specify a LIST file (rows and columns with be determined by the number or list entries).\n");
      try {
         DartBoard dartBoard = null;
         Map<String, String> argMap = parseArgs(args);

         if (argMap.get(HELP) != null) {
            System.out.println(usage.toString());
            return;
         } else if (argMap.get(FILE) != null) {
            dartBoard = load(argMap.get(FILE));
            setScreenDimensions(argMap, dartBoard);
            dartBoard.reInit();
         } else {
            //now we require either rows and columns or a list option
            int rows = getIntArg(ROWS, argMap);
            int cols = getIntArg(COLS, argMap);
            String listfile = argMap.get(LIST);
            List<String> list = null;
            if (listfile != null) {
               BufferedReader reader = new BufferedReader(new FileReader(listfile));
               list = new ArrayList<String>();
               if (reader != null) {
                  String lastRead = null;
                  do {
                     lastRead = reader.readLine();
                     if (lastRead != null) {
                        list.add(lastRead);
                     }
                  } while (lastRead != null);
               }
            }
            //make sure minumum args are present
            if (list != null || (rows > 0 && cols > 0)) {
               int numberOfCells = list != null ? list.size() + 1 : rows * cols;
               if (rows < 0 && cols < 0) {
                  //determine the number of rows and columns by the list size
                  //with a total number make the board a rectangle which is close to even sides.
                  int sqrt = (int)Math.sqrt(numberOfCells);
                  //should we deal with perfect squares, considering that a monitor is typically rectangular?
                  if (Math.pow(sqrt, 2) == numberOfCells) {
                     //perfect square
                     rows = sqrt;
                     cols = sqrt;
                  } else {
                     rows = sqrt <= 1 ? 1 : sqrt - 1;
                     cols = sqrt <= 1 ? numberOfCells : sqrt + 2;
                  }
                  //ensure that the min number of cells is met
                  while (rows * cols < numberOfCells) {
                     cols++;
                  }
               } else if (rows < 0) {
                  rows = numberOfCells / cols;
                  //ensure that the min number of cells is met
                  while (rows * cols < numberOfCells) {
                     rows++;
                  }
               } else if (cols < 0) {
                  cols = numberOfCells / rows;
                  //ensure that the min number of cells is met
                  while (rows * cols < numberOfCells) {
                     cols++;
                  }
               }
               Frame parent = new Frame();
               dartBoard = new DartBoard(parent);
               setScreenDimensions(argMap, dartBoard);
               dartBoard.setSize(getScreenWidth() - 2 * DartBoard.getScreenWidthOffset(), getScreenHeight() - 2 * DartBoard.getScreenHeightOffset());
               dartBoard.setLocation(DartBoard.getScreenWidthOffset(), DartBoard.getScreenHeightOffset());
               dartBoard.setName("DartBoard");
               dartBoard.init(cols, rows);
               dartBoard.start();
               parent.addWindowListener(winadapt);
               dartBoard.setVisible(true);
               dartBoard.toFront();
            } else {
               System.out.println(usage.toString());
               return;
            }
         }
         SoundManager.readSoundList();
         SoundManager.loadSounds();
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println(usage.toString());
         return;
      }
   }
   
   private static int getIntArg(String argName, Map<String, String> argMap) {
      try {
         return Integer.valueOf(argMap.get(argName)).intValue();
      } catch (NumberFormatException e) {
         //ignore
      }
      return -1;
   }
   
   private static void setScreenDimensions(Map<String, String> argMap, DartBoard dartBoard) {
      int screenWidth = getIntArg(SCREEN_WIDTH, argMap);
      if (screenWidth > 0) {
         dartBoard.setScreenWidth(screenWidth);
      }
      int screenHeight = getIntArg(SCREEN_HEIGHT, argMap);
      if (screenHeight > 0) {
         dartBoard.setScreenHeight(screenHeight);
      }
   }

   //Arguments
   public static final String SCREEN_WIDTH = "width";
   public static final String SCREEN_HEIGHT = "height";
   public static final String ROWS = "rows";
   public static final String COLS = "columns";
   public static final String FILE = "file";
   public static final String LIST = "list";
   public static final String HELP = "help";
   public static final Collection<String> ARG_TYPES = Collections.unmodifiableCollection(Arrays.asList(new String[] {
      ROWS, COLS, FILE, LIST, HELP, SCREEN_WIDTH, SCREEN_HEIGHT,
   }));
   public static final Collection<String> ARGS_WITH_OPTIONS = Collections.unmodifiableCollection(Arrays.asList(new String[] {
      ROWS, COLS, FILE, LIST, SCREEN_WIDTH, SCREEN_HEIGHT,
   }));

   private static Map<String, String> parseArgs(String[] args) {
      Map<String, String> argMap = new HashMap();
      if (args != null) {
         for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
               String argName = null;
               String argValue = null;
               if (args[i].charAt(1) != '-') {
                  //short arg
                  for (String argType : ARG_TYPES) {
                     if (argType.charAt(0) == args[i].charAt(1)) {
                        argName = argType;
                        break;
                     }
                  }
               } else {
                  //long arg
                  argName = args[i];
               }
               if (ARGS_WITH_OPTIONS.contains(argName)) {
                  argValue = args[++i];
               } else {
                  argValue = argName;
               }
               if (ARG_TYPES.contains(argName)) {
                  argMap.put(argName, argValue);
               }
            }
         }
      }
      return argMap;
   }
}
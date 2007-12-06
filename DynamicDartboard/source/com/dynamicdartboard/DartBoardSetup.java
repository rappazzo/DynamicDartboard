package com.dynamicdartboard;

import java.awt.*;
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.List;

public class DartBoardSetup {
   
   private static String dartboardPath = null;

   //Arguments
   public static final String SCREEN_WIDTH = "width";
   public static final String SCREEN_HEIGHT = "height";
   public static final String OFFSET = "offset";
   public static final String ROWS = "rows";
   public static final String COLS = "columns";
   public static final String FILE = "file";
   public static final String NAMES = "list";
   public static final String HELP = "help";
   public static final Collection<String> ARG_TYPES = Collections.unmodifiableCollection(Arrays.asList(new String[] {
      ROWS, COLS, FILE, NAMES, HELP, SCREEN_WIDTH, SCREEN_HEIGHT, OFFSET,
   }));
   public static final Collection<String> ARGS_WITH_OPTIONS = Collections.unmodifiableCollection(Arrays.asList(new String[] {
      ROWS, COLS, FILE, NAMES, SCREEN_WIDTH, SCREEN_HEIGHT, OFFSET,
   }));
   
   public static final String DEFAULT_NAMES_FILE = getDartboardPath() + "resources/names.txt";
   
   public static String getDartboardPath() {
      if (dartboardPath == null) {
         File currentDir = new File(".").getAbsoluteFile();
         FilenameFilter rootFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
               return name.equalsIgnoreCase("root");
            }
         };
         while (dartboardPath == null && currentDir.getParent() != null) {
            String[] files = currentDir.list(rootFilter);
            if (files != null && files.length == 1) {
               dartboardPath = currentDir.getAbsolutePath();
            }
            currentDir = currentDir.getParentFile();
         }
         if (dartboardPath == null) {
            dartboardPath = "/DynamicDartboard";
         }
         dartboardPath += "/";
      }
      return dartboardPath;
   }

   public static void run(String args[]) {
      StringBuilder usage = new StringBuilder();
      usage.append("Usage:  java DartBoard [options]\n");
      usage.append("  Options:\n");
      usage.append("    -w --width <##> - specify the screen width (full width is default)\n");
      usage.append("    -h --height <##> - specify the screen height (full height is default)\n");
      usage.append("    -f --file <file name> - specify a file to resume a game in progress\n");
      usage.append("    -r --rows <##> - specify the number of rows\n");
      usage.append("    -c --columns <##> - specify the number of columns\n");
      usage.append("    -n --names <file name> - specify a file with the list of names to start from.\n");
      usage.append("    -o --offset <##> - specify the position offset (specify a number or +X to shift by X screen widths).\n");
      usage.append("    -? --help - show this help text.\n\n");
      usage.append(" A dartboard will be started in one of these possible option sets (with the following priority):\n");
      usage.append("    (1) Resume from a previous session (this will ignore the ROWS, COLUMNS, and LIST options).\n");
      usage.append("    (2) Specify the number of ROWS and COLUMNS (LIST is optional).\n");
      usage.append("    (3) Specify the number of ROWS only (LIST is required, columns determined by the list entries).\n");
      usage.append("    (4) Specify the number of COLUMNS only (LIST is required, rows determined by the list entries).\n");
      usage.append("    (5) Specify a LIST file (rows and columns with be determined by the number or list entries).\n");
      try {
         DartBoard dartBoard = null;
         Map<String, String> argMap = parseArgs(args);

         if (argMap.get(HELP) != null) {
            System.out.println(usage.toString());
            return;
         } else if (argMap.get(FILE) != null) {
            dartBoard = DartBoard.load(argMap.get(FILE));
            setScreenDimensions(argMap, dartBoard);
            dartBoard.reInit();
         } else {
            //now we require either rows and columns or a list option
            int rows = getIntArg(ROWS, argMap);
            int cols = getIntArg(COLS, argMap);
            String namesFile = argMap.get(NAMES);
            List<String> names = null;
            try {
               BufferedReader reader = new BufferedReader(new FileReader(namesFile != null ? namesFile : DEFAULT_NAMES_FILE));
               names = new ArrayList<String>();
               if (reader != null) {
                  String lastRead = null;
                  do {
                     lastRead = reader.readLine();
                     if (lastRead != null && !lastRead.equals("")) {
                        names.add(lastRead.trim());
                     }
                  } while (lastRead != null);
               }
            } catch (IOException e) {
               //names = null;
            }
            //make sure minumum args are present
            if (names != null || (rows > 0 && cols > 0)) {
               int numberOfCells = names != null ? names.size() + 1 : rows * cols;
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
               dartBoard = DartBoard.getInstance().create(parent);
               setScreenDimensions(argMap, dartBoard);
               int offset = 0;
               String offsetString = argMap.get(OFFSET);
               try {
                  if (offsetString.charAt(0) == '+') {
                     offset = new BigDecimal(offsetString.substring(1)).multiply(new BigDecimal(dartBoard.getScreenWidth())).intValue();
                  } else {
                     offset = Integer.valueOf(offsetString).intValue();
                  }
               } catch (NumberFormatException e) {
                  offset = 0;
               }
               if (offset < 0) {
                  offset = 0;
               }
               dartBoard.setLocationOffset(offset);
               dartBoard.setSize(dartBoard.getScreenWidth() - 2 * dartBoard.getScreenWidthOffset(), dartBoard.getScreenHeight() - 2 * dartBoard.getScreenHeightOffset() - CommandWindow.HEIGHT);
               dartBoard.setLocation(offset + dartBoard.getScreenWidthOffset(), dartBoard.getScreenHeightOffset());
               dartBoard.setName(DartBoard.DARTBOARD);
               dartBoard.setUserNames(names);
               dartBoard.init(rows, cols);
               dartBoard.start();
               dartBoard.setVisible(true);
               dartBoard.toFront();
            } else {
               System.out.println(usage.toString());
               return;
            }
         }
         dartBoard.setOptions(argMap);
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

   private static Map<String, String> parseArgs(String[] args) {
      Map<String, String> argMap = new HashMap();
      if (args != null) {
         for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
               String argName = null;
               String argValue = null;
               if (args[i].charAt(1) != '-') {
                  //short arg
                  if (args[i].charAt(1) == '?') {
                     argName = HELP;
                  } else {
                     for (String argType : ARG_TYPES) {
                        if (argType.charAt(0) == args[i].charAt(1)) {
                           argName = argType;
                           break;
                        }
                     }
                  }
               } else {
                  //long arg
                  argName = args[i].substring(2);
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

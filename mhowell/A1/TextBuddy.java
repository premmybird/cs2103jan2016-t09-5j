import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

public class TextBuddy{
  //Standard console response strings
  private static final String WELCOME_SCREEN = "\n<Welcome to TextBuddy by Morgan Howell!>\n"
                                             + "          ---------------------           \n"
                                             + "Changes will be saved to \"%1$s\"\n"
                                             + "Type \"help\" for a list of available commands\n";
  private static final String HELP_GUIDE = "\n ---------------HELP GUIDE---------------\n"
                                         + "  |                                        |\n" 
                                         + "  | add ANY_TEXT_CAN_FOLLOW: adds text     |\n"
                                         + "  | delete LINE_NUMBER: removes that entry |\n"
                                         + "  | clear: removes all entries             |\n"
                                         + "  | display: shows all entries             |\n"
                                         + "  | exit: terminates the program           |\n"
                                         + "   ----------------------------------------\n";
  private static final String DISPLAY_TEMPLATE = "\n------------LIST FOR \"%1$s\"------------\n"
                                               + "%2$s"
                                               + "--------------------------------------------------------\n";
  private static final String IO_EXCEPTION = "\nWe encountered an IOException while attempting to open the given file.\n";
  private static final String UNSUPPORTED_COMMAND = "\nPlease supply a supported command. Issue command 'help' for details.\n";
  private static final String WRONG_FILEOUT = "\nMalformed Request: Supplied parameter does not follow expected format of (*.%1$s).\n";
  private static final String WRONG_NUM_PARAMS = "\nMalformed Request: Please provide the correct number of parameters.\n";
  private static final String SENTENCE_ADDED = "\nAdded to %1$s: \"%2$s\"\n";
  private static final String BLANK_LINE_ATTEMPT = "\nA blank line was added to %1$s. Please consider adding something more meaningful next time.\n";
  private static final String SENTENCE_DELETED = "\nDeleted from %1$s: \"%2$s\"\n";
  private static final String MEMORY_CLEARED = "\nAll content cleared from %1$s.\n";
  private static final String CURRENTLY_CLEARED = "\nAll content cleared from %1$s.\n";
  private static final String EXIT = "\nThank you for using TextBuddy by Morgan Howell!"
                                   + "Your additions were saved to %1$s";
  private static final String COMMAND_PROMPT = "%1$s>$ "
  //Add supported extensions for output file below (regex tested).
  private static final String SUPPORTED_EXTENSIONS = "txt|md|rtf";
  private static final String REGEX_EXTENSION_TEST = "\\w+\\.(" + SUPPORTED_EXTENSIONS + ")";

  private enum CommandIssue  = {
    HELP, ADD_ITEM, DELETE_ITEM, CLEAR, DISPLAY, UNSUPPORTED, EXIT;
  } 

  private List<String> data_list;
  private File file_out;
  private String file_name;
  private int item_count;

  public static void main(String[] args){
    TextBuddy helper = TextBuddy.generateBuddyHelper();
    Scanner sys_in = new Scanner(System.in);
    String next_line = null;
    do {
      System.out.print(text_out + ">$ ");
      next_line = sys_in.nextLine();
      String[] input = next_line.split("\\s+");

      if( input.length>0) {
        String command = input[0];
        switch(command) {

          case "add":
            String display;
            if(input.length>1) {
              String[] sentence_portion = Arrays.copyOfRange(input, 1, input.length);
              String sentence_parsed = String.join(" ",sentence_portion);
              display = buddy.addText(sentence_parsed);
            }
            else
            {
              display = buddy.addText(null);
            }
            System.out.println(display);
            break;

          case "display":
            display = buddy.getText();
            System.out.println(display);
            break;

          case "delete":
            int target_index = -1;
            try
            {
              target_index = Integer.parseInt(input[1]);
            }
            catch(NumberFormatException err)
            {
              System.out.println("\nPlease supply a number to be deleted.\n");
              continue;
            }

            if(input.length == 2 && target_index > 0 && buddy.getNumberOfItems() >= target_index)
            {
              display = buddy.deleteLine(target_index);
              System.out.println(display);
            }
            else
            {
              System.out.println("\nPlease supply number representing line to be deleted with the 'delete' command.\n");
            }
            break;

          case "clear":

            if(buddy.getNumberOfItems() > 0)
            {
              buddy.clearText();
              System.out.println("\nall content deleted from " + buddy.getFileName() + "\n");
            }
            else
            {
              System.out.println("\nGiven file has already been cleared.");
            }
            break;

          case "help":
            System.out.println(help_guide);
            break;
          case "exit":
            System.out.println("\nThank you for using TextBuddy by Morgan Howell!");
            System.out.println("Your additions were saved to " + text_out);
            break;
          default:
            System.out.println("\nPlease supply a supported command. Issue command 'help' for details.\n");
            break;
        }
      }

    } while(!next_line.equals("exit"));

    sys_in.close();
  }

  //Factory design pattern style object generator for instantiation and object setup
  public static TextBuddy generateBuddyHelper(String fileName) {
    try {
      sanitizedFileOut = sanitizeArgs(fileName);
      TextBuddy helper = new TextBuddy(sanitizedFileOut);
      helper.restoreInMemory();
      return helper;
    } catch (UnsupportedOperationException err) {
      System.out.println(err.getMessage());
    } catch (IOException err) {
      System.out.println(IO_EXCEPTION);
    }
    return null;
  }

  //Throws error upon malformed parameter types
  public static String sanitizeArgs(String[] args) throws UnsupportedOperationException {
    if (args.length == 1) {
      //Validate given parameter as expected text file format
      String unsanitizedFile = args[0];
      Pattern pattern = Pattern.compile("\\w+\\.("+ TextBuddy.SUPPORTED_EXTENSIONS + ")");
      Matcher match = pattern.matcher(unsanitizedFile);
      boolean isValidDataSource = match.matches();
      if(isValidDataSource) {
        return unsanitizedFile;
      } else {
        throw new UnsupportedOperationException(String.format(WRONG_FILEOUT, SUPPORTED_EXTENSIONS));
      }
    } else {
      throw new UnsupportedOperationException(WRONG_NUM_PARAMS);
    }
  }

  public static CommandIssue mapUserInputToCommand(String userInput) {
    CommandIssue command;
    switch (userInput) {
      case "add":
        command = CommandIssue.ADD_ITEM;
        break;

      case "display":
        command = CommandIssue.DISPLAY;
        break;

      case "delete":
        command = CommandIssue.DELETE_ITEM;
        break;

      case "clear":
        command = CommandIssue.CLEAR;
        break;

      case "help":
        command = CommandIssue.HELP;
        break;

      case "exit":
        command = CommandIssue.EXIT;
        break;

      default:
        command = CommandIssue.UNSUPPORTED;
        break;
    }

    return command;
  }

  public TextBuddy(String file_name) {
    this.file_name = file_name;
    file_out = new File(file_name);
    data_list = new ArrayList<String>();
  }

  public String addText(String sentence) {
    String display;
    if(sentence != null) {
      data_list.add(sentence);
      display = "\nAdded to " + file_name + ": \"" + sentence + "\"\n";
    } else {
      data_list.add(" ");
      display = ;
    }
    item_count++;
    return display;
  }

  public String clearText() {
    data_list.clear();
    item_count=0;
    return "\nAll content cleared from " + file_name + ".\n";
  }

  public String getText() {
    String display="";
    if(!data_list.isEmpty()){
      display += "------------LIST FOR \"" + file_name + "\"------------";
      for(int i=0; i<data_list.size(); i++)
      {
        display += "\n" + (i+1) + ". " + data_list.get(i) + "\n";
      }
      display += "--------------------------------------------------------\n";
    }
    else {
      display = "\n" + file_name + " is empty.\n";
    }
    return display;
  }
  
  public String deleteLine(int line_number) {
    String line = data_list.remove(line_number-1);
    return "\nDeleted from " + file_name + ": \"" + line + "\"\n";  
  }

  public String getFileName() {
    return file_name;
  }

  public int getNumberOfItems() {
    return item_count;
  }

  public void restoreInMemory() throws IOException {
    if(file_out.exists()) {
      //stream in and stream out are the same in basic version
      try(BufferedReader br = new BufferedReader(new FileReader(file_out))) {
        for(String line; (line = br.readLine()) != null;) {
            //transfer data from text file during object initialization
            addText(line);
        }
      }
    }
  }

  public void closeAndSavePersistent() throws Throwable {
    if(item_count>0) { 
      try(PrintWriter writer = new PrintWriter(file_name, "UTF-8")) {
        for(String item : data_list) {
          writer.println(item);
        }
      }
    }
    writer.close();
  }





}


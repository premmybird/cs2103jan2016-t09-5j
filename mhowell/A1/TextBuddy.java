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
  private List<String> data_list;
  private File file_out;
  private String file_name;
  private int item_count;
  //Add supported extensions for output file below.
  static String SUPPORTED_EXTENSIONS = "txt|md|rtf";

  //Factory design pattern for necessary setup, such as restoring list in memory.
  public static TextBuddy generateBuddyHelper(String file_name) throws IOException{
    TextBuddy helper = new TextBuddy(file_name);
    //helper.restoreInMemory();
    return helper;
  }

  public TextBuddy(String file_name)
  {
    this.file_name = file_name;
    this.file_out = new File(file_name);
    this.data_list = new ArrayList<String>();
  }

  public String addText(String sentence)
  {
    String display;
    if(sentence != null)
    {
      data_list.add(sentence);
      display = "\nAdded to " + file_name + ": \"" + sentence + "\"\n";
    }
    else
    {
      data_list.add(" ");
      display = "\nA blank line was added to" + file_name + ". Please consider adding something more meaningful next time.\n";
    }
    item_count++;
    return display;
  }

  public String clearText()
  {
    data_list.clear();
    item_count=0;
    return "\nAll content cleared from " + file_name + ".\n";
  }

  public String getText()
  {
    String display="";
    if(!data_list.isEmpty()){
      display += "------------LIST FOR \"" + file_name + "\"------------";
      for(int i=0; i<data_list.size(); i++)
      {
        display += "\n" + (i+1) + ". " + data_list.get(i) + "\n";
      }
      display += "--------------------------------------------------------\n";
    }
    else
    {
      display = "\n" + file_name + " is empty.\n";
    }
    return display;
  }
  
  public String deleteLine(int line_number)
  {
    String line = data_list.remove(line_number-1);
    return "\nDeleted from " + file_name + ": \"" + line + "\"\n";  
  }

  public String getFileName()
  {
    return file_name;
  }

  public int getNumberOfItems()
  {
    return item_count;
  }

  public void restoreInMemory() throws IOException
  {
    //stream in and stream out are the same in basic version
    try(BufferedReader br = new BufferedReader(new FileReader(file_out))) 
    {
      for(String line; (line = br.readLine()) != null; ) 
      {
          //transfer data from text file during object initialization
          addText(line);
      }
    }
  }

  public void closeAndSavePersistent() throws Throwable
  {


  }


  public static String sanitizeArgs(String[] args) throws UnsupportedOperationException{
    //Throw error upon malformed parameter types
    if(args.length == 1)
    {
      //Validate given parameter as expected text file format
      String input = args[0];
      Pattern p = Pattern.compile("\\w+\\.("+ TextBuddy.SUPPORTED_EXTENSIONS + ")");
      Matcher m = p.matcher(input);
      boolean isValidDataSource = m.matches();
  
      if(isValidDataSource){
        return input;
      }
      else
      {
        throw new UnsupportedOperationException("\nMalformed Request: Supplied parameter does not follow expected format of (*.txt).\n");
      }

    }
    else 
    {
      throw new UnsupportedOperationException("\nMalformed Request: Please provide the correct number of parameters.\n");
    }
  
  }

  public static void main(String[] args){
    String text_out;
    TextBuddy buddy;
    try
    {
      text_out = sanitizeArgs(args);
      buddy = generateBuddyHelper(text_out);
      System.out.println("\n<Welcome to TextBuddy by Morgan Howell!>");
      System.out.println("          ---------------------           ");
      System.out.println("Changes will be saved to \"" + text_out +"\"");
      System.out.println("Type \"help\" for a list of available commands\n");
      
    }
    catch(UnsupportedOperationException err)
    {
      System.out.println(err.getMessage());
      return;
    }
    catch(IOException err)
    {
      System.out.println("We encountered an IOException while attempting to open the given file.");
      return;
    }

    Scanner sys_in = new Scanner(System.in);
    String next_line = null;
    do 
    {
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
            String help_guide = "\n  ---------------HELP GUIDE---------------\n";
            help_guide       += " |            LIST OF COMMANDS            |\n";
            help_guide       += " |                                        |\n";
            help_guide       += " | add ANY_TEXT_CAN_FOLLOW: adds text     |\n";
            help_guide       += " | delete LINE_NUMBER: removes that entry |\n";
            help_guide       += " | clear: removes all entries             |\n";
            help_guide       += " | display: shows all entries             |\n";
            help_guide       += " | exit: terminates the program           |\n";
            help_guide       += "  ----------------------------------------\n";
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

}


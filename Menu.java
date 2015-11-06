package userCommunication;
/**
 * Used to manage printing a menu of choices and getting the proper selected choice from the user
 * @author Jordan Kidney
 * @version 4.0
 * 
 * Last Modified:  Jan 11, 2014 - Complete redesign to make it easier to use
 *                 Aug 18, 2014 - Added new features (Jordan Kidney)
 *                 Feb 18, 2014 - Created (Jordan Kidney)
 *               
 */
import java.util.*;
public class Menu 
{
    public static final String DEFAULT_MENU_HEADER = "Menu: ";

    private ConsoleInteraction comm;
    private HashMap<String, String> seenOptions;
    private boolean clearOnPrintMenu;
    private ArrayList<String> menuChoices;

    /**
     * Used to set access to a specific input
     * @param comm the specific input object to use for all console interaction 
     */
    public Menu(ConsoleInteraction comm)
    {
        this.comm = comm;
        menuChoices = new ArrayList<String>();
        seenOptions = new HashMap<String, String>();
        clearOnPrintMenu = false;
    }

    /**
     * Default Constructor
     */
    public Menu()
    {
        this( ConsoleInteraction.getInstance() );
    }

    /**
     * used to turn on/off the clear terminal when the menu is printed
     */
    public void clearWhenMenuIsPrinted(boolean value)
    {
        clearOnPrintMenu = value;  
    }

    /**
     * Adds a menu option that the user can select. This method does not 
     * check for duplicate choices/options
     * 
     * @param option the option object to add to the possible choices 
     */
    public void addMenuOption(String option)
    {
        if(seenOptions.containsKey(option.toUpperCase())) 
            throw new RuntimeException("Duplicate menu option added: " + option);

        menuChoices.add(option); 
        seenOptions.put(option.toUpperCase(), option);
    }

    /**
     * Presents the user with a text based menu interface based upon all the options that have been 
     * added to this object before the call to this method. Steps the method goes through:
     *  
     *  (1) Prints out all the options to the user, one per line with a number associated with each option. 
     *         ( this number = The options index number in the array list + 1)
     *  (2) Asks the user which option they would like
     *  (3) Verifies if the user has entered a correct choice, if incorrect it goes back to step 1
     *  (4) Returns the the number entered by the user       
     * @return the selected option (based upon the options that have been given to the user)
     */
    public int getChoice()
    {
        return getChoice(DEFAULT_MENU_HEADER);
    }

    /**
     * Presents the user with a text based menu interface based upon all the options that have been 
     * added to this object before the call to this method. Steps the method goes through:
     *  
     *  (1) Prints out all the options to the user, one per line with a number associated with each option. 
     *         ( this number = The options index number in the array list + 1)
     *  (2) Asks the user which option they would like
     *  (3) Verifies if the user has entered a correct choice, if incorrect it goes back to step 1
     *  (4) Returns the the number entered by the user       
     * @param header the message to display above the menu
     * @return the selected option number
     * @throws RuntimeException if no menu options have been added before this method was called or an unknown error has occurred
     */
    public int getChoice(String header)
    {
        boolean end = false;
        int userChoice = -1;

        if(menuChoices.isEmpty())
            throw new RuntimeException("No options have been added to the menu");

        String message = "Enter choice (from 1 to " + menuChoices.size() +") : ";
        if(clearOnPrintMenu) comm.clearBlueJTerminal();

        printMenuOptions(header);

        while(!end)
        {
            try
            {
                userChoice = comm.getInput_IntBetween(message,1, menuChoices.size());
                end = true;
            }
            catch(InputMismatchException inputError)
            {
                comm.println_error("Non-numeric value entered");
                comm.clearInputLine();
            }
            catch(Exception error)
            {
                comm.println_error("An unknown error has occured");
                error.printStackTrace();
                throw new RuntimeException("Unknown error caught - " + error.getMessage());
            }
        }

        return userChoice;
    }

    /**
     * Presents the user with a text based menu interface based upon all the options that have been 
     * added to this object before the call to this method. Steps the method goes through:
     *  
     *  (1) Prints out all the options to the user, one per line with a number associated with each option. 
     *         ( this number = The options index number in the array list + 1)
     *  (2) Asks the user which option they would like
     *  (3) Verifies if the user has entered a correct choice, if incorrect it goes back to step 1
     *  (4) Returns the the option string for the chosen value entered by the user       
     * @return the selected option string
     * @throws RuntimeException if no menu options have been added before this method was called or an unknown error has occurred
     */
    public String getChoice_string()
    {
        return getChoice_string(DEFAULT_MENU_HEADER);
    }

    /**
     * Presents the user with a text based menu interface based upon all the options that have been 
     * added to this object before the call to this method. Steps the method goes through:
     *  
     *  (1) Prints out all the options to the user, one per line with a number associated with each option. 
     *         ( this number = The options index number in the array list + 1)
     *  (2) Asks the user which option they would like
     *  (3) Verifies if the user has entered a correct choice, if incorrect it goes back to step 1
     *  (4) Returns the the option string for the chosen value entered by the user       
     * @param header the message to display above the menu
     * @return the selected option string
     * @throws RuntimeException if no menu options have been added before this method was called or an unknown error has occurred
     */
    public String getChoice_string(String header)
    {
        int choice = getChoice(header);
        return menuChoices.get(choice-1);
    }

    /**
     * Prints all menu options to the console
     *  @param header the message to display above the menu
     */
    private void printMenuOptions(String header)
    {
        comm.println(header);
        //print out all options
        for(int index=0;index < menuChoices.size(); index++)
        {
            String option = menuChoices.get(index);
            comm.printlnFormat("%3d: %s", (index+1),option);
        }
    }

    /**
     * Gives the number of options currently in this menu
     * @return the number of options currently in the menu
     */
    public int numOptions() { return menuChoices.size(); }

    /**
     * Gets the requested menu option at the given choice location
     * @param index the choice index for the requested option
     * @return the selected option
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 1 || index > numOptions())
     */
    public String getOptionAt(int index) { return menuChoices.get(index-1); }

    /**
     * Returns the choice index of the requested option (this operation is case sensitive)
     * @param option the to get the choice index for
     * @return -1 if the option is not found, otherwise the choice index is returned
     */
    public int optionChoiceNumber(String option) 
    { 
        int choiecIndex = menuChoices.indexOf(option);

        if(choiecIndex >= 0) choiecIndex++;

        return choiecIndex;
    }

    /**
     * Removes all current menu options 
     */
    public void removeAllOptions() { menuChoices.clear(); }
}


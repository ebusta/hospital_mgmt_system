import hospitalThings.*;
import userCommunication.*;
import java.io.*;
import java.util.*;

/**
 * This is where everything in the program runs from. Contains the main loop.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class Run
{
    private Menu menu;
    private ConsoleInteraction cI;
    private Login login;
    private Hospital hospital;
    private FileManager fM;
    private boolean userIsAdmin;

    private static final int   LIST_PATIENTS = 1;
    private static final int     ADD_PATIENT = 2;
    private static final int  REMOVE_PATIENT = 3;
    private static final int     SEARCH_BEDS = 4;
    private static final int GENERATE_REPORT = 5;
    private static final int      QUIT_BASIC = 6;

    private static final int      LIST_USERS = 1;
    private static final int        ADD_USER = 2;
    private static final int     REMOVE_USER = 3;
    private static final int      QUIT_ADMIN = 4;
    
    private static final int       THIS_YEAR = 2015;

    /**
     * Boots up the whole program.
     */
    public void start()
    {
        cI = new ConsoleInteraction();        
        menu = new Menu(cI);
        login = new Login(cI);
        fM = new FileManager(cI, login);
        hospital = new Hospital(cI);

        try
        {            
            fM.readUserFile();
            String username = cI.getInput_String("Please enter your username (case sensitive): ");
            String password = cI.getInput_String("Please enter your password (case sensitive): ");
            if (login.userLogin(username,password))
            {
                userIsAdmin = login.checkAdmin();
                buildMenu();
                if (!userIsAdmin)
                    fM.readDataFile(hospital);
                mainLoop();
            }
        }
        catch (Exception e)
        {cI.println_error("Unable to open file! Goodbye."); }
    }

    private void welcome()
    {
        cI.println("Welcome to Lord Pratt's Magical Infirmary Management System!");
        cI.println("");
    }

    /**
     * Builds the menu to be used by the whole program.
     * Will be different if the user is basic or admin.
     */
    private void buildMenu()
    {
        if (userIsAdmin)
        {
            menu.addMenuOption("List all users");
            menu.addMenuOption("Add new user");
            menu.addMenuOption("Remove a user");
        }
        else
        {
            menu.addMenuOption("List all current patients");
            menu.addMenuOption("Add new patient");
            menu.addMenuOption("Remove a patient");
            menu.addMenuOption("Search for open beds");
            menu.addMenuOption("Generate hospital report");
        }
        menu.addMenuOption("Quit");
    }

    /**
     * Main functionality of the program.
     */
    private void mainLoop() throws FileNotFoundException
    {
        boolean done = false;
        int userChoice = -1;
        do {
            cI.clearBlueJTerminal();
            userChoice = menu.getChoice("What do you want to do?");
            if (userIsAdmin)
                done = doUserChoiceAdmin(userChoice, done);
            else 
                done = doUserChoiceBasic(userChoice, done);
            if (!done)
                cI.pause();
        } while (!done);
        if (userIsAdmin)
            login.saveUsers();
        else
            fM.writeFile(hospital);
            
        cI.print("Thank you, goodbye!");
    }

    /**
     * Processes what the user has decided to do.
     * @param userChoice The numeric representation of the users choice (see static variables)
     * @param done Whether or not the user is finished the program yet
     * @return Returns whether or not the program is finished (the user has chosen to quit). 
     */
    private boolean doUserChoiceBasic(int userChoice, boolean done) throws FileNotFoundException
    {
        switch (userChoice)
        {
            case LIST_PATIENTS: listPatients(); break;
            case ADD_PATIENT: addPatient(); break;
            case REMOVE_PATIENT: removePatient(); break; 
            case SEARCH_BEDS: openBedReport(); break;
            case GENERATE_REPORT: generateReport(); break;
            case QUIT_BASIC: done = cI.confirm("Are you sure you want to quit? "); break;
        }
        return done;
    }

    /**
     * Processes what the user has decided to do.
     * @param userChoice The numeric representation of the users choice (see static variables)
     * @param done Whether or not the user is finished the program yet
     * @return Returns whether or not the program is finished (the user has chosen to quit). 
     */
    private boolean doUserChoiceAdmin(int userChoice, boolean done) throws FileNotFoundException
    {
        switch (userChoice)
        {
            case LIST_USERS: listUsers(); break;
            case ADD_USER: addNewUser(); break;
            case REMOVE_USER: removeUser(); break;
            case QUIT_ADMIN: done = cI.confirm("Are you sure you want to quit? "); break;
        }
        return done;
    }

    private void listPatients()
    {
        cI.println("\n   Patient Name | Age | Room # | Room Name | Bed # | Infected?");
        cI.println("   -----------------------------------------------------------");
        hospital.listPatients();
        cI.println("   -----------------------------------------------------------");
        cI.println("");
    }

    private void addPatient()
    {
        String newPatientName = cI.getInput_String("Please input the patient's full name: ");
        int newPatientAge = cI.getInput_Int("Please input the patient's age: ");
        boolean infected = cI.confirm("Is the patient infected? ");
        hospital.addPatient(newPatientName,newPatientAge,infected);
    }

    private void removePatient()
    {
        int roomNumber = cI.getInput_Int("What room number is the patient in? ");
        int bedNumber = cI.getInput_Int("What bed number is the patient in? ");
        hospital.removePatient(roomNumber,bedNumber);
    }

    private void openBedReport()
    {
        cI.println("The following beds are empty:\n");
        cI.println("Bed # | Room # | Room Name ");
        cI.println("--------------------------");
        int openBedCount = hospital.openBedReport();
        if (openBedCount == 0)
            cI.println_error("There are no open beds left! ");
        else
        {
            cI.println("--------------------------\n");
            cI.println("There are " + openBedCount + " beds remaining in the hospital.\n");
        }
    }

    private void generateReport()
    {
        String fileName = cI.getInput_String("Please enter the the file name for the report: ");
        fileName += ".txt";
        int numPatients = hospital.getNumPatients();
        int numInfected = hospital.getNumInfected();
        try {fM.generateReport(fileName,numPatients,numInfected,hospital);}
        catch (Exception e)
        {cI.println_error("Could not write file."); }
    }

    private void listUsers()
    {
        cI.println("\n      Full Name | Years Employed | Admin? | Zombie Killing Skill Level |    Username | Password");
        cI.println("-----------------------------------------------------------------------------------------------");
        login.listUsers();
        cI.println("-----------------------------------------------------------------------------------------------\n");
    }

    private void addNewUser()
    {
        boolean isAdmin = cI.confirm("Is the new user an administrator? ");
        boolean found = false;
        String username;
        String fullName = cI.getInput_String("Please enter new user's full name: ");
        do {
            username = cI.getInput_String("Please enter new user's username: ");
            found = login.validateUserName(username);
            if (found)
                cI.println_error("This username already exists in the system!");
        } while (found);
        String password = cI.getInput_String("Please enter new user's password: ");
        int yearsEmployed = THIS_YEAR - cI.getInput_IntBetween("When was this user hired? ",0,THIS_YEAR);
        login.addNewUser(isAdmin,username,password,fullName,yearsEmployed);
    }

    private void removeUser()
    {
        String username = cI.getInput_String("Please enter the username of the user to be removed: ");
        login.removeUser(username);
    }
}

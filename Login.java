import java.util.*;
import userCommunication.*;
import java.io.*;

/**
 * Responsible for the whole login process, as well as saving user information.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class Login
{
    private ArrayList<User> validUser;
    private ConsoleInteraction cI;
    private int currentUserIndex = -1;
    private static final int THIS_YEAR = 2015;
    
    /**
     * Constructor for the login system. Creates the ArrayList used to manage all users.
     * @param cI The console interaction object used to display messages.
     */
    public Login(ConsoleInteraction cI)
    {
        validUser = new ArrayList<User>();
        this.cI = cI;
    }
    
    /**
     * Adds a new User object to the ArrayList. Used on startup.
     * @param user The new User to be added.
     */
    public void addUser(User user)
    {
        validUser.add(user);
    }
    
    /**
     * The login process that gets run when the user starts the whole program.
     * @return Returns whether or not the user has properly logged in.
     */
    public boolean userLogin(String username, String password)
    {
        boolean validLogin = false;
        if (checkUser(username))
        {
            if (checkPassword(password))
            {
                validLogin = true;
                String fullName = validUser.get(currentUserIndex).getFullName();
                cI.println("Welcome, " + fullName + "!");
                cI.pause();
            }
            else
                cI.print("Sorry, this is not a valid password. Goodbye!");
        }
        else
            cI.print("Sorry, this is not a valid username. Goodbye!");
        return validLogin;
    }
    
    /**
     * Checks to see if the username entered is in fact in the ArrayList of users
     * @param username The username being checked
     * @return Will return true if the username is already in the list of users
     */
    private boolean checkUser(String username)
    {
        boolean isValid = false;
        for (int index = 0; index < validUser.size(); index++)
        {
            String realUserName = validUser.get(index).getUserName();
            if (username.equals(realUserName))
            {
                isValid = true;
                currentUserIndex = index;
                break;
            }
        }
        return isValid;
    }
    
    /**
     * Checks to see if the password entered matches with the username being used to login
     * @param password The password being checked
     * @return Will return true if the password is correct
     */
    private boolean checkPassword(String password)
    {
        boolean isValid = false;
        String realPassword = validUser.get(currentUserIndex).getPassword(); 
        if (password.equals(realPassword))
            isValid = true;
        return isValid;
    }
    
    /**
     * Checks if the current user is an administrator or not.
     * @return Will return true if the current user is an administrator
     */
    public boolean checkAdmin()
    {
        boolean isAdmin = false;
        if (validUser.get(currentUserIndex).checkAdmin())
            isAdmin = true;
        return isAdmin;
    }
    
    /**
     * Lists out all the current users in the list in nicely formatted columns.
     */
    public void listUsers()
    {
        for (int index = 0; index < validUser.size(); index++)
        {
            User thisUser = validUser.get(index);
            String fullName = thisUser.getFullName();
            String username = thisUser.getUserName();
            String password = thisUser.getPassword();
            boolean isAdmin = thisUser.checkAdmin();
            int yearsEmployed = thisUser.getYearsEmployed();
            if (isAdmin)
            {
                String adminStatus = "Yes";
                String zomKillSkill = "N/A";
                cI.printlnFormat("%15s |%15d |%7s |%27s |%12s | %s",fullName,yearsEmployed,adminStatus,zomKillSkill,username,password);
            }
            else
            {
                String adminStatus = "No";
                int zomKillSkill = thisUser.getZomKillSkill();
                cI.printlnFormat("%15s |%15d |%7s |%27d |%12s | %s",fullName,yearsEmployed,adminStatus,zomKillSkill,username,password);
            }
        }
    }
    
    /**
     * Adds a new user to the list. Used only during the program by an administrator.
     * Will prompt for the new users information.
     */
    public void addNewUser(boolean isAdmin, String username, String password, String fullName, int yearsEmployed)
    {
        if (isAdmin)
        {
            User newUser = new User(username,password,fullName,yearsEmployed);
            addUser(newUser);
        }
        else
        {
            int zomKillSkill = cI.getInput_Int("Please enter new user's zombie killing skill level: ");
            User newUser = new User(username,password,fullName,yearsEmployed,zomKillSkill);
            addUser(newUser);
        }
    }
    
    /**
     * Called by the addNewUser method. Differs from checkUser() in that this one doesn't care about capitalizaion.
     * @param username The username being searched for
     * @return Will return true if the username already exists in the system.
     */
    public boolean validateUserName(String username)
    {
        boolean found = false;
        for (int index = 0; index < validUser.size(); index++)
        {
            if ( username.equalsIgnoreCase( validUser.get(index).getUserName() ) )
                found = true;
        }
        return found;
    }
    
    /**
     * Called by the removeUser method. Checks if a username exists in the system.
     * @param username The username being searched for.
     * @return Returns the index in the user list this current username was found at. If the name wasn't found, returns -1 which is handled
     * by the removeUser method
     */
    private int findUserName(String username)
    {
        int index;
        boolean found = false;
        for (index = 0; index < validUser.size(); index++)
        {
            if ( username.equalsIgnoreCase( validUser.get(index).getUserName() ) )
            {
                found = true;
                break;
            }
        }
        if (found)
            return index;
        else 
            return -1;
    }
    
    /**
     * Removes a user from the list. Can only be called by administrator accounts.
     */
    public void removeUser(String username)
    {
        int userIndex = findUserName(username);
        String currentUserName = validUser.get(currentUserIndex).getUserName();
        if (userIndex != -1 && userIndex != currentUserIndex)
        {
            User toRemove = validUser.get(userIndex);
            cI.println(toRemove.fullInfo());
            boolean remove = cI.confirm("Are you sure you want to remove this user? ");
            if (remove)
            {
                validUser.remove(userIndex);
                cI.println("User " + username + " has been removed. ");
                currentUserIndex = findUserName(currentUserName);
            }
        }
        else if (userIndex == currentUserIndex)
            cI.println_error("You can't remove yourself from the system! ");
        else
            cI.println_error("This user does not exist!");
    }
   
    /**
     * Saves the finalized list of users on shutdown.
     */
    public void saveUsers() throws FileNotFoundException
    {
        PrintStream outFile = new PrintStream( new FileOutputStream("Users.txt") );
        int numUsers = validUser.size();
        outFile.println(numUsers);
        for (int index = 0; index < numUsers; index++)
        {
            User thisUser = validUser.get(index);
            String username = thisUser.getUserName();
            String password = thisUser.getPassword();
            boolean isAdmin = thisUser.checkAdmin();
            String fullName = thisUser.getFullName();
            int yearHired = THIS_YEAR - thisUser.getYearsEmployed();
            if (isAdmin)
            {
                outFile.println(username+","+password+",A,"+fullName+","+yearHired);
            }
            else
            {
                int zomKillSkill = thisUser.getZomKillSkill();
                outFile.println(username+","+password+",E,"+fullName+","+yearHired+","+zomKillSkill);
            }
        }
    }
}

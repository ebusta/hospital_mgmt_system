**
 * Represents users in the system.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class User
{
    private String username;
    private String password;
    private String fullName;
    
    private int yearsEmployed;
    private int zomKillSkill;
    private boolean isAdmin;
    
    /**
     * Constructor for basic users. 
     * @param username The users username
     * @param password The users password
     * @param fullName The actual name of the user
     * @param yearsEmployed How long the user has been employed for
     * @param zomKillSkill The users zombie killing skill level
     */
    public User(String username, String password, String fullName, int yearsEmployed, int zomKillSkill)
    {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.yearsEmployed = yearsEmployed;
        this.zomKillSkill = zomKillSkill;
        isAdmin = false;
    }
    
    /**
     * Constructor for administrators.
     * @param username The users username
     * @param password The users password
     * @param fullName The actual name of the user
     * @param yearsEmployed How long the user has been employed for
     */
    public User(String username, String password, String fullName, int yearsEmployed)
    {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.yearsEmployed = yearsEmployed;
        isAdmin = true;
    }
    
    public String getUserName()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }    
    public String getFullName()
    {
        return fullName;
    }    
    public int getYearsEmployed()
    {
        return yearsEmployed;
    }    
    public int getZomKillSkill()
    {
        return zomKillSkill;
    }    
    public boolean checkAdmin()
    {
        return isAdmin;
    }
    
    /**
     * Returns a nice string with all the users information.
     */
    public String fullInfo()
    {
        String fullInfo = "";
        if (isAdmin)
        {
            fullInfo += "User: "+fullName+". Username: "+username+", password: "+password+". Employed for "+yearsEmployed+" years. ADMINISTRATOR";
        }
        else
        {
            fullInfo += "User: "+fullName+". Username: "+username+", password: "+password+". Employed for "+yearsEmployed+" years, zombie killing skill level: "+zomKillSkill+". BASIC USER";
        }
        return fullInfo;
    }
    
    /* A bunch of setters that weren't needed for the program. Might be handy for further implementation.
    public void setUserName(String newName)
    {
        username = newName;
    }
    public void setPassword(String newPWord)
    {
        password = newPWord;
    }
    public void setFullName(String newName)
    {
        fullName = newName;
    }
    public void setYearsEmployed(int newYrsEmpl)
    {
        yearsEmployed = newYrsEmpl;
    }
    public void setZomKillSkill(int newSkillLevel)
    {
        zomKillSkill = newSkillLevel;
    }
    public void changeAdminStatus(boolean currentUserStatus)
    {
        if (currentUserStatus)
            isAdmin = !isAdmin;
    }*/
}

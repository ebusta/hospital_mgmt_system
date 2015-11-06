package hospitalThings;

public class Patient
{
    private String name;
    private int age;
    private boolean isInfected; //necessary?
    private int roomNumber; //necessary?
    private int bedNumber; //necessary?
    private String infectedStatus;
    
    public Patient(String name,int age,String infectedStatus,int roomNumber,int bedNumber)
    {
        this.name = name;
        this.age = age;
        this.isInfected = isInfected;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
        this.infectedStatus = infectedStatus;
        switch (infectedStatus)
        {
            case "INFECTED": isInfected = true; break;
            case "CLEAN" : 
            default: isInfected = false; break;
        }
    }
    
    public Patient(String name,int age,boolean isInfected,int roomNumber,int bedNumber)
    {
        this.name = name;
        this.age = age;
        this.isInfected = isInfected;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
        if (isInfected)
            infectedStatus = "INFECTED";
        else
            infectedStatus = "CLEAN";
    }
    
    public String getPatientName()
    {
        return name;
    }
    
    public int getPatientAge()
    {
        return age;
    }
    
    public String getInfectedStatus()
    {
        return infectedStatus;
    }
    
    public boolean isInfected()
    {
        return isInfected;
    }
    
    /* Might be useful in further implementation
     
     public void changeInfectedStatus()
    {
        if (isInfected)        
            infectedStatus = "CLEAN";
        else
            infectedStatus = "INFECTED";
            
        isInfected = !isInfected;        
    }*/
}

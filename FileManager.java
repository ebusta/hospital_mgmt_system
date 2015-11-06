import hospitalThings.*;
import java.util.*;
import java.io.*;
import userCommunication.*;

/**
 * The class that deals with reading all files in the system.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class FileManager
{
    private Scanner reader;
    private ConsoleInteraction cI;
    private Login login;
    private String hospitalName;
    private static final int THIS_YEAR = 2015;

    public FileManager(ConsoleInteraction cI, Login login)
    {
        this.cI = cI;
        this.login = login;
    }

    /**
     * Reads the file containing all user information
     */
    public void readUserFile() throws FileNotFoundException
    {
        reader = new Scanner( new FileInputStream("Users.txt") );
        int numUsers = reader.nextInt();
        reader.nextLine();
        String line;
        Scanner lineScan;
        
        while(reader.hasNext() )
        {
            String username, password, adminStatus, fullName;
            boolean isAdmin;
            int yearHired, yearsEmployed, zomKillSkill;
            
            line = reader.nextLine();
            lineScan = new Scanner(line);
            lineScan.useDelimiter(",");
            
            username = lineScan.next();
            password = lineScan.next();
            adminStatus = lineScan.next();
            fullName = lineScan.next();
            yearHired = lineScan.nextInt();
            yearsEmployed = THIS_YEAR - yearHired;
            switch (adminStatus)
            {
                case "A": isAdmin = true; break;
                case "E": 
                default : isAdmin = false; break; 
            }
            if (isAdmin)
            {                
                User newUser = new User(username, password, fullName, yearsEmployed);
                login.addUser(newUser);
            }
            else
            {
                zomKillSkill = lineScan.nextInt();
                User newUser = new User(username, password, fullName, yearsEmployed, zomKillSkill);
                login.addUser(newUser);
            }            
        }
        reader.close();
    }

    /**
     * Reads the file containing all the hospital information
     * @param hospital The controller object being used to hold all the hospital data
     */
    public void readDataFile(Hospital hospital) throws FileNotFoundException
    {
        String hospitalName = cI.getInput_String("Please enter hospital file name: ");//should be in RUN
        this.hospitalName = hospitalName;
        reader = new Scanner( new FileInputStream(hospitalName) );//move to RUN
        reader.nextLine();
        String line, subLine;
        Scanner lineScan, subLineScan;

        while (reader.hasNext())
        {
            int roomNumber, bedsInRoom, bedNumber, patientAge, takenCount = 0;
            String roomName, bedStatus, patientName, patientStatus;
            boolean isEmpty, infected;

            line = reader.nextLine();
            lineScan = new Scanner(line);
            lineScan.useDelimiter(",");

            roomNumber = lineScan.nextInt();
            roomName = lineScan.next();
            bedsInRoom = lineScan.nextInt();
            
            Room newRoom = new Room(roomNumber,bedsInRoom,roomName);
            hospital.addRoom(newRoom);
            

            for (int bed = 1; bed <= bedsInRoom; bed++)
            {
                subLine = reader.nextLine();
                subLineScan = new Scanner(subLine);
                subLineScan.useDelimiter(",");

                bedNumber = subLineScan.nextInt();
                bedStatus = subLineScan.next();

                switch (bedStatus)
                {
                    case "TAKEN": 
                        isEmpty = false; 
                        patientName = subLineScan.next(); 
                        patientAge = subLineScan.nextInt();
                        patientStatus = subLineScan.next(); 
                        Patient newPatient = new Patient(patientName,patientAge,patientStatus,roomNumber,bedNumber);
                        Bed fullBed = new Bed(bedNumber,isEmpty,roomNumber);
                        fullBed.setPatient(newPatient);
                        newRoom.addBed(fullBed);//maybe move this above the switch?
                        hospital.addPatient(newPatient);
                        takenCount++; break;
                    case "OPEN":
                    default: 
                        isEmpty = true;
                        Bed openBed = new Bed(bedNumber,isEmpty,roomNumber);
                        newRoom.addBed(openBed);//maybe move this above the switch?
                        break;
                }
            }
        }

    }
    
    /**
     * Saves all the hospital information on shutdown.
     * @param hospital The controller object that contains all the hospital data
     */
    public void writeFile(Hospital hospital) throws FileNotFoundException
    {
        PrintStream outFile = new PrintStream( new FileOutputStream(hospitalName) );
        ArrayList<Room> rooms = hospital.getRoomList();
        int numRooms = rooms.size();
        outFile.println(numRooms);
        String roomLine,bedLine;
        
        for (int indexR = 0; indexR < numRooms; indexR++)
        {
            Room thisRoom = rooms.get(indexR);
            int roomNum = thisRoom.getRoomNumber();
            String roomName = thisRoom.getRoomName();
            int numBeds = thisRoom.getNumBeds();
            outFile.println(roomNum+","+roomName+","+numBeds);
            ArrayList<Bed> beds = thisRoom.getBeds();
            
            for (int indexB = 0; indexB < numBeds; indexB++)
            {
                Bed thisBed = beds.get(indexB);
                int bedNum = thisBed.getBedNumber();
                boolean isEmpty = thisBed.isEmpty();
                if (isEmpty)
                    outFile.println(bedNum+",OPEN");
                else
                {
                    Patient thisPatient = thisBed.getPatient();
                    String patientName = thisPatient.getPatientName();
                    int patientAge = thisPatient.getPatientAge();
                    String patientStatus = thisPatient.getInfectedStatus();
                    outFile.println(bedNum+",TAKEN,"+patientName+","+patientAge+","+patientStatus);
                }
            }
            
        }
    }

    /**
     * Writes a report on all the patients in the hospital.
     * @param fileName The name of the file to be written to
     * @param numPatients The number of patients currently in the hospital
     * @param numInfected How many patients are currently infected
     * @param hospital The controller object that contains more data on the hospital.
     */
    public void generateReport(String fileName, int numPatients, int numInfected, Hospital hospital) throws FileNotFoundException
    {
        PrintStream outFile = new PrintStream( new FileOutputStream(fileName) );
        outFile.println("          --------------INFIRMARY REPORT--------------");
        outFile.println();
        outFile.println(" Current patient total : " + numPatients);
        outFile.println(" Infected patient total: " + numInfected);
        outFile.println();        
        outFile.println("   Patient Name | Age | Room # | Room Name | Bed # | Infected?");
        outFile.println("   -----------------------------------------------------------");
        hospital.printPatients(outFile);
        outFile.println("   -----------------------------------------------------------");
    }
}

package hospitalThings;

import java.util.*;
import userCommunication.ConsoleInteraction;
import java.io.*;

/**
 * The controller object that holds all data on the hospital.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class Hospital
{
    private ArrayList<Room> rooms;
    private ConsoleInteraction cI;
    private int numPatients = 0;
    private int numInfected = 0;
    
    /**
     * Constructor for the hospital object. Sets up the ArrayList of rooms.
     */
    public Hospital(ConsoleInteraction cI)
    {
        rooms = new ArrayList<Room>();
        this.cI = cI;
    }

    /**
     * Lists all patients currently in the hospital system.
     */
    public void listPatients()
    { 
        for (int indexR = 0; indexR < rooms.size(); indexR++)
        {
            Room thisRoom = rooms.get(indexR);
            for (int indexB = 0; indexB < thisRoom.getBeds().size(); indexB++)
            {
                Bed thisBed = thisRoom.getBeds().get(indexB);                
                if (!thisBed.isEmpty())
                {
                    int roomNum = thisRoom.getRoomNumber();
                    String roomName = thisRoom.getRoomName();
                    int bedNum = thisBed.getBedNumber();
                    Patient thisPatient = thisBed.getPatient();
                    String patientName = thisPatient.getPatientName();
                    String infected = thisPatient.getInfectedStatus();
                    int age = thisPatient.getPatientAge();
                    cI.printlnFormat("%15s |%4d |%7d |%10s |%6d | %s",patientName,age,roomNum,roomName,bedNum,infected);
                }
            }
        }
    }
    
    /**
     * Prints all patients in the system to a seperate file.
     * @param outFile The PrintStream object used to write to the seperate file.
     */
    public void printPatients(PrintStream outFile)
    {
        for (int indexR = 0; indexR < rooms.size(); indexR++)
        {
            Room thisRoom = rooms.get(indexR);
            for (int indexB = 0; indexB < thisRoom.getBeds().size(); indexB++)
            {
                Bed thisBed = thisRoom.getBeds().get(indexB);
                if (!thisBed.isEmpty())
                {
                    int roomNum = thisRoom.getRoomNumber();
                    String roomName = thisRoom.getRoomName();
                    int bedNum = thisBed.getBedNumber();
                    Patient thisPatient = thisBed.getPatient();
                    String patientName = thisPatient.getPatientName();
                    String infected = thisPatient.getInfectedStatus();
                    int age = thisPatient.getPatientAge();
                    outFile.printf("%15s |%4d |%7d |%10s |%6d | %s%n",patientName,age,roomNum,roomName,bedNum,infected);
                }
            }
        }
    }

    /**
     * Adds a new room to the ArrayList of rooms
     */
    public void addRoom(Room toAdd)
    {
        rooms.add(toAdd);
    }

    /**
     * Used when setting up the system. Helps keep track of how many patients are in the system.
     * @param newPatient The patient to be added.
     */
    public void addPatient(Patient newPatient)
    {
        numPatients++;
        if (newPatient.isInfected())
            numInfected++;
    }

    /**
     * Used by users to add new patients to the system. Used when the program is loaded and running.
     */
    public void addPatient(String newPatientName, int newPatientAge, boolean infected)
    {
        Bed openBed = searchOpenBed();

        if (openBed != null)
        {
            int bedNumber = openBed.getBedNumber();
            int roomNumber = openBed.getRoomNumber();
            Patient newPatient = new Patient(newPatientName,newPatientAge,infected,roomNumber,bedNumber);
            openBed.setPatient(newPatient);
            cI.println("New patient " + newPatientName + " is set to room " + roomNumber + ", bed " + bedNumber + " .");
            numPatients++;
            if (infected)
                numInfected++;
        }
        else 
        {
            cI.print("There are no free beds!");
        }
    }

    /**
     * Removes a patient from the system.
     */
    public void removePatient(int roomNumber, int bedNumber)
    {
        boolean roomFound = false, bedFound = false, isEmpty = false;
        for (int indexR = 0; indexR < rooms.size(); indexR++)
        {
            ArrayList<Bed> beds = rooms.get(indexR).getBeds();
            if (roomNumber == rooms.get(indexR).getRoomNumber())
            {
                roomFound = true;
                for (int indexB = 0; indexB < beds.size(); indexB++)
                {
                    if (bedNumber == beds.get(indexB).getBedNumber())
                    {
                        bedFound = true;
                        isEmpty = beds.get(indexB).isEmpty();
                        if (isEmpty)
                            cI.println_error("This bed is already empty!");                            
                        else
                        {
                            beds.get(indexB).removePatient();
                            cI.println("The patient was successfully removed.");
                            numPatients--;
                        }
                        break;
                    }                    
                }
                break;
            }
        }

        if (!roomFound)
            cI.println_error("This room does not exist.");
        else if(!bedFound)
            cI.println_error("This bed does not exist.");
    }

    /**
     * Searches for open beds in the hospital system.
     * @return Will return an open bed if one exists, or null if none do.
     */
    private Bed searchOpenBed()
    {
        Bed freeBed = null;
        boolean foundBed = false;
        for (int indexR = 0; indexR < rooms.size(); indexR++)
        {            
            ArrayList<Bed> beds = rooms.get(indexR).getBeds();            
            for (int indexB = 0; indexB < beds.size(); indexB++)
            {
                if (beds.get(indexB).isEmpty())
                {
                    freeBed = beds.get(indexB);
                    foundBed = true;
                    break;
                }
            }
            if (foundBed)
                break;
        }
        return freeBed;
    }

    /**
     * Gathers all information on open beds and prints it out.
     */
    public int openBedReport()
    {
        int openBedCount = 0;
        for (int indexR = 0; indexR < rooms.size(); indexR++)
        {            
            ArrayList<Bed> beds = rooms.get(indexR).getBeds();            
            for (int indexB = 0; indexB < beds.size(); indexB++)
            {
                if (beds.get(indexB).isEmpty())
                {
                    openBedCount++;
                    int bedNumber = beds.get(indexB).getBedNumber();
                    int roomNumber = rooms.get(indexR).getRoomNumber();
                    String roomName = rooms.get(indexR).getRoomName();
                    cI.printlnFormat("%5d | %6d | %s",bedNumber,roomNumber,roomName);
                }
            }            
        }        
        return openBedCount;
    }
    
    /**
     * Gets the list of rooms
     * @return Returns the room list
     */
    public ArrayList<Room> getRoomList()
    {
        return rooms;
    }
    
    public int getNumPatients()
    {
        return numPatients;
    }
    
    public int getNumInfected()
    {
        return numInfected;
    }
}

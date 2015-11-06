package hospitalThings;

import java.util.*;

/**
 * Room objects. They know what beds are in them.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class Room
{
    private ArrayList<Bed> beds;
    int numBeds;
    String roomName;
    int thisRoomNumber;
    
    /**
     * Constructor for a Room object. Sets up the ArrayList of beds stored in it.
     * @param thisRoomNumber The room's number
     * @param numBeds How many beds are in this room
     * @param roomName The name given to this room
     */
    public Room(int thisRoomNumber, int numBeds, String roomName)
    {
        this.thisRoomNumber = thisRoomNumber;
        this.numBeds = numBeds;
        this.roomName = roomName;
        beds = new ArrayList<Bed>();
    }
    
    /**
     * Adds a new Bed to the list of beds
     * @param toAdd The bed to be added to the list
     */
    public void addBed(Bed toAdd)
    {
        beds.add(toAdd);
    }
    
    public int getNumBeds()
    {
        return numBeds;
    }
    public int getRoomNumber()
    {
        return thisRoomNumber;
    }
    public String getRoomName()
    {
        return roomName;
    }
    public ArrayList<Bed> getBeds()
    {
        return beds;
    }
}

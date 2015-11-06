package hospitalThings;

/**
 * Bed objects that know what patient is in them.
 * @author Erik Smistad
 * @version 1.0
 * Last Modified: March 16, 2015
 */
public class Bed
{
    boolean isEmpty;
    int bedNumber;
    Patient patient;
    int roomNumber;
    
    /**
     * Constructor for new beds. NOTE: Does not set a patient to the bed, this must be done seperately
     * @param bedNumber This beds number in the room it's in
     * @param isEmpty True if the bed is empty, false if not
     * @param roomNumber The room number this bed is in
     */
    public Bed(int bedNumber, boolean isEmpty, int roomNumber)
    {
        this.bedNumber = bedNumber;
        this.isEmpty = isEmpty;
        this.roomNumber = roomNumber;
    }
    
    /**
     * Checks if this bed has a patient in it or not.
     * @return Will return true if there is a patient in the bed, false otherwise.
     */
    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    /**
     * Puts a patient in this bed.
     * @param patient The patient to be added
     */
    public void setPatient(Patient patient)
    {
        this.patient = patient;
        isEmpty = false;
    }
    
    public Patient getPatient()
    {
        return patient;
    }
    
    /**
     * Removes a patient from the bed.
     */
    public void removePatient()
    {
        patient = null;
        isEmpty = true;
    }
    
    public int getBedNumber()
    {
        return bedNumber;
    }
    
    public int getRoomNumber()
    {
        return roomNumber;
    }
}

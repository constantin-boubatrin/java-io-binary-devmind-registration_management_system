package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuestsList implements Serializable {

    /** FIELDS */
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String serialFileName = "guestslist.dat";

    private final ArrayList<Guest> guests;
    private final ArrayList<Guest> searchResults;
    private final int guestsCapacity;
    private int numOfGuests;

    /** CONSTRUCTORS */
    public GuestsList(int guestsCapacity) {
        this.guests = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.guestsCapacity = guestsCapacity;
        this.numOfGuests = 0;
    }

    /** METHODS
     *
     * Add a new, unique guest to the list.
     *
     * @param g the guest to be added
     * @return '-1' if the guest is already present, '0' if he is a guest, or the number on the waiting list
     */
    public int add(Guest g) {
        if (isOnTheListAlready(g)) return -1;

        guests.add(g);
        this.numOfGuests++;

        if (this.numOfGuests <= this.guestsCapacity) {
            System.out.println("[" + g.fullName() + "] Congratulations! Your spot at the event is confirmed. We are waiting for you!");
            return 0;
        }

        int loc = this.numberOfPeopleWaiting();
        System.out.println("[" + g.fullName() + "] You have successfully joined the waiting list and received the order number "
                + loc + ". We will notify you if a spot becomes available.");

        return loc;
    }

    /**
     * Check if someone is already registered (either as a guest, or on the waiting list).
     *
     * @param g the guest we are searching for
     * @return true if present, false if not
     */
    private boolean isOnTheListAlready(Guest g) {
        for (Guest guest : guests) {
            if (guest.equals(g)) {
                System.out.println("The person is already registered.");
                return true;
            }

            if (guest.getPhoneNumber().equals(g.getPhoneNumber())) {
                System.out.println("The phone number is already registered.");
                return true;
            }

            if (guest.getEmail().equals(g.getEmail())) {
                System.out.println("The email address is already registered.");
                return true;
            }
        }

        return false;
    }

    /**
     * Search for a guest based on first and last name. Return the first result.
     *
     * @param firstName first name of the guest
     * @param lastName  last name of the guest
     * @return the guest if found, null if not
     */
    public Guest search(String firstName, String lastName) {
        for (Guest guest : guests)
            if (guest.getFirstName().equalsIgnoreCase(firstName) && guest.getLastName().equalsIgnoreCase(lastName))
                return guest;

        return null;
    }

    /**
     * Search for a guest based on email or phone number. Return the first result.
     *
     * @param opt   option to use for searching: 2 for email, 3 for phone number
     * @param match the match we are searching for
     * @return the guest if found, null if not
     */
    public Guest search(int opt, String match) {
        Guest foundGuest = null;

        switch (opt) {
            case 2 -> {
                for (Guest guest : guests)
                    if (guest.getEmail().equalsIgnoreCase(match)) {
                        foundGuest = guest;
                        break; // break "for" loop
                    }
            }
            case 3 -> {
                for (Guest guest : guests)
                    if (guest.getPhoneNumber().equals(match)) {
                        foundGuest = guest;
                        break; // break "for" loop
                    }
            }
        }

        return foundGuest;
    }

    /**
     * Remove a guest based on first and last name. Remove the first result.
     *
     * @param firstName first name of the guest
     * @param lastName  last name of the guest
     * @return true if removed, false if not
     */
    public boolean remove(String firstName, String lastName) {
        Guest rem = search(firstName, lastName);
        return deleteGuest(rem);
    }

    /**
     * Remove a guest based on email or phone number. Remove the first result.
     *
     * @param opt   option to use for searching: 2 for email, 3 for phone number
     * @param match the match we are searching for
     * @return true if removed, false if not
     */
    public boolean remove(int opt, String match) {
        Guest rem = search(opt, match);
        return deleteGuest(rem);
    }

    /**
     * Delete a guest from the list. Notify the next person in line they have a space, if needed.
     *
     * @param g the guest we are deleting
     * @return true if guest was deleted, false otherwise
     */
    private boolean deleteGuest(Guest g) {
        int index; // to see if we are removing a guest, or a person

        index = this.guests.indexOf(g);

        if (index == -1) return false;
        this.guests.remove(g);
        this.numOfGuests--;
        if (index < this.guestsCapacity) notifyNewGuest();
        return true;
    }

    // Notify the guest
    private void notifyNewGuest() {
        if (this.numberOfPeopleTotal() == 0) return; // if we have a list of capacity 1 and are removing the last person

        Guest g;
        g = guests.get(this.guestsCapacity - 1);

        if (g != null)
            System.out.println("Congratulations " + g.fullName() + "! Your place at the event is confirmed. We are waiting for you!");
    }

    // Show the list of guests.
    public void showGuestsList() {
        if (this.guests.isEmpty()) {
            System.out.println("No participants registered...");
            return;
        }

        int limit = Math.min(this.numOfGuests, this.guestsCapacity);

        for (int i = 0; i < limit; i++)
            System.out.println((i + 1) + ". " + this.guests.get(i));
    }

    // Show the people on the waiting list.
    public void showWaitingList() {
        if (this.numberOfPeopleWaiting() == 0) {
            System.out.println("The waiting list is empty...");
            return;
        }

        for (int i = this.guestsCapacity, j = 1; i < this.guests.size(); i++, j++)
            System.out.println(j + ". " + this.guests.get(i));
    }

    /**
     * Show how many free spots are left.
     *
     * @return the number of spots left for guests
     */
    public int numberOfAvailableSpots() {
        int result = this.guestsCapacity - this.numOfGuests;
        return Math.max(result, 0);
    }

    /**
     * Show how many guests there are.
     *
     * @return the number of guests
     */
    public int numberOfGuests() {
        return Math.min(this.numOfGuests, this.guestsCapacity);
    }

    /**
     * Show how many people are on the waiting list.
     *
     * @return number of people on the waiting list
     */
    public int numberOfPeopleWaiting() {
        int waiting = this.numOfGuests - this.guestsCapacity;
        return Math.max(waiting, 0);
    }

    /**
     * Show how many people there are in total, including guests.
     *
     * @return how many people there are in total
     */
    public int numberOfPeopleTotal() {
        return this.numOfGuests;
    }

    /**
     * Find all people based on a partial value search.
     *
     * @param match the match we are looking for
     * @return a list of people matching the criteria
     */
    public List<Guest> partialSearch(String match) {
        this.searchResults.clear();

        for (Guest guest : this.guests) {
            if (guest.getFirstName().toLowerCase().contains(match.toLowerCase())
                    || guest.getLastName().toLowerCase().contains(match.toLowerCase())
                    || guest.getEmail().toLowerCase().contains(match.toLowerCase())
                    || guest.getPhoneNumber().contains(match))
                this.searchResults.add(guest);
        }

        return new ArrayList<>(this.searchResults);
    }

    @Override
    public String toString() {
        return this.guests.toString();
    }

    /**
     * Write/Save application data to a binary file.
     * @throws IOException
     */
    public void writeToBinaryFile() throws IOException {
        try (ObjectOutputStream binaryFileOut =
                     new ObjectOutputStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(serialFileName)))) {

            binaryFileOut.writeObject(this);
        }
    }

    /**
     * Read/Restore previous stored application data from a binary file.
     *
     * @return a 'GuestsList' instance
     * @throws IOException and ClassNotFoundException
     */
    public GuestsList readFromBinaryFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream binaryFileIn =
                     new ObjectInputStream(
                             new BufferedInputStream(
                                     new FileInputStream(serialFileName)))) {

            return (GuestsList) binaryFileIn.readObject();
        }
    }

    /**
     * Reset the application
     */
    public void resetList() {
        this.guests.clear();
        this.searchResults.clear();
        this.numOfGuests = 0;
    }
}
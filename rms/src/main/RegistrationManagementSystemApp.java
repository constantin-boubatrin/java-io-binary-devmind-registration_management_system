package main;

import java.io.*;
import java.util.Scanner;
import java.util.List;

public class RegistrationManagementSystemApp {

    private void addNewGuest(Scanner sc, GuestsList list) {
        String lastName = sc.nextLine();
        String firstName = sc.nextLine();
        String email = sc.nextLine();
        String phoneNumber = sc.nextLine();
        list.add(new Guest(lastName, firstName, email, phoneNumber));
    }

    private void checkGuest(Scanner sc, GuestsList list) {
        int opt;
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 3) System.out.println("Please enter an integer between 1 and 3.");
            else break;
        }

        Guest foundGuest = null;

        switch (opt) {
            case 1 -> {
                String lastName = sc.nextLine();
                String firstName = sc.nextLine();
                foundGuest = list.search(firstName, lastName);
            }
            case 2, 3 -> {
                String match = sc.nextLine();
                foundGuest = list.search(opt, match);
            }
        }

        if (foundGuest == null) System.out.println("Not found");
        else System.out.println(foundGuest);
    }

    private void removeGuest(Scanner sc, GuestsList list) {
        int opt;
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 3) System.out.println("Please enter an integer between 1 and 3.");
            else break;
        }

        boolean removed = false;

        switch (opt) {
            case 1 -> {
                String lastName = sc.nextLine();
                String firstName = sc.nextLine();
                removed = list.remove(firstName, lastName);
            }
            case 2 -> {
                String email = sc.nextLine();
                removed = list.remove(opt, email);
            }
            case 3 -> {
                String phoneNumber = sc.nextLine();
                removed = list.remove(opt, phoneNumber);
            }
        }
    }

    private void updateGuest(Scanner sc, GuestsList list) {
        int opt;
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 3) System.out.println("Please enter an integer between 1 and 3.");
            else break;
        }

        Guest foundGuest = null;

        switch (opt) {
            case 1 -> {
                String lastName = sc.nextLine();

                String firstName = sc.nextLine();

                foundGuest = list.search(firstName, lastName);
            }
            case 2 -> {
                String email = sc.nextLine();
                foundGuest = list.search(opt, email);
            }
            case 3 -> {
                String phoneNumber = sc.nextLine();
                foundGuest = list.search(opt, phoneNumber);
            }
        }

        // If we have no results, exit
        if (foundGuest == null) {
            System.out.println("Error: The person is not registered for the event.");
            return;
        }

        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 4) System.out.println("Please enter an integer between 1 and 4.");
            else break;
        }

        switch (opt) {
            case 1 -> foundGuest.setLastName(sc.nextLine());
            case 2 -> foundGuest.setFirstName(sc.nextLine());
            case 3 -> foundGuest.setEmail(sc.nextLine());
            case 4 -> foundGuest.setPhoneNumber(sc.nextLine());
        }
    }

    private void searchList(Scanner sc, GuestsList list) {
        String match = sc.nextLine();
        List<Guest> results = list.partialSearch(match);

        for (Guest g : results)
            System.out.println(g.toString());

        if (results.size() == 0)
            System.out.println("Nothing found");
    }

    private static void saveList(GuestsList list) {
        try {
            list.writeToBinaryFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static GuestsList restoreList(GuestsList list) {
        try {
            list = list.readFromBinaryFile();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private static void resetList(GuestsList list) {
        list.resetList();
        saveList(list);
    }

    private static void printCommandsList() {
        System.out.println("""
                        help         - Display the list of commands
                        add          - Add a new person (registration)
                        check        - Check if a person is registered for the event
                        remove       - Remove an existing person from the list
                        update       - Update the details of a person
                        guests       - List of participants in the event
                        waitlist     - People on the waiting list
                        available    - Available spots
                        guests_no    - Number of participants in the event
                        waitlist_no  - Number of people on the waiting list
                        subscribe_no - Total number of people registered
                        search       - Search all guests based on input
                        save         - Save the guest list
                        restore      - Complete the list with previously saved information
                        reset        - Delete saved information about guests
                        quit         - Close the application""");
    }

    public void run(Scanner scanner) {
        int size = scanner.nextInt();
        scanner.nextLine();

        GuestsList list = new GuestsList(size);

        boolean running = true;
        while (running) {
            String command = scanner.nextLine();

            switch (command) {
                case "help" -> printCommandsList();
                case "add" -> addNewGuest(scanner, list);
                case "check" -> checkGuest(scanner, list);
                case "remove" -> removeGuest(scanner, list);
                case "update" -> updateGuest(scanner, list);
                case "guests" -> list.showGuestsList();
                case "waitlist" -> list.showWaitingList();
                case "available" -> System.out.println("The number of spots left: " + list.numberOfAvailableSpots());
                case "guests_no" -> System.out.println("Number of participants: " + list.numberOfGuests());
                case "waitlist_no" -> System.out.println("Number of persons on the waiting list: " + list.numberOfPeopleWaiting());
                case "subscribe_no" -> System.out.println("Total number of persons: " + list.numberOfPeopleTotal());
                case "search" -> searchList(scanner, list);
                case "save" -> saveList(list);
                case "restore" -> list = restoreList(list);
                case "reset" -> resetList(list);
                case "quit" -> {
                    System.out.println("Application is closing...");
                    scanner.close();
                    running = false;
                }
                default -> System.out.println("Unknown command. Please try again.");
            }
        }
    }
}
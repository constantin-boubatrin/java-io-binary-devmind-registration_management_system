package main;

import java.io.Serial;
import java.io.Serializable;

public class Guest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

    public Guest(String lastName, String firstName, String email, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (this.firstName == null ? 0 : this.firstName.hashCode());
        result = prime * result + (this.lastName == null ? 0 : this.lastName.hashCode());
        result = prime * result + (this.email == null ? 0 : this.email.hashCode());
        result = prime * result + (this.phoneNumber == null ? 0 : this.phoneNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Guest g = (Guest) obj;

        return this.firstName.equals(g.firstName)
                && this.lastName.equals(g.lastName)
                && this.email.equals(g.email)
                && this.phoneNumber.equals(g.phoneNumber);
    }

    @Override
    public String toString() {
        return "Full Name: " + fullName() + ", Email: " + this.email + ", Phone Number: " + this.phoneNumber;
    }

    public String fullName() {
        return this.lastName + " " + this.firstName;
    }
}
package ca.dal.csci3130.quickcash.usermanagement;

import java.util.ArrayList;
import java.util.List;

/**
 * User Object to save a user's details
 */
public class User implements UserInterface {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean isEmployee;
    private double rating;
    private int numberOfRatings;
    private List<String> appliedJobs;

    // constructor
    public User(String firstName, String lastName, String email, String phone, String password,
                boolean isEmployee) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isEmployee = isEmployee;
        this.appliedJobs = new ArrayList<>();
        this.rating = 0;
        this.numberOfRatings = 0;
    }

    // empty constructor
    public User() {
    }

    // getters and setters for all data points

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean getIsEmployee() {
        return isEmployee;
    }

    @Override
    public void setIsEmployee(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    @Override
    public List<String> getAppliedJobs() {
        return appliedJobs == null ? new ArrayList<>() : appliedJobs;
    }

    @Override
    public double getRating() {
        return rating;
    }

    @Override
    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    @Override
    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }
}

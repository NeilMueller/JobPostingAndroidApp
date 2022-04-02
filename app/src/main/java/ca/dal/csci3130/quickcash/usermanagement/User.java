package ca.dal.csci3130.quickcash.usermanagement;

import java.util.ArrayList;

public class User implements UserInterface {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean isEmployee; // true or false.
    private double rating;
    private int numberOfRatings;
    private ArrayList<String> appliedJobs;

    public User(String firstName, String lastName, String email, String phone, String password,
                boolean isEmployee) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isEmployee = isEmployee;
        this.appliedJobs = new ArrayList<>();
        rating = 0;
        numberOfRatings = 0;
        //appliedJobs.add("Te75803310");
        //appliedJobs.add("Pa67723601");
    }

    public User() {
    }

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
    public ArrayList<String> getAppliedJobs(){
        return appliedJobs;
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

package ca.dal.csci3130.quickcash.usermanagement;

import java.util.List;

/**
 * User Interface for storing all the details of a user
 */

public interface UserInterface {

    /**
     * Gets the first name of the user
     *
     * @return a string that contains the first name
     */
    String getFirstName();

    /**
     * Set the first name of the user
     *
     * @param firstName
     */
    void setFirstName(String firstName);

    /**
     * Get the first name of the user
     *
     * @return lastName
     */
    String getLastName();

    /**
     * Set last name of the user
     *
     * @param lastName
     */
    void setLastName(String lastName);

    /**
     * Get the user's email
     *
     * @return userEmail
     */
    String getEmail();

    /**
     * Set the user's email
     *
     * @param email
     */
    void setEmail(String email);

    /**
     * Get the user's phone number
     *
     * @return phoneNumber - String
     */
    String getPhone();

    /**
     * Set the user's phone number
     *
     * @param phone
     */
    void setPhone(String phone);

    /**
     * Get the user's password
     *
     * @return
     */
    String getPassword();

    /**
     * Set the user's password
     *
     * @param password
     */
    void setPassword(String password);

    /**
     * Check whether the user is an employee or an employer
     *
     * @return true - employee, false - employer
     */
    boolean getIsEmployee();

    /**
     * Set whether the user is an employee or employer
     *
     * @param isEmployee
     */
    void setIsEmployee(boolean isEmployee);

    /**
     * Get the list of applied jobs for the user
     *
     * @return List of strings that contain the job id of the every job the user has applied
     */
    List<String> getAppliedJobs();

    /**
     * Get the user's rating
     *
     * @return rating - double value
     */
    double getRating();

    /**
     * Set the user's rating
     *
     * @param rating
     */
    void setRating(double rating);

    /**
     * Get the number of times the user has been rated
     *
     * @return number of times the user was rated - int
     */
    int getNumberOfRatings();

    /**
     * Set the number of times the user has been rated
     *
     * @param numberOfRating
     */
    void setNumberOfRatings(int numberOfRating);
}


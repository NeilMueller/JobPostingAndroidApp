package ca.dal.csci3130.quickcash.usermanagement;

public interface UserInterface {

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);

    int getPhone();

    void setPhone(int phone);

    String getPassword();

    void setPassword(String password);

    boolean getIsEmployee();

    void setIsEmployee(boolean isEmployee);
}


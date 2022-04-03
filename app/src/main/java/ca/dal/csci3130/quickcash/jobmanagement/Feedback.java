package ca.dal.csci3130.quickcash.jobmanagement;

public class Feedback {

    private String employeeEmailFB;
    private float myRating;

    public Feedback(){}
    public Feedback(String employeeEmailFB, float myRating){
        this.employeeEmailFB = employeeEmailFB;
        this.myRating = myRating;
    }

    public String getEmployeeEmailFB() {
        return employeeEmailFB;
    }

    public void setEmployeeEmailFB(String employeeEmailFB) {
        this.employeeEmailFB = employeeEmailFB;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }
}

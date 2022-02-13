package ca.dal.csci3130.quickcash.usermanagement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginActivityJUnitTest {
    LoginActivity loginActivityMock;

    final String[] invalidCredentials = {"invalidCreds@dal.ca", "Invalidcreds@1"};
    final String[] validEmployeeCredentials = {"employee1@dal.ca", "Employee@1"};
    final UserInterface registeredEmployee = new User("Employee", "1", "employee1@dal.ca", 1234567890,
            "Employee@1", true);

    @Before
    public void setup () {
        loginActivityMock = Mockito.mock(LoginActivity.class, Mockito.CALLS_REAL_METHODS);
    }

    // No users present
    @Test
    public void nullUser(){
        assertFalse(loginActivityMock.checkCredentials(validEmployeeCredentials, null));
    }

    // Invalid Employee Credentials
    @Test
    public void invalidEmployeeCredentials(){
        assertFalse(loginActivityMock.checkCredentials(invalidCredentials, registeredEmployee));
    }

    // Valid Employee Credentials
    @Test
    public void validEmployeeCredentials(){
        assertTrue(loginActivityMock.checkCredentials(validEmployeeCredentials, registeredEmployee));
    }
}

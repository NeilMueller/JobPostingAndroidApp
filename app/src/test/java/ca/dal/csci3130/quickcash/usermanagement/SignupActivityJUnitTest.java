package ca.dal.csci3130.quickcash.usermanagement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SignupActivityJUnitTest {

    UserDAO userMock;
    SignupActivity signupActivityMock;



    @Before
    public void setup () {
        userMock = Mockito.mock(UserDAO.class);
        when(userMock.add(any(UserInterface.class))).thenReturn(null);
        signupActivityMock = Mockito.mock(SignupActivity.class);
        doNothing().when(signupActivityMock).addUser(any());
    }


    /*** isEmpty()**/
    @Test
    public void emptyFName() {
        assertTrue(signupActivityMock.isEmpty("", "Smith", "js12345@dal.ca", 1234567890, "Abc1de9fG!", "Abc1de9fG!"));
    }

    @Test
    public void emptyLName() {
        assertTrue(signupActivityMock.isEmpty("Smith", "", "js12345@dal.ca", 1234567890, "Abc1de9fG!", "Abc1de9fG!"));
    }

    @Test
    public void emptyEmail() {
        assertTrue(signupActivityMock.isEmpty("Smith", "Smith", "", 1234567890, "Abc1de9fG!", "Abc1de9fG!"));
    }

    @Test
    public void emptyPhone() {
        assertTrue(signupActivityMock.isEmpty("Smith", "Smith", "js12345@dal.ca", 0, "Abc1de9fG!", "Abc1de9fG!"));
    }

    @Test
    public void emptyPassword() {
        assertTrue(signupActivityMock.isEmpty("Smith", "Smith", "js12345@dal.ca", 1234567890, "", "Abc1de9fG!"));
    }

    @Test
    public void emptyCPassword() {
        assertTrue(signupActivityMock.isEmpty("Smith", "Smith", "js12345@dal.ca", 1234567890, "Abc1de9fG!", ""));
    }

    @Test
    public void notEmpty() {
        assertFalse(signupActivityMock.isEmpty("Joe", "Smith", "js12345@dal.ca", 1234567890, "Abc1de9fG!", "Abc1de9fG!"));
    }

    /*** isValidEmail()**/
    @Test
    public void checkIfEmailIsValid() { assertTrue(signupActivityMock.isValidEmail("js12345@dal.ca")); }

    @Test
    public void checkIfEmailIsInvalid() {
        assertFalse(signupActivityMock.isValidEmail("js12345dal.ca"));
        assertFalse(signupActivityMock.isValidEmail("js12345@dalca"));
        assertFalse(signupActivityMock.isValidEmail("@dal.ca"));
        assertFalse(signupActivityMock.isValidEmail(".js12345@dal.ca"));
    }

    /*** isValidPassword()**/
    @Test
    public void checkIfPasswordIsValid() { assertTrue(signupActivityMock.isPasswordValid("Abc1d9G!")); }

    @Test
    public void checkIfPasswordIsInvalid() {
        assertFalse(signupActivityMock.isPasswordValid("abc1de9fg!"));
        assertFalse(signupActivityMock.isPasswordValid("ABC1DE9FG!"));
        assertFalse(signupActivityMock.isPasswordValid("Abc1ef!"));
        assertFalse(signupActivityMock.isPasswordValid("Abc1de9fg"));
    }

    /*** passwordConfirmation()**/
    @Test
    public void checkIfPasswordsMatch() { assertTrue(signupActivityMock.passwordMatcher("Abc1de9fG!", "Abc1de9fG!")); }

    @Test
    public void checkIfPasswordsDontMatch() {
        assertFalse(signupActivityMock.passwordMatcher("Abc1de9fG!", "abc1de9fg!"));
        assertFalse(signupActivityMock.passwordMatcher("Abc1de9fG!", "Abc1e9fG!"));
        assertFalse(signupActivityMock.passwordMatcher("Abc1de9fG!", "Abcde9fG!"));
        assertFalse(signupActivityMock.passwordMatcher("Abc1de9fG!", "Abc1de9fG"));
    }


    /*** phoneLength()**/
    @Test
    public void checkIfPhoneValid() { assertTrue(signupActivityMock.isPhoneValid(1234567890)); }

    @Test
    public void checkIfPhoneInvalid() {
        assertFalse(signupActivityMock.isPhoneValid(123456789));
        assertFalse(signupActivityMock.isPhoneValid(10000));       //FIX THIS IT SHOULD BE ABLE TO HANDLE A TEST OF TOO LARGE
    }
}

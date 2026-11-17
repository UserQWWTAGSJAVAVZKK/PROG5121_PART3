
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Student
 */


package com.mycompany.prog5121_poe;

public class Login {

    private String userName;
    private String password;
    private String cellPhoneNumber;

    // Store the registration information.
    public Login(String userName, String password, String cellPhoneNumber) {
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // A username must contain an underscore and be no longer than 5 characters.
    public boolean checkUserName() {
        return userName != null
                && userName.contains("_")
                && userName.length() <= 5;
    }

    // A password must have at least 8 characters,
    // one capital letter, one number and one special character.
    public boolean checkPassword() {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapital && hasNumber && hasSpecialCharacter;
    }

    // Check that the cellphone number starts with +27
    // and contains 9 digits after the country code.
    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null
                && cellPhoneNumber.matches("\\+27\\d{9}");
    }

    // Return the username validation result.
    public String getUserNameMessage() {

        if (checkUserName()) {
            return "Username successfully captured.";
        }

        return "Username is incorrectly formatted. "
                + "Please ensure that your username contains an underscore "
                + "and is no more than five characters long.";
    }

    // Return the password validation result.
    public String getPasswordMessage() {

        if (checkPassword()) {
            return "Password successfully captured.";
        }

        return "Password is not correctly formatted. "
                + "It must contain at least 8 characters, "
                + "a capital letter, a number and a special character.";
    }

    // Return the cellphone validation result.
    public String getCellPhoneMessage() {

        if (checkCellPhoneNumber()) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number incorrectly formatted. "
                + "Please include the South African country code +27.";
    }

    // Check whether all registration details are valid.
    public boolean registrationSuccessful() {
        return checkUserName()
                && checkPassword()
                && checkCellPhoneNumber();
    }

    // Compare the entered login information with the registered details.
    public boolean loginUser(String enteredUsername, String enteredPassword) {

        return userName.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    // Display the login result.
    public String returnLoginStatus(boolean successful) {

        if (successful) {
            return "Login successful. Welcome!";
        }

        return "Login failed. Incorrect username or password.";
    }

    // Return the registered cellphone number.
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}


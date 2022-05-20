package com.albertmercade.model;

import javafx.beans.property.*;

public class Password {

    // Generated password
    private final StringProperty password;
    // Length of password to be generated
    private final IntegerProperty passwordLength;

    // Boolean indicating whether to include that type of character
    private final BooleanProperty includeUpperCase, includeLowerCase,
            includeDigits, includeSpecialChars;
    // Minimum number of occurrences of a specific character type
    private final IntegerProperty minNumDigits, minNumSpecialChars;
    // Set of special characters to use in password
    private final StringProperty userDefinedSpecialChars;

    public Password() {
        // Default settings
        passwordLength = new SimpleIntegerProperty(20);
        includeUpperCase = new SimpleBooleanProperty(true);
        includeLowerCase = new SimpleBooleanProperty(true);
        includeDigits = new SimpleBooleanProperty(true);
        minNumDigits = new SimpleIntegerProperty(5);
        includeSpecialChars = new SimpleBooleanProperty(true);
        minNumSpecialChars = new SimpleIntegerProperty(5);
        userDefinedSpecialChars = new SimpleStringProperty("");

        // Generate initial password according to default settings
        password = new SimpleStringProperty();
    }

    // Getters for private member variables
    public StringProperty getPasswordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String newPassword) {
        password.set(newPassword);
    }

    public IntegerProperty getPasswordLengthProperty() {
        return passwordLength;
    }

    public int getPasswordLength() {
        return passwordLength.get();
    }

    public void updatePasswordLength(int newPasswordLength) {
        // Minimum password length according to updated criteria
        int totalMinChars = calculateMinPasswordLength();

        // Update password length if criteria is met
        passwordLength.set(Math.max(newPasswordLength, totalMinChars));
    }

    public void updatePasswordLengthOnCriteriaChange() {
        // Minimum password length according to updated criteria
        int totalMinChars = calculateMinPasswordLength();

        // Get the password length before update
        int currPasswordLength = passwordLength.get();

        passwordLength.set(Math.max(currPasswordLength, totalMinChars));
    }

    public BooleanProperty getIncludeUpperCaseProperty() {
        return includeUpperCase;
    }

    public boolean getIncludeUpperCase() {
        return includeUpperCase.get();
    }

    public BooleanProperty getIncludeLowerCaseProperty() {
        return includeLowerCase;
    }

    public boolean getIncludeLowerCase() {
        return includeLowerCase.get();
    }

    public void setIncludeLowerCase(boolean include) {
        includeLowerCase.set(include);
    }


    public BooleanProperty getIncludeDigitsProperty() {
        return includeDigits;
    }

    public boolean getIncludeDigits() {
        return includeDigits.get();
    }

    public BooleanProperty getIncludeSpecialCharsProperty() {
        return includeSpecialChars;
    }
  
    public boolean getIncludeSpecialChars() {
        return includeSpecialChars.get();
    }

    public IntegerProperty getMinNumDigitsProperty() {
        return minNumDigits;
    }

    public int getMinNumDigits() {
        return minNumDigits.get();
    }

    public IntegerProperty getMinNumSpecialCharsProperty() {
        return minNumSpecialChars;
    }

    public int getMinNumSpecialChars() {
        return minNumSpecialChars.get();
    }

    public StringProperty getUserDefinedSpecialCharsProperty() {
        return userDefinedSpecialChars;
    }

    public char[] getUserDefinedSpecialChars() {
        return userDefinedSpecialChars.get().toCharArray();
    }

    public void setUserDefinedSpecialChars(String specialChars) {
        userDefinedSpecialChars.set(specialChars);
    }

    private int calculateMinPasswordLength() {
        // Get minimum number of chars per type
        int numUpperCase = includeUpperCase.get() ? 1 : 0;
        int numLowerCase = includeLowerCase.get() ? 1 : 0;
        int numDigits = includeDigits.get() ? minNumDigits.get() : 0;
        int numSpecialChars = includeSpecialChars.get() ? minNumSpecialChars.get() : 0;

        // Return minimum password length according to criteria
        return numUpperCase + numLowerCase + numDigits + numSpecialChars;
    }
}

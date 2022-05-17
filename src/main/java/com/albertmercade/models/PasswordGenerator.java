package com.albertmercade.models;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class PasswordGenerator {
    // Constants
    private final static int MAX_PWD_LENGTH = 50;
    private final static char[] SPECIAL_CHARS = {
            '!','"','#','$','%','&','\'','(',')','*','+',',','-','.','/',':',
            ';','<','=','>','?','@','[',']','^','_','`','{','|','}','~'
    };
    private final static short ASCII_UPPERCASE_BASE = 65;
    private final static short ASCII_LOWERCASE_BASE = 97;
    private final static short ALPHABET_LENGTH = 26;

    // Random instance for random number generation
    private final static Random randomGenerator = new Random();

    // Generated password
    private StringProperty password;
    // Length of password to be generated
    private IntegerProperty passwordLength;

    // Boolean indicating whether to include that type of character
    private final BooleanProperty includeUpperCase, includeLowerCase,
            includeDigits, includeSpecialChars;
    // Minimum number of occurrences of a specific character type
    private final IntegerProperty minNumDigits, minNumSpecialChars;

    public PasswordGenerator() {
        // Default settings
        passwordLength = new SimpleIntegerProperty(20);
        includeUpperCase = new SimpleBooleanProperty(true);
        includeLowerCase = new SimpleBooleanProperty(true);
        includeDigits = new SimpleBooleanProperty(true);
        minNumDigits = new SimpleIntegerProperty(1);
        includeSpecialChars = new SimpleBooleanProperty(true);
        minNumSpecialChars = new SimpleIntegerProperty(1);

        // Generate initial password according to default settings
        String initialPassword = generatePassword();
        password = new SimpleStringProperty(initialPassword);
    }

    // Getters for private member variables
    public StringProperty getPasswordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public IntegerProperty getPasswordLengthProperty() {
        return passwordLength;
    }

    public int getPasswordLength() {
        return passwordLength.get();
    }

    public BooleanProperty getIncludeUpperCaseProperty() {
        return includeUpperCase;
    }

    public BooleanProperty getIncludeLowerCaseProperty() {
        return includeLowerCase;
    }

    public BooleanProperty getIncludeDigitsProperty() {
        return includeDigits;
    }

    public BooleanProperty getIncludeSpecialCharsProperty() {
        return includeSpecialChars;
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

    public int getMaxMinNumDigits() {
        return passwordLength.get() - minNumSpecialChars.get();
    }

    public int getMaxMinNumSpecialChars() {
        return passwordLength.get() - minNumDigits.get();
    }

    public int getMaxPasswordLength() {
        return MAX_PWD_LENGTH;
    }

    private String generatePassword() {
        ArrayList<Character> newPassword =
                new ArrayList<Character>(passwordLength.get());

        for(int i = 0; i < minNumDigits.get(); ++i) {
            newPassword.add(randomDigitChar());
        }

        for(int i = 0; i < minNumSpecialChars.get(); ++i) {
            newPassword.add(randomSpecialChar());
        }

        int numRemainingChars = passwordLength.get() - minNumDigits.get()
                - minNumSpecialChars.get();
        for (int i = 0; i < numRemainingChars; ++i) {
            newPassword.add(randomChar());
        }

        Collections.shuffle(newPassword);

        return newPassword.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    // Helper methods
    private char randomChar() {
        int randomCharType = randomGenerator.nextInt(4);
        char randomChar;
        switch (randomCharType) {
            case 0:
                randomChar = randomUpperCaseChar();
                break;
            case 1:
                randomChar = randomDigitChar();
                break;
            case 2:
                randomChar = randomSpecialChar();
                break;
            default:
                randomChar = randomLowerCaseChar();
                break;
        }

        return randomChar;
    }

    private char randomUpperCaseChar() {
        return (char)(ASCII_UPPERCASE_BASE + randomGenerator.nextInt(ALPHABET_LENGTH));
    }

    private char randomLowerCaseChar() {
        return (char)(ASCII_LOWERCASE_BASE + randomGenerator.nextInt(ALPHABET_LENGTH));
    }

    private char randomDigitChar() {
        return Character.forDigit(randomGenerator.nextInt(10), 10);
    }

    private char randomSpecialChar() {
        int randomIdx = randomGenerator.nextInt(SPECIAL_CHARS.length);
        return SPECIAL_CHARS[randomIdx];
    }
}

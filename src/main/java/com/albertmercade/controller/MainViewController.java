package com.albertmercade.controller;

import com.albertmercade.model.Password;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import org.controlsfx.control.ToggleSwitch;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MainViewController {

    // Constants
    private final static List<Character> SPECIAL_CHARS = Arrays.asList(
        '!', '#', '$', '%', '&', '(', ')', '*', '+', '?', '@', '[', ']', '^', '{', '}', '~'
    );
    private final static short ASCII_UPPERCASE_BASE = 65;
    private final static short ASCII_LOWERCASE_BASE = 97;
    private final static short ALPHABET_LENGTH = 26;
    private final static int MAX_PASSWORD_LENGTH = 100;
    private final static int MAX_MIN_NUM_CHARS_PER_TYPE = 10;

    // Enum defining the possible chars types in the password
    private enum CHAR_TYPES {
        UPPER,
        LOWER,
        DIGIT,
        SPECIAL
    }
    // ArrayList indicating the char types to be included in the password
    private ArrayList<CHAR_TYPES> includedCharTypes;

    // Random instance for random number generation
    private final static Random randomGenerator = new Random();

    // Model
    private Password password;

    // FXML View nodes
    @FXML private Label passwordLabel;
    @FXML private Button copyPasswordButton;
    @FXML private FontAwesomeIconView copyPasswordGlyph;
    @FXML private Label copyPasswordLabel;
    @FXML private Spinner<Integer> passwordLengthSpinner;
    @FXML private ToggleSwitch upperCaseToggle;
    @FXML private ToggleSwitch lowerCaseToggle;
    @FXML private ToggleSwitch digitsToggle;
    @FXML private Spinner<Integer> digitsSpinner;
    @FXML private ToggleSwitch specialCharsToggle;
    @FXML private Spinner<Integer> specialCharsSpinner;
    @FXML private TextField userSpecialCharsTextField;

    // Methods
    public void initialize() {
        includedCharTypes = new ArrayList<>(Arrays.asList(CHAR_TYPES.values()));

        // Instantiate Model object
        password = new Password();
        // Generate and set password according to default conditions
        setNewPassword();

        // Initialize and link View nodes
        initializeViewNodes();
    }

    private void initializeViewNodes() {
        // Bind password label to password string
        passwordLabel.textProperty().bind(password.getPasswordProperty());

        // Bind ToggleSwitches to their respective Password properties
        initializeToggles();

        // Setup range of values for Spinners and bind them to their respective Password properties
        initializeSpinners();

        // Bind TextField to its Password property
        initializeSpecialCharsTextField();
    }

    private void initializeSpinners() {
        passwordLengthSpinner.setValueFactory(
                new IntegerSpinnerValueFactory(
                        0,
                        MAX_PASSWORD_LENGTH,
                        password.getPasswordLength()
                )
        );
        passwordLengthSpinner.getValueFactory().valueProperty()
                .bindBidirectional(password.getPasswordLengthProperty().asObject());
        passwordLengthSpinner.getValueFactory().valueProperty().addListener((observable, oldVal, newVal) -> {
            password.updatePasswordLength(newVal);
            setNewPassword();
        });

        digitsSpinner.setValueFactory(
                new IntegerSpinnerValueFactory(
                        0,
                        MAX_MIN_NUM_CHARS_PER_TYPE,
                        password.getMinNumDigits()
                )
        );
        password.getMinNumDigitsProperty().bind(digitsSpinner.getValueFactory().valueProperty());
        digitsSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            password.updatePasswordLengthOnCriteriaChange();
            setNewPassword();
        });

        specialCharsSpinner.setValueFactory(
                new IntegerSpinnerValueFactory(
                        0,
                        MAX_MIN_NUM_CHARS_PER_TYPE,
                        password.getMinNumSpecialChars()
                )
        );
        password.getMinNumSpecialCharsProperty().bind(specialCharsSpinner.getValueFactory().valueProperty());
        specialCharsSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            password.updatePasswordLengthOnCriteriaChange();
            setNewPassword();
        });
    }

    private void initializeToggles() {
        password.getIncludeUpperCaseProperty().bind(upperCaseToggle.selectedProperty());
        upperCaseToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            checkAllTogglesUnchecked();
            updateIncludedCharTypes(newValue, CHAR_TYPES.UPPER);
            setNewPassword();
        });

        lowerCaseToggle.selectedProperty().bindBidirectional(password.getIncludeLowerCaseProperty());
        lowerCaseToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            checkAllTogglesUnchecked();
            updateIncludedCharTypes(newValue, CHAR_TYPES.LOWER);
            setNewPassword();
        });

        password.getIncludeDigitsProperty().bind(digitsToggle.selectedProperty());
        digitsToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            digitsSpinner.setDisable(!newValue);
            checkAllTogglesUnchecked();
            updateIncludedCharTypes(newValue, CHAR_TYPES.DIGIT);
            setNewPassword();
        });

        password.getIncludeSpecialCharsProperty().bind(specialCharsToggle.selectedProperty());
        specialCharsToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            specialCharsSpinner.setDisable(!newValue);
            userSpecialCharsTextField.setDisable(!newValue);
            checkAllTogglesUnchecked();
            updateIncludedCharTypes(newValue, CHAR_TYPES.SPECIAL);
            setNewPassword();
        });
    }

    private void initializeSpecialCharsTextField() {
        // Bind special chars text field to password special chars string
        userSpecialCharsTextField.textProperty().bindBidirectional(password.getUserDefinedSpecialCharsProperty());
        // When text in text field is updated, generate new password
        userSpecialCharsTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            String filteredSpecialChars = filterUserDefinedSpecialChars(newValue);
            password.setUserDefinedSpecialChars(filteredSpecialChars);
            setNewPassword();
        }));
    }

    private void setNewPassword() {
        String newPassword = generatePassword();
        password.setPassword(newPassword);
    }

    private String generatePassword() {
        boolean includeUpperCase = password.getIncludeUpperCase(),
                includeLowerCase = password.getIncludeLowerCase(),
                includeDigits = password.getIncludeDigits(),
                includeSpecialChars = password.getIncludeSpecialChars();

        int passwordLength = password.getPasswordLength(),
                minUpperCase = includeUpperCase ? 1 : 0,
                minLowerCase = includeLowerCase ? 1 : 0,
                minNumDigits = password.getMinNumDigits(),
                minNumSpecialChars = password.getMinNumSpecialChars();

        ArrayList<Character> newPassword = new ArrayList<>(passwordLength);

        // Add to password the minimum number of characters of each type
        if (includeUpperCase) {
            newPassword.add(randomUpperCaseChar());
        }

        if (includeLowerCase) {
            newPassword.add(randomLowerCaseChar());
        }

        for(int i = 0; i < minNumDigits && includeDigits; ++i) {
            newPassword.add(randomDigitChar());
        }

        for(int i = 0; i < minNumSpecialChars && includeSpecialChars; ++i) {
            newPassword.add(randomSpecialChar());
        }

        // Randomly choose the rest of the characters
        int numRemainingChars = passwordLength - minUpperCase - minLowerCase
                - minNumDigits - minNumSpecialChars;
        for (int i = 0; i < numRemainingChars; ++i) {
            newPassword.add(randomChar());
        }

        // Randomly shuffle the characters in password
        Collections.shuffle(newPassword);

        // Convert ArrayList<Character> to String and return it
        return newPassword.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    // Event Handlers
    @FXML private void copyPasswordToClipboard(ActionEvent event) {
        String currPassword = password.getPassword();
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(currPassword), null);
        passwordLabel.getStyleClass().addAll("password-hbox-green-border", "password-label-green");
        copyPasswordButton.getStyleClass().addAll("password-hbox-green-border", "copy-password-button-green");
        copyPasswordGlyph.getStyleClass().add("copy-password-glyph-green");
        copyPasswordLabel.setVisible(true);

        CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
            passwordLabel.getStyleClass().removeAll("password-hbox-green-border", "password-label-green");
            copyPasswordButton.getStyleClass().removeAll("password-hbox-green-border", "copy-password-button-green");
            copyPasswordGlyph.getStyleClass().remove("copy-password-glyph-green");
            copyPasswordLabel.setVisible(false);
        });

        event.consume();
    }

    @FXML private void generateNewPassword(ActionEvent event) {
        setNewPassword();
        event.consume();
    }

    // Helper methods
    private void updateIncludedCharTypes(boolean newVal, CHAR_TYPES type) {
        if (newVal && !includedCharTypes.contains(type)) {
            includedCharTypes.add(type);
        }
        else {
            includedCharTypes.remove(type);
        }
    }

    private void checkAllTogglesUnchecked() {
        if (!password.getIncludeUpperCase()
                && !password.getIncludeLowerCase()
                && !password.getIncludeDigits()
                && !password.getIncludeSpecialChars()) {
            password.setIncludeLowerCase(true);
        }
    }

    private String filterUserDefinedSpecialChars(String newChars) {
        return newChars.codePoints()
                .filter(c -> SPECIAL_CHARS.contains((char)c))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private char randomChar() {
        int randomCharTypeIdx = randomGenerator.nextInt(includedCharTypes.size());
        return switch (includedCharTypes.get(randomCharTypeIdx)) {
            case UPPER -> randomUpperCaseChar();
            case DIGIT -> randomDigitChar();
            case SPECIAL -> randomSpecialChar();
            default  -> randomLowerCaseChar();
        };
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
        char randomChar;
        char[] userDefinedSpecialChars = password.getUserDefinedSpecialChars();

        if (userDefinedSpecialChars.length > 0) {
            int randomIdx = randomGenerator.nextInt(userDefinedSpecialChars.length);
            randomChar = userDefinedSpecialChars[randomIdx];
        }
        else {
            int randomIdx = randomGenerator.nextInt(SPECIAL_CHARS.size());
            randomChar = SPECIAL_CHARS.get(randomIdx);
        }

        return randomChar;
    }
}

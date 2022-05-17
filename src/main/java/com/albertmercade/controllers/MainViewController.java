package com.albertmercade.controllers;

import com.albertmercade.models.PasswordGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.controlsfx.control.ToggleSwitch;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class MainViewController {

    // Model
    private PasswordGenerator passwordGenerator;

    // View nodes
    @FXML private Label passwordLabel;
    @FXML private Spinner passwordLengthSpinner;
    @FXML private ToggleSwitch upperCaseToggle;
    @FXML private ToggleSwitch lowerCaseToggle;
    @FXML private ToggleSwitch digitsToggle;
    @FXML private Spinner digitsSpinner;
    @FXML private ToggleSwitch specialCharsToggle;
    @FXML private Spinner specialCharsSpinner;

    // Methods
    public void initialize() {
        passwordGenerator = new PasswordGenerator();

        // Initialize View nodes
        initializeSpinners();

        // Link Model properties to View
        passwordLabel.textProperty()
                .bind(passwordGenerator.getPasswordProperty());
    }

    private void initializeSpinners() {
        passwordLengthSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        passwordGenerator.getMaxPasswordLength(),
                        passwordGenerator.getPasswordLength()
                )
        );

        digitsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        passwordGenerator.getMaxMinNumDigits(),
                        passwordGenerator.getMinNumDigits()
                )
        );

        specialCharsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        passwordGenerator.getMaxMinNumSpecialChars(),
                        passwordGenerator.getMinNumDigits()
                )
        );
    }

    // Event Handlers
    @FXML private void copyPasswordToClipboard(ActionEvent event) {
        String currPassword = passwordGenerator.getPassword();
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(currPassword), null);
        event.consume();
    }
}

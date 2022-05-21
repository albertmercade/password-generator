# Password Generator App

App to generate passwords according to certain criteria. One can customise:
 * Length of the password
 * Types of characters included
(upper case, lower case, digits and special characters)
 * The minimum number of digits and special characters
to include
 * The special characters to use in the generation of the password.
There's a default set of characters that will be used if none is provided.

The application has been developed using JDK 17 and JavaFX 17.

![Application screenshot](https://raw.githubusercontent.com/albertmercade/password-generator/media/screenshot.png)

## Run

1. Have JDK 17 installed in your machine.

2. Clone this repository:
```shell
# using ssh
git clone git@github.com:albertmercade/password-generator.git
# or using https
git clone https://github.com/albertmercade/password-generator.git
```

3. Run the following command:
```shell
# macOS/Linux
./gradlew run
# or Windows
gradlew.bat run
```
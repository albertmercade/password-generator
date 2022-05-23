# Password Generator App

App to generate passwords according to certain criteria. The user can customise:
 * Length of the password
 * Types of characters included
(upper case, lower case, digits and special characters)
 * The minimum number of digits and special characters
to include
 * The special characters to use in the generation of the password.
There's a default set of characters that will be used if none is provided.

The application has been developed using [JavaFX](https://openjfx.io/) 17
and [FXML](https://openjfx.io/javadoc/17/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html)
(with [SceneBuilder](https://gluonhq.com/products/scene-builder/)).

![Application screen recording](https://raw.githubusercontent.com/albertmercade/password-generator/media/screen-recording.gif)

## Run

1. Have JDK 11 (or newer) installed in your machine.

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
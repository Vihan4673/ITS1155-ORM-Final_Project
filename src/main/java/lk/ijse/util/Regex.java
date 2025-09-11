package lk.ijse.util;

import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {


    public enum FieldType {
        NAME, ADDRESS, TEL, MONTH, PRICE, STUDENTID, PROGRAMID
    }

    // Validation method
    public static boolean isTextFieldValid(lk.ijse.util.TextField fieldType, String text) {
        String regex = switch (fieldType) {
            case NAME -> "^[A-Za-z]+(?: [A-Za-z]+)*$";
            case ADDRESS -> "^([A-z0-9]|[-\\,.@+]|\\s){4,}$";
            case TEL -> "^[0]([1-9]{2})([0-9]){7}$";
            case MONTH -> "^[0-9]{1,5}$";
            case PRICE -> "^([0-9]+)(\\.[0-9]+)?$";
            case STUDENTID -> "^S\\d{3}$";
            case PROGRAMID -> "^CA\\d{4}$";
        };

        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    // TextField ekata border color set karanna
    public static boolean setTextColor(lk.ijse.util.TextField fieldType, TextField textField) {
        if (isTextFieldValid(fieldType, textField.getText())) {
            textField.setStyle("-fx-border-color: transparent; -fx-focus-color: transparent;");
            return true;
        } else {
            textField.setStyle("-fx-border-color: red; -fx-border-radius: 5; -fx-border-width: 2;");
            return false;
        }
    }
}

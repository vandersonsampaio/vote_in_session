package br.com.vandersonsampaio.model.exceptions;

public class DateIncorrectFilledException extends Exception {

    public DateIncorrectFilledException(String fieldName) {
        super("Incorrectly filled date field. Field name: " + fieldName);
    }

}

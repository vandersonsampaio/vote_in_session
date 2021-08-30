package br.com.vandersonsampaio.model.exceptions;

public class FieldNotFilledException extends Exception {

    public FieldNotFilledException(String fieldName) {
        super("Field not filled. Field name: " + fieldName);
    }
}

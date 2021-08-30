package br.com.vandersonsampaio.model.exceptions;

public class VoteFieldIncorrectException extends Exception {

    public VoteFieldIncorrectException(String value) {
        super("Incorrect vote field value. Value informed: " + value);
    }
}

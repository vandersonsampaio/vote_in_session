package br.com.vandersonsampaio.model.exceptions;


public class SessionNotFoundException extends Exception {

    public SessionNotFoundException(String idSession) {
        super("Session not found. Session code: " + idSession);
    }
}

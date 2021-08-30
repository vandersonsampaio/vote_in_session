package br.com.vandersonsampaio.model.exceptions;

import java.util.Date;

public class SessionClosedException extends Exception {

    public SessionClosedException(Date opening, Date closing, Date request){
        super("Session closed. Opening Time: " + opening.toString() + " Closing Time: " + closing.toString() + " Request Time: " + request.toString());
    }
}

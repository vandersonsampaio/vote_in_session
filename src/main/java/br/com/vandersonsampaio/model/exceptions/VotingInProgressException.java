package br.com.vandersonsampaio.model.exceptions;

public class VotingInProgressException extends Exception {

    public VotingInProgressException(String idSession) {
        super("Voting is not over yet. Session code: " + idSession);
    }
}

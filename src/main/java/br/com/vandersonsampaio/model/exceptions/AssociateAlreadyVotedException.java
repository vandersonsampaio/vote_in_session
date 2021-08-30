package br.com.vandersonsampaio.model.exceptions;

public class AssociateAlreadyVotedException extends Exception {

    public AssociateAlreadyVotedException(String idAssociate, String idSession) {
        super("Associate has already voted in this session. Associate Code: " +
                idAssociate + " Session Code: " + idSession);
    }
}

package br.com.vandersonsampaio.model.exceptions;

public class AssociateNotFoundException extends Exception {
    
    public AssociateNotFoundException(String idAssociate){
        super("Associate not found. Associate code: " + idAssociate);
    }
}

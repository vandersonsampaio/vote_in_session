package br.com.vandersonsampaio.model.exceptions;

public class AgendaNotFoundException extends Exception {

    public AgendaNotFoundException(String idAgenda) {
        super("Agenda not found. Agenda code: " + idAgenda);
    }
}

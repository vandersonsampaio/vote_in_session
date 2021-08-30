package br.com.vandersonsampaio.service.interfaces;

public interface IVotingService {

    boolean vote(int idSession, int idAssociate, String agree) throws Exception;
}

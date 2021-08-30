package br.com.vandersonsampaio.service.interfaces;

import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.dto.ComputeVoteDTO;

public interface ISessionService {

    Session createSession(int idItemAgenda, String initialTime, int duration) throws Exception;
    ComputeVoteDTO computeVote(int idSession) throws Exception;
    void queueResult(int idSession) throws Exception;
}

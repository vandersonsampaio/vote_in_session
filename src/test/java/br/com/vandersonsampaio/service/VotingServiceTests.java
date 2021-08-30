package br.com.vandersonsampaio.service;

import br.com.vandersonsampaio.client.UserInfoClient;
import br.com.vandersonsampaio.model.Associate;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.Voting;
import br.com.vandersonsampaio.model.dto.UserInfoDTO;
import br.com.vandersonsampaio.model.exceptions.*;
import br.com.vandersonsampaio.model.repository.*;
import br.com.vandersonsampaio.service.implementation.VotingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class VotingServiceTests {

    private VotingService service;
    private IVotingRepository repository;
    private IAssociateRepository associateRepository;
    private ISessionRepository sessionRepository;
    private UserInfoClient client;

    @Before
    public void init(){
        repository = Mockito.mock(IVotingRepository.class);
        associateRepository = Mockito.mock(IAssociateRepository.class);
        sessionRepository = Mockito.mock(ISessionRepository.class);
        client = Mockito.mock(UserInfoClient.class);

        service = new VotingService(repository, associateRepository,
                sessionRepository, client);
    }

    @Test(expected = VoteFieldIncorrectException.class)
    public void votingService_Vote_AgreeInvalid() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Other";

        service.vote(idSession, idAssociate, agree);
    }

    @Test(expected = SessionNotFoundException.class)
    public void votingService_Vote_SessionNotFound() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Não";

        when(sessionRepository.findById(idSession)).thenReturn(Optional.empty());
        service.vote(idSession, idAssociate, agree);
    }

    @Test(expected = AssociateNotFoundException.class)
    public void votingService_Vote_AssociateNotFound() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "No";
        Session session = new Session();
        session.setId(idSession);

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.empty());
        service.vote(idSession, idAssociate, agree);
    }

    @Test(expected = SessionClosedException.class)
    public void votingService_Vote_MomentVoteBeforeDateOpening() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Yes";
        Date opening = new Date(System.currentTimeMillis() + 1000000);
        Date closing = new Date();
        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        Associate associate = Associate.builder().id(idAssociate).build();

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.of(associate));

        service.vote(idSession, idAssociate, agree);
    }

    @Test(expected = SessionClosedException.class)
    public void votingService_Vote_MomentVoteAfterDateClosing() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Sim";
        Date closing = new Date(System.currentTimeMillis() - 1000000);
        Date opening = new Date(System.currentTimeMillis() - 1000100);
        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        Associate associate = Associate.builder().id(idAssociate).build();

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.of(associate));

        service.vote(idSession, idAssociate, agree);
    }

    @Test
    public void votingService_Vote_CPFNotFound() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Yes";
        Date closing = new Date(System.currentTimeMillis() + 10000);
        Date opening = new Date(System.currentTimeMillis() - 10000);
        String cpf = "CPF Test";
        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        Associate associate = Associate.builder().id(idAssociate).cpf(cpf).build();

        Voting voting = new Voting();

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.of(associate));
        when(repository.save(Mockito.any(Voting.class))).thenReturn(voting);
        when(client.getStatusCPF(cpf)).thenReturn(ResponseEntity.notFound().build());

        boolean actual = service.vote(idSession, idAssociate, agree);

        Assert.assertFalse(actual);
    }

    @Test
    public void votingService_Vote_CPFUnable() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Yes";
        Date closing = new Date(System.currentTimeMillis() + 10000);
        Date opening = new Date(System.currentTimeMillis() - 10000);
        String cpf = "CPF Test";
        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        Associate associate = Associate.builder().id(idAssociate).cpf(cpf).build();

        Voting voting = new Voting();
        UserInfoDTO userInfo = new UserInfoDTO("unable_to_vote");

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.of(associate));
        when(repository.save(Mockito.any(Voting.class))).thenReturn(voting);
        when(client.getStatusCPF(cpf)).thenReturn(ResponseEntity.ok(userInfo));

        boolean actual = service.vote(idSession, idAssociate, agree);

        Assert.assertFalse(actual);
    }

    @Test
    public void votingService_Vote_ServiceCPFCrash() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Yes";
        Date closing = new Date(System.currentTimeMillis() + 10000);
        Date opening = new Date(System.currentTimeMillis() - 10000);
        String cpf = "CPF Test";
        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        Associate associate = Associate.builder().id(idAssociate).cpf(cpf).build();

        Voting voting = new Voting();

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.of(associate));
        when(repository.save(Mockito.any(Voting.class))).thenReturn(voting);
        when(client.getStatusCPF(cpf)).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).build());

        boolean actual = service.vote(idSession, idAssociate, agree);

        Assert.assertFalse(actual);
    }

    @Test
    public void votingService_Vote_Success() throws Exception {
        int idSession = 1;
        int idAssociate = 2;
        String agree = "Yes";
        Date closing = new Date(System.currentTimeMillis() + 10000);
        Date opening = new Date(System.currentTimeMillis() - 10000);
        String cpf = "CPF Test";
        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        Associate associate = Associate.builder().id(idAssociate).cpf(cpf).build();

        Voting voting = new Voting();
        UserInfoDTO userInfo = new UserInfoDTO("able_to_vote");

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));
        when(associateRepository.findById(idAssociate)).thenReturn(Optional.of(associate));
        when(repository.save(Mockito.any(Voting.class))).thenReturn(voting);
        when(client.getStatusCPF(cpf)).thenReturn(ResponseEntity.ok(userInfo));

        boolean actual = service.vote(idSession, idAssociate, agree);

        Assert.assertTrue(actual);
    }
}

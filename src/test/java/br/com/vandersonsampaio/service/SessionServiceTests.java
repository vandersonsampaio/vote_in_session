package br.com.vandersonsampaio.service;

import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.Voting;
import br.com.vandersonsampaio.model.dto.ComputeVoteDTO;
import br.com.vandersonsampaio.model.exceptions.AgendaNotFoundException;
import br.com.vandersonsampaio.model.exceptions.DateIncorrectFilledException;
import br.com.vandersonsampaio.model.exceptions.SessionNotFoundException;
import br.com.vandersonsampaio.model.exceptions.VotingInProgressException;
import br.com.vandersonsampaio.model.repository.IAgendaRepository;
import br.com.vandersonsampaio.model.repository.ISessionRepository;
import br.com.vandersonsampaio.service.component.ResultQueueSender;
import br.com.vandersonsampaio.service.implementation.SessionService;
import org.jobrunr.jobs.lambdas.JobLambda;
import org.jobrunr.scheduling.JobScheduler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionServiceTests {

    private SessionService service;
    private JobScheduler jobScheduler;
    private ISessionRepository repository;
    private IAgendaRepository agendaRepository;
    private ResultQueueSender resultQueueSender;

    @Before
    public void init(){
        jobScheduler = Mockito.mock(JobScheduler.class);
        repository = Mockito.mock(ISessionRepository.class);
        agendaRepository = Mockito.mock(IAgendaRepository.class);
        resultQueueSender = Mockito.mock(ResultQueueSender.class);

        service = new SessionService(repository, agendaRepository, jobScheduler, resultQueueSender);
    }

    @Test(expected = AgendaNotFoundException.class)
    public void sessionService_createSession_AgendaNotFound() throws Exception {
        int idAgenda = 1;
        String initialTime = "22:00";
        int duration = 0;

        when(agendaRepository.findById(idAgenda)).thenReturn(Optional.empty());

        service.createSession(idAgenda, initialTime, duration);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void sessionService_createSession_IncorrectInitialTime() throws Exception {
        int idAgenda = 1;
        String initialTime = "26:00";
        int duration = 0;

        when(agendaRepository.findById(idAgenda)).thenReturn(Optional.empty());

        service.createSession(idAgenda, initialTime, duration);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void sessionService_createSession_IncorrectInitialTime_NoNumbers() throws Exception {
        int idAgenda = 1;
        String initialTime = "AA";
        int duration = 0;

        when(agendaRepository.findById(idAgenda)).thenReturn(Optional.empty());

        service.createSession(idAgenda, initialTime, duration);
    }

    @Test
    public void sessionService_createSession_DurationDiffZero() throws Exception {
        int idAgenda = 1;
        int idSession = 10;
        String initialTime = "10:00";
        Date closing = new Date();
        int duration = 10;
        Agenda agenda = Agenda.builder().id(idAgenda).date(closing).build();

        Session session = new Session();
        session.setId(idSession);
        session.setClosing(closing);

        when(agendaRepository.findById(idAgenda)).thenReturn(Optional.of(agenda));
        when(repository.save(Mockito.any())).thenReturn(session);

        service.createSession(idAgenda, initialTime, duration);
        verify(jobScheduler).schedule(Mockito.any(LocalDateTime.class), Mockito.any(JobLambda.class));
    }

    @Test
    public void sessionService_createSession_DurationEqualsZero() throws Exception {
        int idAgenda = 1;
        int idSession = 10;
        String initialTime = "11:00";
        Date closing = new Date();
        int duration = 0;
        Agenda agenda = Agenda.builder().id(idAgenda).date(closing).build();

        Session session = new Session();
        session.setId(idSession);
        session.setClosing(closing);

        when(agendaRepository.findById(idAgenda)).thenReturn(Optional.of(agenda));
        when(repository.save(Mockito.any())).thenReturn(session);

        service.createSession(idAgenda, initialTime, duration);
    }

    @Test(expected = SessionNotFoundException.class)
    public void sessionService_ComputeVote_SessionNotFound() throws Exception {
        int idSession = 1;

        when(repository.findById(idSession)).thenReturn(Optional.empty());

        service.computeVote(idSession);
    }

    @Test(expected = VotingInProgressException.class)
    public void sessionService_ComputeVote_VotingInProgress() throws Exception {
        int idSession = 1;
        Date closing = new Date(System.currentTimeMillis() + 10000);

        Session session = new Session();
        session.setId(idSession);
        session.setClosing(closing);

        when(repository.findById(idSession)).thenReturn(Optional.of(session));

        service.computeVote(idSession);
    }

    @Test
    public void sessionService_ComputeVote_AgreeWinner() throws Exception {
        String result = "Agree";
        String theme = "Theme Test";
        int idSession = 1;
        Date date = new Date(System.currentTimeMillis() - 50000);
        Date opening = new Date(System.currentTimeMillis() - 30000);
        Date closing = new Date(System.currentTimeMillis() - 10000);

        Agenda agenda = Agenda.builder().theme(theme).date(date).build();

        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        session.setAgenda(agenda);
        session.setVotingSet(buildAgreeVotingSet());

        when(repository.findById(idSession)).thenReturn(Optional.of(session));

        ComputeVoteDTO actual = service.computeVote(idSession);

        Assert.assertEquals(theme, actual.getTheme());
        Assert.assertEquals(date, actual.getDateVoting());
        Assert.assertEquals(opening, actual.getInitialVoting());
        Assert.assertEquals(closing, actual.getFinalVoting());
        Assert.assertEquals(result, actual.getOptionWinner());
        Assert.assertEquals(2, actual.getAgreeVotes());
        Assert.assertEquals(1, actual.getDisagreeVotes());
        Assert.assertEquals(3, actual.getValidVotes());
        Assert.assertEquals(1, actual.getInvalidVotes());
        Assert.assertEquals(4, actual.getTotalVotes());
    }

    private Set<Voting> buildAgreeVotingSet(){
        Date date = new Date();
        Voting voting1 = new Voting(1, true, date, false, "Invalid Test", null, null);
        Voting voting2 = new Voting(2, true, date, true, null, null, null);
        Voting voting3 = new Voting(3, false, date, true, null, null, null);
        Voting voting4 = new Voting(4, true, date, true, null, null, null);

        return new HashSet<>(Arrays.asList(voting1, voting2, voting3, voting4));
    }

    @Test
    public void sessionService_ComputeVote_DisagreeWinner() throws Exception {
        String result = "Disagree";
        String theme = "Theme Test";
        int idSession = 1;
        Date date = new Date(System.currentTimeMillis() - 50000);
        Date opening = new Date(System.currentTimeMillis() - 30000);
        Date closing = new Date(System.currentTimeMillis() - 10000);

        Agenda agenda = Agenda.builder().theme(theme).date(date).build();

        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        session.setAgenda(agenda);
        session.setVotingSet(buildDisagreeVotingSet());

        when(repository.findById(idSession)).thenReturn(Optional.of(session));

        ComputeVoteDTO actual = service.computeVote(idSession);

        Assert.assertEquals(theme, actual.getTheme());
        Assert.assertEquals(date, actual.getDateVoting());
        Assert.assertEquals(opening, actual.getInitialVoting());
        Assert.assertEquals(closing, actual.getFinalVoting());
        Assert.assertEquals(result, actual.getOptionWinner());
        Assert.assertEquals(2, actual.getAgreeVotes());
        Assert.assertEquals(3, actual.getDisagreeVotes());
        Assert.assertEquals(5, actual.getValidVotes());
        Assert.assertEquals(1, actual.getInvalidVotes());
        Assert.assertEquals(6, actual.getTotalVotes());
    }

    private Set<Voting> buildDisagreeVotingSet(){
        Date date = new Date();
        Voting voting5 = new Voting(5, false, date, true, null, null, null);
        Voting voting6 = new Voting(6, false, date, true, null, null, null);

        Set<Voting> votingSet = buildAgreeVotingSet();
        votingSet.add(voting5);
        votingSet.add(voting6);

        return votingSet;
    }

    @Test
    public void sessionService_ComputeVote_Draw() throws Exception {
        String result = "Draw";
        String theme = "Theme Test";
        int idSession = 1;
        Date date = new Date(System.currentTimeMillis() - 50000);
        Date opening = new Date(System.currentTimeMillis() - 30000);
        Date closing = new Date(System.currentTimeMillis() - 10000);

        Agenda agenda = Agenda.builder().theme(theme).date(date).build();

        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        session.setAgenda(agenda);
        session.setVotingSet(buildDrawVotingSet());

        when(repository.findById(idSession)).thenReturn(Optional.of(session));

        ComputeVoteDTO actual = service.computeVote(idSession);

        Assert.assertEquals(theme, actual.getTheme());
        Assert.assertEquals(date, actual.getDateVoting());
        Assert.assertEquals(opening, actual.getInitialVoting());
        Assert.assertEquals(closing, actual.getFinalVoting());
        Assert.assertEquals(result, actual.getOptionWinner());
        Assert.assertEquals(3, actual.getAgreeVotes());
        Assert.assertEquals(3, actual.getDisagreeVotes());
        Assert.assertEquals(6, actual.getValidVotes());
        Assert.assertEquals(1, actual.getInvalidVotes());
        Assert.assertEquals(7, actual.getTotalVotes());
    }

    private Set<Voting> buildDrawVotingSet(){
        Date date = new Date();
        Voting voting7 = new Voting(7, true, date, true, null, null, null);

        Set<Voting> votingSet = buildDisagreeVotingSet();
        votingSet.add(voting7);

        return votingSet;
    }

    @Test
    public void sessionService_QueueResult_Success() throws Exception {
        String theme = "Theme Test";
        int idSession = 1;
        Date date = new Date(System.currentTimeMillis() - 50000);
        Date opening = new Date(System.currentTimeMillis() - 30000);
        Date closing = new Date(System.currentTimeMillis() - 10000);

        Agenda agenda = Agenda.builder().theme(theme).date(date).build();

        Session session = new Session();
        session.setId(idSession);
        session.setOpening(opening);
        session.setClosing(closing);
        session.setAgenda(agenda);
        session.setVotingSet(buildDrawVotingSet());

        when(repository.findById(idSession)).thenReturn(Optional.of(session));

        ComputeVoteDTO result = new ComputeVoteDTO(theme, date, opening, closing,
                7, 6, 1, 3, 3, "Draw");

        service.queueResult(idSession);
        verify(resultQueueSender).send(result.toString());
    }
}

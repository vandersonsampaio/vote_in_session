package br.com.vandersonsampaio.controller;

import br.com.vandersonsampaio.mapper.SessionMapper;
import br.com.vandersonsampaio.mapper.SessionMapperImpl;
import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.dto.ComputeVoteDTO;
import br.com.vandersonsampaio.model.dto.request.RegisterSessionDTO;
import br.com.vandersonsampaio.model.dto.response.RegisteredSessionDTO;
import br.com.vandersonsampaio.service.interfaces.ISessionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.mockito.Mockito.when;

public class SessionControllerTests {

    private ISessionService service;
    private SessionController controller;

    @Before
    public void setUp(){
        service = Mockito.mock(ISessionService.class);
        SessionMapper mapper = new SessionMapperImpl();
        controller = new SessionController(service, mapper);
    }

    @Test
    public void sessionTest_RegisterSession() throws Exception {
        Date date = new Date();
        int idSession = 34;
        int idAgenda = 1;
        int minutesDuration = 10;
        Agenda agenda = Agenda.builder().id(idAgenda).build();
        String initialTime = "11:59";

        RegisterSessionDTO dto = new RegisterSessionDTO(idAgenda, minutesDuration, initialTime);
        Session session = new Session(idSession, date, date, minutesDuration, agenda, null);

        when(service.createSession(idAgenda, initialTime, minutesDuration)).thenReturn(session);

        ResponseEntity<RegisteredSessionDTO> response = controller.registerSession(dto);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());

        RegisteredSessionDTO actual = response.getBody();
        Assert.assertEquals(idSession, actual.getId());
        Assert.assertEquals(date, actual.getOpening());
        Assert.assertEquals(date, actual.getClosing());
        Assert.assertEquals(minutesDuration, actual.getMinutesDuration());
    }

    @Test
    public void sessionTest_ComputeVote() throws Exception {
        Date now = new Date();
        int idSession = 8;
        String theme = "Theme Test";
        long totalVotes = 5;
        long validVotes = 3;
        long invalidVotes = 2;
        long agreeVotes = 2;
        long disagreeVotes = 1;
        String optionWinner = "Agree";
        ComputeVoteDTO dto = new ComputeVoteDTO(theme, now, now, now, totalVotes, validVotes, invalidVotes,
                agreeVotes, disagreeVotes, optionWinner);

        when(service.computeVote(idSession)).thenReturn(dto);
        ResponseEntity<ComputeVoteDTO> response = controller.computeVote(idSession);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());

        ComputeVoteDTO actual = response.getBody();
        Assert.assertEquals(theme, actual.getTheme());
        Assert.assertEquals(now, actual.getDateVoting());
        Assert.assertEquals(now, actual.getInitialVoting());
        Assert.assertEquals(now, actual.getFinalVoting());
        Assert.assertEquals(totalVotes, actual.getTotalVotes());
        Assert.assertEquals(validVotes, actual.getValidVotes());
        Assert.assertEquals(invalidVotes, actual.getInvalidVotes());
        Assert.assertEquals(agreeVotes, actual.getAgreeVotes());
        Assert.assertEquals(disagreeVotes, actual.getDisagreeVotes());
        Assert.assertEquals(optionWinner, actual.getOptionWinner());
    }
}

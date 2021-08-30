package br.com.vandersonsampaio.controller;

import br.com.vandersonsampaio.model.dto.request.RegisterVoteDTO;
import br.com.vandersonsampaio.model.dto.response.MessageDTO;
import br.com.vandersonsampaio.service.interfaces.IVotingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

public class VotingControllerTests {

    private IVotingService service;
    private VotingController controller;

    @Before
    public void setUp(){
        service = Mockito.mock(IVotingService.class);
        controller = new VotingController(service);
    }

    @Test
    public void votingTest_RegisterVote_Success() throws Exception {
        int idSession = 9;
        int idAssociate = 7;
        String agree = "Yes";
        RegisterVoteDTO dto = RegisterVoteDTO.builder().idSession(idSession).idAssociate(idAssociate).agree(agree).build();

        when(service.vote(idSession, idAssociate, agree)).thenReturn(true);

        ResponseEntity<MessageDTO> response = controller.registerVote(dto);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().getStatus().contains("Successfully"));
    }

    @Test
    public void votingTest_RegisterVote_Error() throws Exception {
        int idSession = 9;
        int idAssociate = 7;
        String agree = "Nao";
        RegisterVoteDTO dto = RegisterVoteDTO.builder().idSession(idSession).idAssociate(idAssociate).agree(agree).build();

        when(service.vote(idSession, idAssociate, agree)).thenReturn(false);

        ResponseEntity<MessageDTO> response = controller.registerVote(dto);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().getStatus().contains("Error"));
    }
}

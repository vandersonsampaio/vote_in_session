package br.com.vandersonsampaio.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.vandersonsampaio.Application;
import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.Associate;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.Voting;
import br.com.vandersonsampaio.model.dto.request.RegisterAgendaDTO;
import br.com.vandersonsampaio.model.dto.request.RegisterVoteDTO;
import br.com.vandersonsampaio.model.repository.IAgendaRepository;
import br.com.vandersonsampaio.model.repository.IAssociateRepository;
import br.com.vandersonsampaio.model.repository.ISessionRepository;
import br.com.vandersonsampaio.model.repository.IVotingRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AllIntegrationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ISessionRepository sessionRepository;
    @Autowired
    private IAgendaRepository agendaRepository;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IAssociateRepository associateRepository;

    @Test
    public void agendaController_register_thenStatus200() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        RegisterAgendaDTO dto = new RegisterAgendaDTO("Manager", "Responsible",
                dateTime.toLocalDate(), "22:00", null, "Theme", null,
                Collections.singletonList("Only one item"));

        mvc.perform(post("/agenda/v1/register").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto))).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void votingController_Vote_VoteDuplicate() throws Exception {
        //Database preparation
        Date date = new Date();
        Agenda agenda = new Agenda(0, "Manager", "Responsible", date, date, null,"Theme", null, null, null);
        agenda = agendaRepository.save(agenda);
        Date closing = new Date(System.currentTimeMillis() + 100000);
        Session session = new Session(0, date, closing, 10, agenda, null);
        session = sessionRepository.save(session);
        Associate associate = new Associate(1, "Name", "CPF", date, null);
        associate = associateRepository.save(associate);
        Voting vote = new Voting(0, false, date, true, null, session, associate);
        votingRepository.save(vote);

        RegisterVoteDTO dto = new RegisterVoteDTO(session.getId(), associate.getId(), "não");

        mvc.perform(post("/voting/v1/register").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto))).andDo(print()).andExpect(status().isBadRequest());
    }

    static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}

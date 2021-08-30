package br.com.vandersonsampaio.controller;

import br.com.vandersonsampaio.mapper.AgendaMapper;
import br.com.vandersonsampaio.mapper.AgendaMapperImpl;
import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.ItemAgenda;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.dto.request.RegisterAgendaDTO;
import br.com.vandersonsampaio.model.dto.response.RegisteredAgendaDTO;
import br.com.vandersonsampaio.service.interfaces.IAgendaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AgendaControllerTests {

    private IAgendaService service;
    private AgendaController controller;

    @Before
    public void setUp(){
        service = Mockito.mock(IAgendaService.class);
        AgendaMapper mapper = new AgendaMapperImpl();
        controller = new AgendaController(service, mapper);
    }

    @Test
    public void agendaTest_RegisterAgenda() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        int id = 1;
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        LocalDate date = dateTime.toLocalDate();
        String initialTime = "10:00";
        Date dtInitialTime = Date.from( LocalTime.parse(initialTime).atDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth())).atZone(ZoneId.systemDefault()).toInstant());
        int initialTimeHours = 10;
        int initialTimeMinutes = 0;
        String finalTime = "11:00";
        Date dtFinalTime = Date.from( LocalTime.parse(finalTime).atDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth())).atZone(ZoneId.systemDefault()).toInstant());
        int finalTimeHours = 11;
        int finalTimeMinutes = 0;
        String theme = "Theme Test";
        String observations = "Observation";
        List<String> itemsAgenda = Collections.singletonList("Item");
        Session session = new Session();
        Set<ItemAgenda> items = Collections.singleton(new ItemAgenda(id, "Item", null));

        RegisterAgendaDTO registerDTO = new RegisterAgendaDTO(manager, responsible, date, initialTime,
                finalTime, theme, observations, itemsAgenda);
        Agenda agenda = new Agenda(id, manager, responsible, Date.from(dateTime.toInstant(ZoneOffset.UTC)),
                dtInitialTime, dtFinalTime, theme, observations, session, items);

        when(service.create(any(Agenda.class))).thenReturn(agenda);

        ResponseEntity<RegisteredAgendaDTO> response = controller.registerAgenda(registerDTO);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());

        RegisteredAgendaDTO actual = response.getBody();
        Assert.assertEquals(id, actual.getId());
        Assert.assertEquals(manager, actual.getManager());
        Assert.assertEquals(responsible, actual.getResponsible());
        Assert.assertEquals(date, actual.getDate());
        Assert.assertEquals(initialTimeHours, actual.getInitialTime().getHour());
        Assert.assertEquals(initialTimeMinutes, actual.getInitialTime().getMinute());
        Assert.assertEquals(finalTimeHours, actual.getFinalTime().getHour());
        Assert.assertEquals(finalTimeMinutes, actual.getFinalTime().getMinute());
        Assert.assertEquals(theme, actual.getTheme());
        Assert.assertEquals(observations, actual.getObservations());
        Assert.assertEquals(itemsAgenda, actual.getItemsAgenda());
    }
}

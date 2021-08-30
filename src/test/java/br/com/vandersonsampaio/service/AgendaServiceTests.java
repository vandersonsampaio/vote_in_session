package br.com.vandersonsampaio.service;

import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.ItemAgenda;
import br.com.vandersonsampaio.model.exceptions.DateIncorrectFilledException;
import br.com.vandersonsampaio.model.exceptions.FieldNotFilledException;
import br.com.vandersonsampaio.model.repository.IAgendaRepository;
import br.com.vandersonsampaio.model.repository.IItemAgendaRepository;
import br.com.vandersonsampaio.service.implementation.AgendaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

public class AgendaServiceTests {

    private AgendaService service;
    private IAgendaRepository repository;

    @Before
    public void init(){
        repository = Mockito.mock(IAgendaRepository.class);
        IItemAgendaRepository itemAgendaRepository = Mockito.mock(IItemAgendaRepository.class);

        service = new AgendaService(repository, itemAgendaRepository);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_AgendaIsNull() throws Exception {
        service.create(null);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_ManagerIsNull() throws Exception {
        Agenda agenda = new Agenda();
        agenda.setManager(null);
        service.create(agenda);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_ManagerIsEmpty() throws Exception {
        String manager = "";

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        service.create(agenda);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_ResponsibleIsNull() throws Exception {
        String manager = "Manager Test";

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(null);
        service.create(agenda);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_ResponsibleIsEmpty() throws Exception {
        String manager = "Manager Test";
        String responsible = "";

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        service.create(agenda);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_ThemeIsNull() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(null);
        service.create(agenda);
    }

    @Test(expected = FieldNotFilledException.class)
    public void agendaService_Create_ThemeIsEmpty() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "";

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        service.create(agenda);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void agendaService_Create_DateIsNull() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(null);

        service.create(agenda);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void agendaService_Create_DateBeforeToday() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        Date date = new Date(System.currentTimeMillis() - 1000000000);

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);

        service.create(agenda);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void agendaService_Create_InitialTimeIsNull() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        Date date = new Date();

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);
        agenda.setInitialTime(null);

        service.create(agenda);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void agendaService_Create_InitialTimeBeforeToday() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        Date date = new Date();
        Date initialTime = new Date(System.currentTimeMillis() - 1000000000);

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);
        agenda.setInitialTime(initialTime);

        service.create(agenda);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void agendaService_Create_FinalTimeEqualsInitialTime() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        Date date = new Date();
        Date initialTime = new Date(System.currentTimeMillis() + 1000000000);

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);
        agenda.setInitialTime(initialTime);
        agenda.setFinalTime(initialTime);

        service.create(agenda);
    }

    @Test(expected = DateIncorrectFilledException.class)
    public void agendaService_Create_FinalTimeBeforeInitialTime() throws Exception {
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        Date date = new Date();
        Date initialTime = new Date(System.currentTimeMillis() + 1000000000);
        Date finalTime = new Date(System.currentTimeMillis() + 1000000);

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);
        agenda.setInitialTime(initialTime);
        agenda.setFinalTime(finalTime);

        service.create(agenda);
    }

    @Test
    public void agendaService_Create_ItemAgendaIsNull() throws Exception {
        int id = 1;
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        Date date = new Date();
        Date finalTime = new Date(System.currentTimeMillis() + 100000);

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);
        agenda.setInitialTime(date);
        agenda.setFinalTime(finalTime);
        agenda.setItemAgendaSet(null);

        Agenda saved = new Agenda(id, manager, responsible, date, date, finalTime, theme, "", null, new HashSet<>());

        when(repository.save(agenda)).thenReturn(saved);

        Agenda actual = service.create(agenda);

        Assert.assertEquals(id, actual.getId());
        Assert.assertEquals(manager, actual.getManager());

        Assert.assertEquals(responsible, actual.getResponsible());
        Assert.assertEquals(date, actual.getDate());
        Assert.assertEquals(date, actual.getInitialTime());
        Assert.assertEquals(finalTime, actual.getFinalTime());
        Assert.assertEquals(theme, actual.getTheme());

        Assert.assertEquals(0, actual.getItemAgendaSet().size());
    }

    @Test
    public void agendaService_Create_Success() throws Exception {
        int id = 1;
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        String theme = "Theme Test";
        String observations = "Observations Test";
        ItemAgenda itemAgenda = new ItemAgenda(2, "Item", null);
        Set<ItemAgenda> itemAgendaSet = Collections.singleton(itemAgenda);
        Date date = new Date();

        Agenda agenda = new Agenda();
        agenda.setManager(manager);
        agenda.setResponsible(responsible);
        agenda.setTheme(theme);
        agenda.setDate(date);
        agenda.setInitialTime(date);
        agenda.setObservations(observations);
        agenda.setItemAgendaSet(itemAgendaSet);

        Agenda saved = new Agenda(id, manager, responsible, date, date, null, theme, observations, null, itemAgendaSet);

        when(repository.save(agenda)).thenReturn(saved);

        Agenda actual = service.create(agenda);

        Assert.assertEquals(id, actual.getId());
        Assert.assertEquals(manager, actual.getManager());

        Assert.assertEquals(responsible, actual.getResponsible());
        Assert.assertEquals(date, actual.getDate());
        Assert.assertEquals(date, actual.getInitialTime());
        Assert.assertNull(actual.getFinalTime());
        Assert.assertEquals(theme, actual.getTheme());
        Assert.assertEquals(observations, actual.getObservations());

        Assert.assertEquals(1, actual.getItemAgendaSet().size());
        Assert.assertEquals(itemAgenda, actual.getItemAgendaSet().stream().findAny().get());
    }
}

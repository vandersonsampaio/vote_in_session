package br.com.vandersonsampaio.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AgendaTests {

    @Test
    public void agendaTest_BuilderGetterAndToString(){
        int id = 1;
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        Date date = new Date();
        Date initialTime = new Date();
        Date finalTime = new Date();
        String theme = "Theme Test";
        String observations = "Observations";

        Session session = new Session();
        Set<ItemAgenda> itemAgendaSet = new HashSet<>();

        Agenda agenda = Agenda.builder().id(id)
                .manager(manager).responsible(responsible)
                .date(date).initialTime(initialTime)
                .finalTime(finalTime).theme(theme)
                .observations(observations).session(session)
                .itemAgendaSet(itemAgendaSet).build();

        Assert.assertEquals(id, agenda.getId());
        Assert.assertEquals(manager, agenda.getManager());
        Assert.assertEquals(responsible, agenda.getResponsible());
        Assert.assertEquals(date, agenda.getDate());
        Assert.assertEquals(initialTime, agenda.getInitialTime());
        Assert.assertEquals(finalTime, agenda.getFinalTime());
        Assert.assertEquals(theme, agenda.getTheme());
        Assert.assertEquals(observations, agenda.getObservations());
        Assert.assertEquals(session, agenda.getSession());
        Assert.assertEquals(itemAgendaSet, agenda.getItemAgendaSet());
        Assert.assertNotEquals("", agenda.toString());
    }

    @Test
    public void agendaTest_AllArgs(){
        int id = 1;
        String manager = "Manager Test";
        String responsible = "Responsible Test";
        Date date = new Date();
        Date initialTime = new Date();
        Date finalTime = new Date();
        String theme = "Theme Test";
        String observations = "Observations";

        Session session = new Session();
        Set<ItemAgenda> itemAgendaSet = new HashSet<>();

        Agenda agenda = new Agenda(id, manager, responsible,
                date, initialTime, finalTime, theme, observations,
                session, itemAgendaSet);

        Assert.assertEquals(id, agenda.getId());
        Assert.assertEquals(manager, agenda.getManager());
        Assert.assertEquals(responsible, agenda.getResponsible());
        Assert.assertEquals(date, agenda.getDate());
        Assert.assertEquals(initialTime, agenda.getInitialTime());
        Assert.assertEquals(finalTime, agenda.getFinalTime());
        Assert.assertEquals(theme, agenda.getTheme());
        Assert.assertEquals(observations, agenda.getObservations());
        Assert.assertEquals(session, agenda.getSession());
        Assert.assertEquals(itemAgendaSet, agenda.getItemAgendaSet());
    }

    @Test
    public void agendaTest_AgendaBuilder(){
        int id = 1;
        Agenda.AgendaBuilder builder = Agenda.builder().id(id);
        Assert.assertTrue(builder.toString().contains(Integer.toString(id)));
    }
}

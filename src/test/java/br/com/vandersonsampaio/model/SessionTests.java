package br.com.vandersonsampaio.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SessionTests {

    @Test
    public void sessionTest_AllArgsGetterAndToString(){
        int id = 1;
        Date opening = new Date();
        Date closing = new Date();
        int minutesDuration = 2;

        Agenda agenda = new Agenda();
        Set<Voting> votingSet = new HashSet<>();

        Session session = new Session(id, opening, closing, minutesDuration, agenda, votingSet);

        Assert.assertEquals(id, session.getId());
        Assert.assertEquals(opening, session.getOpening());
        Assert.assertEquals(closing, session.getClosing());
        Assert.assertEquals(minutesDuration, session.getMinutesDuration());
        Assert.assertEquals(agenda, session.getAgenda());
        Assert.assertEquals(votingSet, session.getVotingSet());
        Assert.assertNotEquals("", session.toString());
    }

    @Test
    public void sessionTest_NoArgsAndSetter(){
        int id = 1;
        Date opening = new Date();
        Date closing = new Date();
        int minutesDuration = 2;

        Agenda agenda = new Agenda();
        Set<Voting> votingSet = new HashSet<>();

        Session session = new Session();
        session.setId(id);
        session.setOpening(opening);
        session.setClosing(closing);
        session.setMinutesDuration(minutesDuration);
        session.setAgenda(agenda);
        session.setVotingSet(votingSet);

        Assert.assertEquals(id, session.getId());
        Assert.assertEquals(opening, session.getOpening());
        Assert.assertEquals(closing, session.getClosing());
        Assert.assertEquals(minutesDuration, session.getMinutesDuration());
        Assert.assertEquals(agenda, session.getAgenda());
        Assert.assertEquals(votingSet, session.getVotingSet());
    }

    @Test
    public void sessionTest_SpecificConstructor() {
        Date opening = new Date();
        Date closing = new Date();
        int minutesDuration = 2;

        Agenda agenda = new Agenda();

        Session session = new Session(opening, closing, minutesDuration, agenda);

        Assert.assertEquals(opening, session.getOpening());
        Assert.assertEquals(closing, session.getClosing());
        Assert.assertEquals(minutesDuration, session.getMinutesDuration());
        Assert.assertEquals(agenda, session.getAgenda());
    }
}

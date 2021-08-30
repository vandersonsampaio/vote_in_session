package br.com.vandersonsampaio.model;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class VotingTests {

    @Test
    public void votingTest_AllArgsGetterAndToString(){
        int id = 1;
        Date moment = new Date();
        String invalidReason = "Invalid Test";

        Session session = new Session();
        Associate associate = new Associate();

        Voting voting = new Voting(id, true, moment, false, invalidReason, session, associate);

        Assert.assertEquals(id, voting.getId());
        Assert.assertTrue(voting.isAgree());
        Assert.assertEquals(moment, voting.getMoment());
        Assert.assertFalse(voting.isValid());
        Assert.assertEquals(invalidReason, voting.getInvalidReason());
        Assert.assertEquals(session, voting.getSession());
        Assert.assertEquals(associate, voting.getAssociate());
        Assert.assertNotEquals("", voting.toString());
    }

    @Test
    public void votingTest_NoArgsAndSetter(){
        int id = 1;
        String invalidReason = "Invalid Test";

        Session session = new Session();
        Associate associate = new Associate();

        Voting voting = new Voting();
        voting.setId(id);
        voting.setAgree(true);
        voting.setValid(false);
        voting.setInvalidReason(invalidReason);
        voting.setSession(session);
        voting.setAssociate(associate);

        Assert.assertEquals(id, voting.getId());
        Assert.assertTrue(voting.isAgree());

        try { Thread.sleep(1000); } catch (InterruptedException ex) { LoggerFactory.getLogger(this.getClass()).debug(ex.getMessage()); }
        Date now = new Date();

        Assert.assertTrue(voting.getMoment().before(now));
        voting.setMoment(now);
        Assert.assertEquals(now, voting.getMoment());

        Assert.assertFalse(voting.isValid());
        Assert.assertEquals(invalidReason, voting.getInvalidReason());
        Assert.assertEquals(session, voting.getSession());
        Assert.assertEquals(associate, voting.getAssociate());
    }
}

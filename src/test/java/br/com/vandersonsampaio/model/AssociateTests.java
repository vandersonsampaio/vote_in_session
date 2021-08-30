package br.com.vandersonsampaio.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AssociateTests {

    @Test
    public void associateTest_BuilderGetterAndToString(){
        int id = 1;
        String name = "name test";
        String cpf = "cpf test";
        Date association = new Date();

        Set<Voting> votingSet = new HashSet<>();

        Associate associate = Associate.builder().id(id)
                .name(name).cpf(cpf).association(association)
                .votingSet(votingSet).build();

        Assert.assertEquals(id, associate.getId());
        Assert.assertEquals(name, associate.getName());
        Assert.assertEquals(cpf, associate.getCpf());
        Assert.assertEquals(association, associate.getAssociation());
        Assert.assertEquals(votingSet, associate.getVotingSet());
        Assert.assertNotEquals("", associate.toString());
    }

    @Test
    public void associateTest_AllArgs(){
        int id = 1;
        String name = "name test";
        String cpf = "cpf test";
        Date association = new Date();

        Set<Voting> votingSet = new HashSet<>();

        Associate associate = new Associate(id, name, cpf, association, votingSet);

        Assert.assertEquals(id, associate.getId());
        Assert.assertEquals(name, associate.getName());
        Assert.assertEquals(cpf, associate.getCpf());
        Assert.assertEquals(association, associate.getAssociation());
        Assert.assertEquals(votingSet, associate.getVotingSet());
    }

    @Test
    public void associateTest_AssociateBuilder(){
        int id = 1;
        Associate.AssociateBuilder builder = Associate.builder().id(id);
        Assert.assertTrue(builder.toString().contains(Integer.toString(id)));
    }
}

package br.com.vandersonsampaio.model;

import org.junit.Assert;
import org.junit.Test;

public class ItemAgendaTests {

    @Test
    public void itemAgendaTest_BuilderGetterAndToString(){
        int id = 1;
        String description = "description default";

        Agenda agenda = new Agenda();

        ItemAgenda itemAgenda = ItemAgenda.builder().id(id)
                .description(description).agenda(agenda).build();

        Assert.assertEquals(id, itemAgenda.getId());
        Assert.assertEquals(description, itemAgenda.getDescription());
        Assert.assertEquals(agenda, itemAgenda.getAgenda());
        Assert.assertNotEquals("", itemAgenda.toString());
    }

    @Test
    public void itemAgendaTest_NoArgsAndSetter(){
        int id = 1;
        String description = "description default";

        Agenda agenda = new Agenda();

        ItemAgenda itemAgenda = new ItemAgenda();
        itemAgenda.setAgenda(agenda);
        itemAgenda.setId(id);
        itemAgenda.setDescription(description);

        Assert.assertEquals(id, itemAgenda.getId());
        Assert.assertEquals(description, itemAgenda.getDescription());
        Assert.assertEquals(agenda, itemAgenda.getAgenda());
    }

    @Test
    public void itemAgendaTest_ItemAgendaBuilder(){
        int id = 1;
        ItemAgenda.ItemAgendaBuilder builder = ItemAgenda.builder().id(id);
        Assert.assertTrue(builder.toString().contains(Integer.toString(id)));
    }
}

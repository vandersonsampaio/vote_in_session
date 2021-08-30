package br.com.vandersonsampaio.service.implementation;

import br.com.vandersonsampaio.model.exceptions.DateIncorrectFilledException;
import br.com.vandersonsampaio.model.exceptions.FieldNotFilledException;
import br.com.vandersonsampaio.model.repository.IItemAgendaRepository;
import br.com.vandersonsampaio.service.interfaces.IAgendaService;
import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.repository.IAgendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashSet;

@Service
public class AgendaService implements IAgendaService {

    private final IAgendaRepository repository;
    private final IItemAgendaRepository itemAgendaRepository;

    public AgendaService(IAgendaRepository repository,
                         IItemAgendaRepository itemAgendaRepository){
        this.repository = repository;
        this.itemAgendaRepository = itemAgendaRepository;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public Agenda create(Agenda agenda) throws Exception {
        //Validation Fields
        if(agenda == null || agenda.getManager() == null || agenda.getManager().trim().length() == 0) throw new FieldNotFilledException("Agenda.Manager");
        if(agenda.getResponsible() == null || agenda.getResponsible().trim().length() == 0) throw new FieldNotFilledException("Agenda.Responsible");
        if(agenda.getTheme() == null || agenda.getTheme().trim().length() == 0) throw new FieldNotFilledException("Agenda.Theme");

        Calendar calendar = buildCalendarTodayMiddleNight();
        if(agenda.getDate() == null || agenda.getDate().before(calendar.getTime())) throw new DateIncorrectFilledException("Agenda.Date");
        if(agenda.getInitialTime() == null || agenda.getInitialTime().before(calendar.getTime())) throw new DateIncorrectFilledException("Agenda.InitialTime");
        if(agenda.getFinalTime() != null && agenda.getFinalTime().compareTo(agenda.getInitialTime()) < 1) throw new DateIncorrectFilledException("Agenda.FinalTime");

        if(agenda.getItemAgendaSet() == null) agenda.setItemAgendaSet(new HashSet<>());

        //Insert in Database
        Agenda agendaSaved = repository.save(agenda);
        agenda.getItemAgendaSet().forEach(i -> i.setAgenda(agendaSaved));
        itemAgendaRepository.saveAll(agenda.getItemAgendaSet());
        return agendaSaved;
    }

    private Calendar buildCalendarTodayMiddleNight(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar;
    }
}

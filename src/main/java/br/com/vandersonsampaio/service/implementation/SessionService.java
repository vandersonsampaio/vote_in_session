package br.com.vandersonsampaio.service.implementation;

import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.Voting;
import br.com.vandersonsampaio.model.dto.ComputeVoteDTO;
import br.com.vandersonsampaio.model.exceptions.AgendaNotFoundException;
import br.com.vandersonsampaio.model.exceptions.DateIncorrectFilledException;
import br.com.vandersonsampaio.model.exceptions.SessionNotFoundException;
import br.com.vandersonsampaio.model.exceptions.VotingInProgressException;
import br.com.vandersonsampaio.model.repository.IAgendaRepository;
import br.com.vandersonsampaio.model.repository.ISessionRepository;
import br.com.vandersonsampaio.service.component.ResultQueueSender;
import br.com.vandersonsampaio.service.interfaces.ISessionService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class SessionService implements ISessionService {

    private static final int DEFAULT_DURATION = 1;
    private final JobScheduler jobScheduler;
    private final ISessionRepository repository;
    private final IAgendaRepository agendaRepository;
    private final ResultQueueSender resultQueueSender;

    public SessionService(ISessionRepository repository,
                          IAgendaRepository agendaRepository,
                          JobScheduler jobScheduler,
                          ResultQueueSender resultQueueSender) {
        this.jobScheduler = jobScheduler;
        this.repository = repository;
        this.agendaRepository = agendaRepository;
        this.resultQueueSender = resultQueueSender;
    }

    @Override
    public Session createSession(int idAgenda, String initialTime, int duration) throws Exception {
        //Validation Fields
        if(initialTime == null || !initialTime.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) throw new DateIncorrectFilledException("Session.InitialTime");
        Agenda agenda = agendaRepository.findById(idAgenda).orElseThrow(() -> new AgendaNotFoundException(Integer.toString(idAgenda)));

        //Adjustment in the Format of the Values
        LocalTime dtInitialTime = LocalTime.parse(initialTime);
        int realDuration = duration == 0 ? DEFAULT_DURATION : duration;

        Calendar initialDate = buildCalendar(agenda.getDate());
        initialDate.set(Calendar.HOUR_OF_DAY, dtInitialTime.getHour());
        initialDate.set(Calendar.MINUTE, dtInitialTime.getMinute());

        Calendar finalDate = buildCalendar(initialDate.getTime());
        finalDate.add(Calendar.MINUTE, realDuration);

        //Insert in Database
        Session session = new Session(initialDate.getTime(), finalDate.getTime(), realDuration, agenda);
        session = repository.save(session);

        schedulePublication(session.getId(), session.getClosing());
        return session;
    }

    private Calendar buildCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    private void schedulePublication(int idSession, Date time){
        //Job creation for queued publishing at the end of the session
        Calendar calendar = buildCalendar(time);
        calendar.add(Calendar.MINUTE, 1);

        TimeZone tz = calendar.getTimeZone();
        ZoneId zoneId = tz.toZoneId();
        LocalDateTime scheduleAt = LocalDateTime.ofInstant(calendar.toInstant(), zoneId);

        jobScheduler.schedule(scheduleAt, () -> queueResult(idSession));
    }

    @Override
    public ComputeVoteDTO computeVote(int idSession) throws Exception {
        //Validation Fields
        Session session = repository.findById(idSession).orElseThrow(() -> new SessionNotFoundException(Integer.toString(idSession)));
        if(session.getClosing().after((new Date()))) throw new VotingInProgressException(Integer.toString(idSession));

        //Calculating Totals
        List<Voting> votingList = new ArrayList<>(session.getVotingSet());
        long totalVotes = votingList.size();
        long totalValid = votingList.stream().filter(Voting::isValid).count();
        long totalInvalid = votingList.stream().filter(v -> !v.isValid()).count();
        long totalAgree = votingList.stream().filter(v -> v.isValid() && v.isAgree()).count();
        long totalDisagree = votingList.stream().filter(v -> v.isValid() && !v.isAgree()).count();
        String winner = totalAgree > totalDisagree ? "Agree" :
                (totalAgree < totalDisagree ? "Disagree" : "Draw");

        //Building Response DTO
        return new ComputeVoteDTO(
                session.getAgenda().getTheme(),
                session.getAgenda().getDate(),
                session.getOpening(), session.getClosing(),
                totalVotes, totalValid, totalInvalid, totalAgree,
                totalDisagree, winner);
    }

    @Override
    @Job(name = "PublishResult")
    public void queueResult(int idSession) throws Exception {
        //Publishing Result in the Queue
        LoggerFactory.getLogger(this.getClass()).info("Publishing to the queue. Session {} result", idSession);
        ComputeVoteDTO result = this.computeVote(idSession);
        resultQueueSender.send(result.toString());
    }
}

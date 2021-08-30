package br.com.vandersonsampaio.service.implementation;

import br.com.vandersonsampaio.client.UserInfoClient;
import br.com.vandersonsampaio.model.Associate;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.Voting;
import br.com.vandersonsampaio.model.dto.UserInfoDTO;
import br.com.vandersonsampaio.model.exceptions.*;
import br.com.vandersonsampaio.model.repository.IAssociateRepository;
import br.com.vandersonsampaio.model.repository.ISessionRepository;
import br.com.vandersonsampaio.model.repository.IVotingRepository;
import br.com.vandersonsampaio.service.interfaces.IVotingService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
public class VotingService implements IVotingService {

    private static final String CPF_INVALID = "Invalid CPF";
    private static final String CPF_UNABLE_TO_VOTE = "CPF Unable to Vote";
    private static final String CPF_SERVICE_OFF = "Situation not mapped in the CPF service";
    private static final String CPF_SERVICE_STATUS_SUCCESS = "able_to_vote";

    private final IVotingRepository repository;
    private final IAssociateRepository associateRepository;
    private final ISessionRepository sessionRepository;
    private final UserInfoClient client;

    @Value("${vote-session.voting.margin}")
    private int interval;

    public VotingService(IVotingRepository repository,
                         IAssociateRepository associateRepository,
                         ISessionRepository sessionRepository,
                         UserInfoClient client) {
        this.repository = repository;
        this.client = client;
        this.associateRepository = associateRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public boolean vote(int idSession, int idAssociate, String agree) throws Exception {
        //Create object to set Date
        Voting voting = new Voting();

        //Validation Fields
        boolean isAgree = removeAccent(agree).compareToIgnoreCase("sim") == 0 ||
                removeAccent(agree).compareToIgnoreCase("yes") == 0;

        if( !isAgree && removeAccent(agree).compareToIgnoreCase("no") != 0 &&
                removeAccent(agree).compareToIgnoreCase("nao") != 0) throw new VoteFieldIncorrectException(agree);

        Session session = sessionRepository.findById(idSession).orElseThrow(() -> new SessionNotFoundException(Integer.toString(idSession)));
        Associate associate = associateRepository.findById(idAssociate).orElseThrow(() -> new AssociateNotFoundException(Integer.toString(idAssociate)));

        //Set values of object
        voting.setSession(session);
        voting.setAssociate(associate);
        voting.setAgree(isAgree);

        //More Validations
        Calendar momentVote = buildCalendar(voting.getMoment());
        Calendar dateOpening = buildCalendar(session.getOpening());
        Calendar dateClosing = buildCalendar(session.getClosing());
        dateClosing.add(Calendar.SECOND, interval);

        if(momentVote.before(dateOpening) || momentVote.after(dateClosing))
            throw new SessionClosedException(session.getOpening(), session.getClosing(), voting.getMoment());

        //Insert in database
        try {
            voting = repository.save(voting);
        } catch (Exception ex) {
            //Catch duplicate vote
            throw new AssociateAlreadyVotedException(Integer.toString(idAssociate), Integer.toString(idSession));
        }

        //Validation Associate's CPF
        validateCPF(voting, associate.getCpf());

        //Update object with valid or reason for the invalidity of the vote.
        repository.save(voting);
        return voting.isValid();
    }

    private static String removeAccent(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private void validateCPF(Voting voting, String cpf){

        try {
            ResponseEntity<UserInfoDTO> userInfo = client.getStatusCPF(cpf);

            if (userInfo.getStatusCode().equals(HttpStatus.OK)) {
                if (Objects.requireNonNull(userInfo.getBody()).getStatus()
                        .compareToIgnoreCase(CPF_SERVICE_STATUS_SUCCESS) == 0) {
                    voting.setValid(true);
                } else {
                    voting.setInvalidReason(CPF_UNABLE_TO_VOTE);
                }
            } else {
                voting.setInvalidReason(CPF_SERVICE_OFF);
            }
        } catch (FeignException ex) {
            //Occurs when the CPF is invalid or the service is out
            if(ex.status() == HttpStatus.NOT_FOUND.value()) {
                voting.setInvalidReason(CPF_INVALID);
            } else {
                voting.setInvalidReason(CPF_SERVICE_OFF);
            }
        }
    }

    private Calendar buildCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }
}

package br.com.vandersonsampaio.controller;

import br.com.vandersonsampaio.model.dto.request.RegisterVoteDTO;
import br.com.vandersonsampaio.model.dto.response.MessageDTO;
import br.com.vandersonsampaio.service.interfaces.IVotingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/voting/v1/", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingController {

    private final IVotingService service;

    public VotingController(IVotingService service){
        this.service = service;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<MessageDTO> registerVote(@RequestBody RegisterVoteDTO voting) throws Exception {
        boolean computedVote = service.vote(voting.getIdSession(), voting.getIdAssociate(), voting.getAgree());

        String message = computedVote ? "Vote Successfully Tallied" : "Error Computing Vote";
        return ResponseEntity.ok(new MessageDTO(message));
    }

}

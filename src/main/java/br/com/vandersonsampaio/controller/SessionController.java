package br.com.vandersonsampaio.controller;

import br.com.vandersonsampaio.mapper.SessionMapper;
import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.dto.ComputeVoteDTO;
import br.com.vandersonsampaio.model.dto.request.RegisterSessionDTO;
import br.com.vandersonsampaio.model.dto.response.RegisteredSessionDTO;
import br.com.vandersonsampaio.service.interfaces.ISessionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/session/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

    private final SessionMapper mapper;
    private final ISessionService service;

    public SessionController(ISessionService service, SessionMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisteredSessionDTO> registerSession(@RequestBody RegisterSessionDTO sessionDTO)
            throws Exception {

        Session createdSession = service.createSession(sessionDTO.getIdAgenda(),
                sessionDTO.getInitialTime(), sessionDTO.getMinutesDuration());

        return ResponseEntity.ok(mapper.toDTO(createdSession));
    }

    @GetMapping(value = "/result/{idSession}")
    public ResponseEntity<ComputeVoteDTO> computeVote(@PathVariable("idSession") int idSession) throws Exception {
        return ResponseEntity.ok(service.computeVote(idSession));
    }
}

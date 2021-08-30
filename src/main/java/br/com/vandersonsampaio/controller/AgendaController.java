package br.com.vandersonsampaio.controller;

import br.com.vandersonsampaio.mapper.AgendaMapper;
import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.dto.request.RegisterAgendaDTO;
import br.com.vandersonsampaio.model.dto.response.RegisteredAgendaDTO;
import br.com.vandersonsampaio.service.interfaces.IAgendaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/agenda/v1/", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AgendaController {

    private final AgendaMapper mapper;
    private final IAgendaService service;

    public AgendaController(IAgendaService service,
                            AgendaMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<RegisteredAgendaDTO> registerAgenda(@RequestBody RegisterAgendaDTO agendaDTO) throws Exception {
        Agenda agenda = mapper.toAgendaModel(agendaDTO);
        agenda = service.create(agenda);
        return ResponseEntity.ok(mapper.toRegisteredAgendaDTO(agenda));
    }
}

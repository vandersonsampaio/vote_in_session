package br.com.vandersonsampaio.mapper;

import br.com.vandersonsampaio.model.Agenda;
import br.com.vandersonsampaio.model.ItemAgenda;
import br.com.vandersonsampaio.model.dto.request.RegisterAgendaDTO;
import br.com.vandersonsampaio.model.dto.response.RegisteredAgendaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class, LocalTime.class,
        LocalDate.class, Date.class, ZoneId.class})
public interface AgendaMapper {

    @Mappings ({
            @Mapping(source = "itemsAgenda", target = "itemAgendaSet"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "session", ignore = true),
            @Mapping(target = "date",
                    expression = "java(dto.getDate() == null ? null : Date.from( dto.getDate().atStartOfDay( ZoneId.systemDefault() ).toInstant() ))"),
            @Mapping(target = "initialTime",
                    expression = "java(dto.getInitialTime() == null ? null : Date.from( LocalTime.parse(dto.getInitialTime()).atDate(LocalDate.of(dto.getDate().getYear(), dto.getDate().getMonth(), dto.getDate().getDayOfMonth())).atZone(ZoneId.systemDefault()).toInstant()))"),
            @Mapping(target = "finalTime",
                    expression = "java(dto.getFinalTime() == null ? null : Date.from( LocalTime.parse(dto.getFinalTime()).atDate(LocalDate.of(dto.getDate().getYear(), dto.getDate().getMonth(), dto.getDate().getDayOfMonth())).atZone(ZoneId.systemDefault()).toInstant()))")
    })
    Agenda toAgendaModel(RegisterAgendaDTO dto);

    @Mappings ({
            @Mapping(source = "descriptions", target = "description"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "agenda", ignore = true)
    })
    ItemAgenda toItemAgendaModel(String descriptions);

    @Mappings ({
            @Mapping(target = "initialTime",
                    expression = "java(model.getInitialTime() == null ? null : LocalDateTime.ofInstant(model.getInitialTime().toInstant(), ZoneId.systemDefault()).toLocalTime())"),
            @Mapping(target = "finalTime",
                    expression = "java(model.getFinalTime() == null ? null : LocalDateTime.ofInstant(model.getFinalTime().toInstant(), ZoneId.systemDefault()).toLocalTime())"),
            @Mapping(target = "itemsAgenda",
                    expression = "java(model.getItemAgendaSet() == null ? null : model.getItemAgendaSet().stream().map(ItemAgenda::getDescription).collect(Collectors.toList()))")
    })
    RegisteredAgendaDTO toRegisteredAgendaDTO(Agenda model);
}

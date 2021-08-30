package br.com.vandersonsampaio.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredAgendaDTO {

    private int id;
    private String manager;
    private String responsible;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime initialTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime finalTime;
    private String theme;
    private String observations;
    private List<String> itemsAgenda;
}

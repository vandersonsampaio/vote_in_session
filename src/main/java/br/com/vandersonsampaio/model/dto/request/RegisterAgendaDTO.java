package br.com.vandersonsampaio.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAgendaDTO {

    private String manager;
    private String responsible;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private String initialTime;
    @JsonFormat(pattern = "HH:mm")
    private String finalTime;
    private String theme;
    private String observations;
    private List<String> itemsAgenda;
}

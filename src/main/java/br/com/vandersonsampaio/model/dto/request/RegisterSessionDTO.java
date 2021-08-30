package br.com.vandersonsampaio.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSessionDTO {

    private int idAgenda;
    private int minutesDuration;
    private String initialTime;
}

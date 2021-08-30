package br.com.vandersonsampaio.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RegisteredSessionDTO {

    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date opening;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date closing;
    private int minutesDuration;
}

package br.com.vandersonsampaio.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterVoteDTO {

    private int idSession;
    private int idAssociate;
    private String agree;
}

package br.com.vandersonsampaio.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ComputeVoteDTO implements Serializable {

    private String theme;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateVoting;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date initialVoting;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date finalVoting;
    private long totalVotes;
    private long validVotes;
    private long invalidVotes;
    private long agreeVotes;
    private long disagreeVotes;
    private String optionWinner;


    public String toString() {
        return "{\"theme\":\"" + this.getTheme() +
                "\", \"dateVoting\":\"" + this.getDateVoting() +
                "\", \"initialVoting\":\"" + this.getInitialVoting() +
                "\", \"finalVoting\":\"" + this.getFinalVoting() +
                "\", \"totalVotes\":" + this.getTotalVotes() +
                ", \"validVotes\":" + this.getValidVotes() +
                ", \"invalidVotes\":" + this.getInvalidVotes() +
                ", \"agreeVotes\":" + this.getAgreeVotes() +
                ", \"disagreeVotes\":" + this.getDisagreeVotes() +
                ", \"optionWinner\":\"" + this.getOptionWinner() + "\"}";
    }
}

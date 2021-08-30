package br.com.vandersonsampaio.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_session")
public class Session {

    @Id
    @Column(name = "id_session", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;

    @Column(name = "dt_opening", nullable = false)
    private Date opening;

    @Column(name = "dt_closing", nullable = false)
    private Date closing;

    @Column(name = "nr_minutes_duration", nullable = false)
    private int minutesDuration;

    @OneToOne
    @JoinColumn(name = "id_agenda", nullable = false)
    private Agenda agenda;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_session")
    private Set<Voting> votingSet;

    public Session(Date opening, Date closing,
                   int minutesDuration, Agenda agenda){
        this.opening = opening;
        this.closing = closing;
        this.minutesDuration = minutesDuration;
        this.agenda = agenda;
    }
}

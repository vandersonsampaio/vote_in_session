package br.com.vandersonsampaio.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_agenda")
public class Agenda {

    @Id
    @Column(name = "id_agenda", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;

    @Column(name = "ds_manager", nullable = false)
    private String manager;

    @Column(name = "ds_responsible", nullable = false)
    private String responsible;

    @Column(name = "dt_agenda", nullable = false)
    private Date date;

    @Column(name = "dt_initial_time", nullable = false)
    private Date initialTime;

    @Column(name = "dt_final_time")
    private Date finalTime;

    @Column(name = "ds_theme", nullable = false)
    private String theme;

    @Column(name = "tx_observations", columnDefinition = "text")
    private String observations;

    @OneToOne(mappedBy = "agenda",cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_session", nullable = false)
    private Session session;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_agenda")
    private Set<ItemAgenda> itemAgendaSet;
}

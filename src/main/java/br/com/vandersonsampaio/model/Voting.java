package br.com.vandersonsampaio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tb_voting", uniqueConstraints=@UniqueConstraint(columnNames={"id_associate", "id_session"}))
public class Voting {

    @Id
    @Column(name = "id_voting", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;

    @Column(name = "is_agree", nullable = false)
    private boolean agree;

    @Column(name = "dt_moment", nullable = false)
    private Date moment;

    @Column(name = "is_valid", nullable = false, columnDefinition = "boolean default false")
    private boolean valid;

    @Column(name = "ds_invalid_reason")
    private String invalidReason;

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "id_associate", nullable = false)
    private Associate associate;

    public Voting(){
        moment = new Date();
    }
}

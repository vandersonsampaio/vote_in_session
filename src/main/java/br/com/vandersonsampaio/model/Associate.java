package br.com.vandersonsampaio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_associate")
public class Associate implements Serializable {

    @Id
    @Column(name = "id_associate", nullable = false)
    private int id;

    @Column(name = "ds_name", nullable = false)
    private String name;

    @Column(name = "ds_cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "dt_association", nullable = false)
    private Date association;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_associate")
    private Set<Voting> votingSet;
}

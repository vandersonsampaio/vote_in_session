package br.com.vandersonsampaio.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_item_agenda")
public class ItemAgenda {

    @Id
    @Column(name = "id_item_agenda", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;

    @Column(name = "ds_item", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_agenda", nullable = false)
    private Agenda agenda;
}

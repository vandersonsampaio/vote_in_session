package br.com.vandersonsampaio.model.repository;

import br.com.vandersonsampaio.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAgendaRepository extends JpaRepository<Agenda, Integer> {
}

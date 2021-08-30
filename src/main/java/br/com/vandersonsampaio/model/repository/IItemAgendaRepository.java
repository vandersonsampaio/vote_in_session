package br.com.vandersonsampaio.model.repository;

import br.com.vandersonsampaio.model.ItemAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemAgendaRepository extends JpaRepository<ItemAgenda, Integer> {
}

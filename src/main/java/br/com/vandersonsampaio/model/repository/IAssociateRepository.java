package br.com.vandersonsampaio.model.repository;

import br.com.vandersonsampaio.model.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAssociateRepository extends JpaRepository<Associate, Integer> {
}

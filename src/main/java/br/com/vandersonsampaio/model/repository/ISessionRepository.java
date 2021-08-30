package br.com.vandersonsampaio.model.repository;

import br.com.vandersonsampaio.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISessionRepository extends JpaRepository<Session, Integer> {
}

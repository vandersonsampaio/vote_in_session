package br.com.vandersonsampaio.model.repository;

import br.com.vandersonsampaio.model.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVotingRepository extends JpaRepository<Voting, Integer> {
}

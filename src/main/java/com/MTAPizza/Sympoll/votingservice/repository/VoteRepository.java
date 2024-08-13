package com.MTAPizza.Sympoll.votingservice.repository;

import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
}

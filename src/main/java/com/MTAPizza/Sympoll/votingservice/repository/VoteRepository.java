package com.MTAPizza.Sympoll.votingservice.repository;

import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    Vote findByUserIdAndAndVotingItemId(UUID userId, int votingItemId);
    Vote findByVotingItemId(int votingItemId);
    int deleteVoteByUserIdAndVotingItemId(UUID userId, int votingItemId);
    int countByVotingItemId(int votingItemId);
    int deleteVoteByVotingItemId(int votingItemId);

    void deleteByVotingItemIdIn(List<Integer> votingItemIds);
    List<Vote> findByVotingItemIdIn(List<Integer> votingItemIds);
}

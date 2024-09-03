package com.MTAPizza.Sympoll.votingservice.repository;

import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    Vote findByUserIdAndAndVotingItemId(UUID userId, int votingItemId);
    Vote findByVotingItemId(int votingItemId);
    int deleteVoteByUserIdAndVotingItemId(UUID userId, int votingItemId);
    int countByVotingItemId(int votingItemId);
    void deleteByVotingItemIdIn(List<Integer> votingItemIds);
    List<Vote> findByVotingItemIdIn(List<Integer> votingItemIds);

    /**
     * Retrieves a list of voting item IDs where the given user ID has cast a vote.
     * @param userId The UUID of the user.
     * @param votingItemIds The list of voting item IDs to check against.
     * @return List of integers representing the voting item IDs that the user has voted on.
     */
    @Query("SELECT v.votingItemId FROM Vote v WHERE v.userId = :userId AND v.votingItemId IN :votingItemIds")
    List<Integer> findVotingItemIdsByUserIdAndVotingItemIds(@Param("userId") UUID userId, @Param("votingItemIds") List<Integer> votingItemIds);
}

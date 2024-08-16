package com.MTAPizza.Sympoll.votingservice.service.vote;

import com.MTAPizza.Sympoll.votingservice.client.PollClient;
import com.MTAPizza.Sympoll.votingservice.dto.vote.*;
import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import com.MTAPizza.Sympoll.votingservice.repository.VoteRepository;
import com.MTAPizza.Sympoll.votingservice.service.api.VoteAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.TreeMap;


@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final PollClient pollClient;

    /**
     * Creates a new vote based on the provided {@link VoteRequest}.
     * Registers the vote with the poll client and saves it to the repository if successful.
     *
     * @param voteRequest The request containing details of the vote to be created.
     * @return The newly created {@link Vote} entity.
     * @throws RuntimeException If the poll client fails to register the vote.
     */
    public Vote createVote(VoteRequest voteRequest) {
        PollServiceVoteRequest requestBody = createPollServiceVoteRequest(voteRequest, VoteAction.add);


        ResponseEntity<String> response = pollClient.voteInPoll(requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            Vote entity = voteRepository.save(Vote.builder()
                    .userId(voteRequest.userId())
                    .votingItemId(voteRequest.votingItemId())
                    .build());

            return entity;
        } else {
            log.error("Failed to vote in poll. Status code: {}", response.getStatusCode());
            throw new RuntimeException("Failed to vote in poll. Status code: " + response.getStatusCode());
        }
    }

    /**
     * Deletes an existing vote based on the provided {@link VoteRequest}.
     * Unregisters the vote with the poll client and deletes it from the repository if successful.
     *
     * @param voteRequest The request containing details of the vote to be deleted.
     * @return The deleted {@link Vote} entity.
     * @throws RuntimeException If the poll client fails to unregister the vote.
     */
    @Transactional
    public Vote deleteVote(VoteRequest voteRequest) {
        PollServiceVoteRequest requestBody = createPollServiceVoteRequest(voteRequest, VoteAction.remove);

        ResponseEntity<String> response = pollClient.voteInPoll(requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            Vote ret = voteRepository.findByUserIdAndAndVotingItemId(voteRequest.userId(), voteRequest.votingItemId());
            voteRepository.deleteVoteByUserIdAndVotingItemId(voteRequest.userId(), voteRequest.votingItemId());

            return ret;

        } else {
            log.error("Failed to remove vote from poll. Status code: {}", response.getStatusCode());
            throw new RuntimeException("Failed to remove vote from poll. Status code: " + response.getStatusCode());
        }
    }

    /**
     * Creates a {@link PollServiceVoteRequest} with the provided {@link VoteRequest} and action.
     *
     * @param voteRequest The request containing vote details.
     * @param action The action to be performed (add or remove).
     * @return A new {@link PollServiceVoteRequest} instance.
     */
    private PollServiceVoteRequest createPollServiceVoteRequest(VoteRequest voteRequest, VoteAction action) {
        return new PollServiceVoteRequest(
                voteRequest.votingItemId(),
                action.name()
        );
    }

    /**
     * Counts the number of votes for each voting item ID provided in the {@link CountVotesRequest}.
     *
     * @param countVotesRequest The request containing the list of voting item IDs.
     * @return A {@link CountVotesResponse} containing the count of votes for each voting item ID.
     */
    public CountVotesResponse countVotes(CountVotesRequest countVotesRequest) {
       Map<Integer, Integer> voteCounts = new TreeMap<>();

        for(int votingItemId : countVotesRequest.votingItemIds()) {
            voteCounts.put(votingItemId, voteRepository.countByVotingItemId(votingItemId));
        }

        return new CountVotesResponse(voteCounts);
    }
}

package com.MTAPizza.Sympoll.votingservice.service.vote;

import com.MTAPizza.Sympoll.votingservice.client.PollClient;
import com.MTAPizza.Sympoll.votingservice.dto.vote.*;
import com.MTAPizza.Sympoll.votingservice.exception.PollServiceUnavailableException;
import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import com.MTAPizza.Sympoll.votingservice.repository.VoteRepository;
import com.MTAPizza.Sympoll.votingservice.service.api.VoteAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final PollClient pollClient;

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

    public Vote deleteVote(VoteRequest voteRequest) {
        PollServiceVoteRequest requestBody = createPollServiceVoteRequest(voteRequest, VoteAction.remove);

        ResponseEntity<String> response = pollClient.voteInPoll(requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            return voteRepository.deleteVoteByUserIdAndVotingItemId(voteRequest.userId(), voteRequest.votingItemId());
        } else {
            log.error("Failed to remove vote from poll. Status code: {}", response.getStatusCode());
            throw new RuntimeException("Failed to remove vote from poll. Status code: " + response.getStatusCode());
        }
    }

    private PollServiceVoteRequest createPollServiceVoteRequest(VoteRequest voteRequest, VoteAction action) {
        return new PollServiceVoteRequest(
                voteRequest.votingItemId(),
                action.name()
        );
    }


    public CountVotesResponse countVotes(CountVotesRequest countVotesRequest) {
        return new CountVotesResponse(
                voteRepository.countByVotingItemId(
                        countVotesRequest.votingItemId()));
    }
}

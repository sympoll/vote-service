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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final PollClient pollClient;

    public Vote createVote(VoteRequest voteRequest) {
        PollServiceVoteRequest requestBody = new PollServiceVoteRequest(
                voteRequest.votingItemId(),
                VoteAction.add.name()
        );

        try {
            ResponseEntity<String> response = pollClient.voteInPoll(requestBody);

            if (response.getStatusCode() == HttpStatus.OK) {
                Vote entity = voteRepository.save(Vote.builder()
                        .userId(voteRequest.userId())
                        .votingItemId(voteRequest.votingItemId())
                        .build());

                return entity;
            } else {
                log.error("Failed to vote in poll. Status code: {}", response.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            // Handle HTTP errors
            log.error("HTTP error while voting in poll: {}", ex.getMessage());
        } catch (ResourceAccessException ex) {
            // Handle network/resource access errors
            log.error("Resource access error while voting in poll: {}", ex.getMessage());
        } catch (Exception ex) {
            // Handle other exceptions
            log.error("An unexpected error occurred: {}", ex.getMessage());
        }

        // Return a meaningful response or throw a custom exception
        throw new PollServiceUnavailableException("Unable to vote in the poll service.");
    }

    public VoteResponse deleteVote(VoteRequest voteRequest) {
    }

    public CountVotesResponse countVotes(CountVotesRequest countVotesRequest) {
    }
}

package com.MTAPizza.Sympoll.votingservice.controller;

import com.MTAPizza.Sympoll.votingservice.dto.health.HealthResponse;
import com.MTAPizza.Sympoll.votingservice.dto.vote.CountVotesRequest;
import com.MTAPizza.Sympoll.votingservice.dto.vote.CountVotesResponse;
import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteRequest;
import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import com.MTAPizza.Sympoll.votingservice.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ServiceController {
    private final VoteService voteService;

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vote createVote(@RequestBody VoteRequest voteRequest) {
        log.info("Received request to create a vote");
        log.debug("Vote received to create: {}", voteRequest);
        return voteService.createVote(voteRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Vote deleteVote(@RequestBody VoteRequest voteRequest) {
        log.info("Received request to delete a vote");
        log.debug("Vote received to delete: {}", voteRequest);
        return voteService.deleteVote(voteRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CountVotesResponse countVotes(@RequestBody CountVotesRequest countVotesRequest) {
        log.info("Received request to count votes for voting items {}", countVotesRequest.votingItemIds());
        return voteService.countVotes(countVotesRequest);
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public HealthResponse healthCheck() {
        log.info("Received health check request, returning OK");
        return new HealthResponse("Running", "Poll Management Service is up and running.");
    }
}

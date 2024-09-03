package com.MTAPizza.Sympoll.votingservice.controller;

import com.MTAPizza.Sympoll.votingservice.dto.vote.*;
import com.MTAPizza.Sympoll.votingservice.model.vote.Vote;
import com.MTAPizza.Sympoll.votingservice.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {
    private final VoteService voteService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vote createVote(@RequestBody VoteRequest voteRequest) {
        log.info("Received request to create a vote");
        return voteService.createVote(voteRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Vote deleteVote(@RequestBody VoteRequest voteRequest) {
        log.info("Received request to delete a vote");
        return voteService.deleteVote(voteRequest);
    }

    @PostMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public CountVotesResponse countVotes(@RequestBody CountVotesRequest countVotesRequest) {
        log.info("Received request to count votes for voting items {}", countVotesRequest.votingItemIds());
        return voteService.countVotes(countVotesRequest);
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> healthCheck() {
        log.info("Received request to health check");
        Map<String, String> healthStatus = new HashMap<>();
        healthStatus.put("status", "running");

        return healthStatus;
    }

    @DeleteMapping("/delete-multiple")
    @ResponseStatus(HttpStatus.OK)
    public DeleteMultipleVotesResponse deleteMultipleVotes(@RequestBody DeleteMultipleVotesRequest deleteMultipleVotesRequest) {
        log.info("Received request to delete multiple votes");
        return voteService.deleteMultipleVotes(deleteMultipleVotesRequest);
    }
}

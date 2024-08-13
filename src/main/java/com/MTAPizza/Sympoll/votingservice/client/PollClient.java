package com.MTAPizza.Sympoll.votingservice.client;

import com.MTAPizza.Sympoll.votingservice.dto.vote.PollServiceVoteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PutExchange;

public interface PollClient {

    @PutExchange("/api/poll/vote")
    ResponseEntity<String> voteInPoll(@RequestBody PollServiceVoteRequest pollServiceVoteRequest);
}

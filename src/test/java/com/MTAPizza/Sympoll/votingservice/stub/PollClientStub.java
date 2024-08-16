package com.MTAPizza.Sympoll.votingservice.stub;

import com.MTAPizza.Sympoll.votingservice.dto.vote.PollServiceVoteRequest;
import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PollClientStub {

    public static void initStubs() {

    }

    public static void stubPollVoteSuccess(PollServiceVoteRequest voteRequest) {
        stubFor(post(urlEqualTo("/api/poll/vote"))
                .withRequestBody(matchingJsonPath("$.votingItemId"))
                .withRequestBody(matchingJsonPath("$.action"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                                {
                                    "votingItemDescription": "string",
                                    "voteCount": 0
                                }
                                """)));

    }
}

package com.MTAPizza.Sympoll.votingservice.stub;

import com.MTAPizza.Sympoll.votingservice.dto.vote.PollServiceVoteRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PollClientStub {

    public static void initStubs() {
        stubPollVoteSuccess();
    }

    public static void stubPollVoteSuccess() {
        stubFor(put(urlEqualTo("/api/poll/vote"))
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

package com.MTAPizza.Sympoll.votingservice.stub;

import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PollClientStub {

    public static void stubPollCallSuccess(VoteRequest voteRequest) {
        // Set up the WireMock server to return a specific response
        String pollServiceResponseJson = """
                    {
                        "pollTitle": "Sample Poll Title",
                        "votingItemDescription": "Sample Voting Item Description",
                        "voteTimestamp": "2024-08-09T12:34:56Z"
                    }
                """;

        stubFor(put(urlEqualTo("/api/poll/vote"))
                .withRequestBody(equalToJson("""
                            {
                                "userId": "user-id-placeholder",
                                "votingItemId": "voting-item-id-placeholder"
                            }
                        """))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(pollServiceResponseJson)));
    }
}

package com.MTAPizza.Sympoll.votingservice.stub;

import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PollClientStub {

    /**
     * Configures a WireMock stub to simulate a successful response from the poll-service.
     * <p>
     * This method sets up a WireMock server to return a predefined JSON response when a PUT request is made
     * to the /api/poll/vote endpoint with a request body matching the provided {@link VoteRequest} parameters.
     * The response simulates the successful handling of a vote request by the poll-service.
     * </p>
     *
     * @param voteRequest The {@link VoteRequest} object that contains the userId and votingItemId.
     *                    The request body in the stubbed PUT request should match the userId and votingItemId provided here.
     *                    Note: This parameter is not directly used in the stub configuration, but it's intended to
     *                    show which request parameters are expected.
     * @see VoteRequest
     */
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

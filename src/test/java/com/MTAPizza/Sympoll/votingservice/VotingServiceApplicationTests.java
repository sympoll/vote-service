package com.MTAPizza.Sympoll.votingservice;

import com.MTAPizza.Sympoll.votingservice.dto.vote.CountVotesResponse;
import com.MTAPizza.Sympoll.votingservice.dto.vote.VotingItemsCheckedResponse;
import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.votingservice.stub.PollClientStub;
import com.MTAPizza.Sympoll.votingservice.validator.exception.VoteExceptionHandler;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock(port = 0)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(VoteExceptionHandler.class)
class VotingServiceApplicationTests {
    private final String userId = "d13a2397-cd48-4602-b399-0e303fc401b4";
    private final int votingItemId = 2;


    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.2").withInitScript("init.sql");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        PollClientStub.initStubs();
    }

    static {
        postgreSQLContainer.start(); //  Run mock test container.
    }

    @Test
    @Order(1)
    void shouldCastVote() {
        // Define the request body for the POST request
        String requestBodyJson = String.format("""
                    {
                        "userId": "%s",
                        "votingItemId": %d
                    }
                """, userId, votingItemId);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyJson)
                .when()
                .post("/api/vote")
                .then()
                .statusCode(201)
                .extract().response();

        VoteResponse voteResponse = response.as(VoteResponse.class);
        assertNotNull(voteResponse.voteId());
    }

    @Test
    @Order(2)
    void shouldDeleteVote() {
        // Define the request body for the DELETE request
        String requestBodyJson = String.format("""
                    {
                        "userId": "%s",
                        "votingItemId": %d
                    }
                """, userId, votingItemId);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyJson)
                .when()
                .delete("/api/vote")
                .then()
                .statusCode(200)
                .extract().response();

        VoteResponse voteResponse = response.as(VoteResponse.class);
        assertNotNull(voteResponse.voteId());
    }

    @Test
    @Order(3)
    void shouldCountVotes() {
        String requestBodyJson = """
                    {
                        "votingItemIds": [1,3]
                    }
                """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyJson)
                .when()
                .post("/api/vote/count")
                .then()
                .statusCode(200)
                .extract().response();

        CountVotesResponse countVotesResponse = response.as(CountVotesResponse.class);

        /* This map is the vote counts in the example db
         * voting item id 3 has 2 counts etc*/
        Map<Integer, Integer> actualVoteCounts = new TreeMap<>();
        actualVoteCounts.put(1, 1);
        actualVoteCounts.put(3, 2);

        assertEquals(actualVoteCounts, countVotesResponse.voteCounts());
    }

    @Test
    @Order(4)
    void shouldGetUserChoices() {
        String requestBodyJson = String.format("""
                    {
                        "votingItemIds": [1,2,3,4,5],
                        "userId": "%s"
                    }
                """, userId);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyJson)
                .when()
                .post("/api/vote/user-choices")
                .then()
                .statusCode(200)
                .extract().response();

        VotingItemsCheckedResponse votingItemsCheckedResponse = response.as(VotingItemsCheckedResponse.class);
        // Assert that the returned list contains exactly [1, 3]
        assertNotNull(votingItemsCheckedResponse, "The response should not be null");
        Set<Integer> expectedItems = new HashSet<>(Arrays.asList(1, 3));
        Set<Integer> actualItems = new HashSet<>(votingItemsCheckedResponse.votingItemIds());
        assertEquals(expectedItems, actualItems, "The voting items should match exactly [1, 3]");
    }
}

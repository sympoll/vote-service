package com.MTAPizza.Sympoll.votingservice;

import com.MTAPizza.Sympoll.votingservice.dto.vote.CountVotesResponse;
import com.MTAPizza.Sympoll.votingservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.votingservice.validator.exception.VoteExceptionHandler;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock(port = 0)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(VoteExceptionHandler.class)
class VotingServiceApplicationTests {
    private static final Gson gson;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    private String userId = "user-id-123";
    private String votingItemId = "voting-item-id-456";


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
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    static {
        postgreSQLContainer.start(); //  Run mock test container.
        gson = new Gson();
    }

    @Test
    @Order(1)
    void shouldCastVote() {
        // Define the request body for the POST request
        String requestBodyJson = String.format("""
                    {
                        "userId": "%s",
                        "votingItemId": "%s"
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
        // Define the request body for the POST request
        String requestBodyJson = String.format("""
                    {
                        "userId": "%s",
                        "votingItemId": "%s"
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
                .delete("/api/vote/count")
                .then()
                .statusCode(200)
                .extract().response();

        CountVotesResponse countVotesResponse = response.as(CountVotesResponse.class);

        /* This map is the vote counts in the example db
        * voting item id 3 has 2 counts etc*/
        Map<Integer, Integer> actualVoteCounts = new TreeMap<>();
        actualVoteCounts.put(3, 2);
        actualVoteCounts.put(1, 1);

        assertEquals(actualVoteCounts, countVotesResponse.voteCounts());
    }
}

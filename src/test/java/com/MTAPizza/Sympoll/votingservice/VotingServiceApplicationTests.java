package com.MTAPizza.Sympoll.votingservice;

import com.MTAPizza.Sympoll.votingservice.validator.exception.VoteExceptionHandler;
import com.google.gson.Gson;
import io.restassured.RestAssured;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock(port = 0)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(VoteExceptionHandler.class)
class VotingServiceApplicationTests {
    private static final Gson gson;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

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
        String requestBodyJson = """
                    {
                        "user_id": "user-id-123",
                        "voting_item_id": "voting-item-id-456"
                    }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        // Send POST request to vote-service
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8084/api/vote",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Validate the response from vote-service
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String responseBody = response.getBody();
        assert responseBody != null;
        assert responseBody.contains("voteId");
        assert responseBody.contains("userId");
        assert responseBody.contains("votingItemId");
        assert responseBody.contains("timestamp");
    }
}

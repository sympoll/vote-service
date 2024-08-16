# vote-service

## Table of Contents

[1. About](#1-about)  
[2. Architecture](#2-architecture)  
&emsp; [2.1 Ports](#21-ports)  
&emsp; [2.2 Database Schema](#22-database-schema)  
&emsp; [2.3 Endpoints](#23-endpoints)  
&emsp; &emsp; [2.3.1 Health Check](#231-health-check)  
&emsp; &emsp; [2.3.2 Vote on Poll](#232-vote-on-poll)  
&emsp; &emsp; [2.3.3 Remove Vote from Poll](#233-remove-vote-from-poll)  
&emsp; &emsp; [2.3.4 Get Vote Count](#234-get-vote-count)

<br />

## 1) About

The Vote Service is responsible for handling user votes.

- The user votes on a specific answer option in the poll via the browser.

- The browser sends a request to the Vote Service API.

- The Vote Service handles that request by interacting with the Poll Service and the database to record and retrieve vote data.

<br />



## 2) Architecture
### 2.1) Ports

- **Service port:**  8084

- **Database port:**  5434

<br />


### 2.2) Database Schema
`votes` Table:**

```sql
CREATE TABLE votes
(
    vote_id        UUID PRIMARY KEY,
    user_id        UUID NOT NULL,
    voting_item_id INT  NOT NULL,
    vote_datetime  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

<br />

### 2.3) Endpoints
**BASE URL:**  `/api/vote`
For each endpoint, use the base URL along with the URL extension if provided.

---

#### 2.3.1) Health Check

- **Method:**  POST

- **Endpoint:** `/health`

- **Description:**  Standard service health check.


**Response:**
- **Status Code:**  200

- **Response body:**


```json
{
    "status" : "running"
}
```

---

#### 2.3.2) Vote on Poll

- **Method:**  POST

- **Description:**  Creates a vote in the Vote Service's database.
- 
  **Request body:**

```json
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // int
}
```

**Response:**

- **Status Code:**  201

- **Response body:**

```json
{
    "voteId": "vote-id", // UUID
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // int
    "voteDateTime": "vote-date-time" // LocalDateTime
}
```

##### Poll Service Interaction
A call to this endpoint also triggers a **PUT**  request from the Vote Service to the Poll Service (/api/poll/vote).

**Request body:**

```json
{
    "votingItemId": "voting-item-id", // int
    "action": "add"
}
```

**Poll Service Response:**

- **Status Code:**  200

- **Response body:**

```json
{
    "votingItemDescription": "voting-item-description", // String
    "voteCount": vote-count // int
}
```

---

#### 2.3.3) Remove Vote from Poll

- **Method:**  DELETE

- **Description:**  Deletes a vote from the Vote Service's database.

-  **Request body:**

```json
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // int
}
```

**Response:**

- **Status Code:**  200

- **Response body:**


```json
{
    "voteId": "vote-id", // UUID
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // int
    "voteDateTime": "vote-date-time" // LocalDateTime
}
```

##### Poll Service Interaction
A call to this endpoint also triggers a **PUT**  request from the Vote Service to the Poll Service (/api/poll/vote).

**Request body:**

```json
{
    "votingItemId": "voting-item-id", // int
    "action": "remove" // String
}
```
**Poll Service Response:**

- **Status Code:**  200

- **Response body:**

```json
{
    "votingItemDescription": "voting-item-description", // String
    "voteCount": vote-count // int
}
```

---

#### 2.3.4) Get Vote Count

- **Method:**  POST

- **Endpoint:** `/count`

- **Description:**  Returns the number of votes for each specified voting item ID. The request body should include a list of voting item IDs, and the response will provide the vote count for each item in a key-value format.**Request body:**

```json
{
    "votingItemIds": [1, 2, 3] // Array of voting item IDs (int)
}
```
**Response:**

- **Status Code:**  200

- **Response body:**

```json
{
    "voteCounts": {
        "1": 10, // Voting item ID and its vote count
        "2": 5,
        "3": 8
    }
}
```
**Note:**  The `voteCounts` field is an object where each key is a voting item ID (as an integer) and the value is the corresponding vote count.

---
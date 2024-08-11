# vote-service

## Table of Contents

[1. About](#1-about)  
[2. Architecture](#2-architecture)  
&emsp; [2.1 Ports](#21-ports)  
&emsp; [2.2 Database Schema](#22-database-schema)  
&emsp; [2.3 Endpoints](#23-endpoints)  
&emsp; &emsp; [2.3.1 Vote on Poll](#231-vote-on-poll)  
&emsp; &emsp; [2.3.2 Remove Vote from Poll](#232-remove-vote-from-poll)  
&emsp; &emsp; [2.3.3 Get Vote Count](#233-get-vote-count)

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

#### 2.3.1) Vote on Poll

- **Method:**  POST

- **Description:**  Creates a vote in the Vote Service's database.
  **Request body:**

```json
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // UUID
}
```
**Response:**
- **Status Code:**  201

- **Response body:**


```json
{
    "voteId": "vote-id", // UUID
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // UUID
    "voteDateTime": "vote-date-time" // LocalDateTime
}
```

##### Poll Service Interaction
A call to this endpoint also triggers a **PUT**  request from the Vote Service to the Poll Service (/api/poll/vote).

**Request body:**

```json
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // UUID
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

#### 2.3.2) Remove Vote from Poll

- **Method:**  DELETE

- **Description:**  Deletes a vote from the Vote Service's database.
  **Request body:**

```json
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // UUID
}
```
**Response:**
- **Status Code:**  200

- **Response body:**


```json
{
    "voteId": "vote-id", // UUID
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // UUID
    "voteDateTime": "vote-date-time" // LocalDateTime
}
```

##### Poll Service Interaction
A call to this endpoint also triggers a **PUT**  request from the Vote Service to the Poll Service (/api/poll/vote).

**Request body:**

```json
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // UUID
    "action": "remove"
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

#### 2.3.3) Get Vote Count

- **Method:**  GET

- **Description:**  Returns the number of votes for a specified voting item by counting the instances in the database.
  **Request body:**

```json
{
    "votingItemId": "voting-item-id" // UUID
}
```
**Response:**
- **Status Code:**  200

- **Response body:**


```json
{
    "voteCount": vote-count // int
}
```

---
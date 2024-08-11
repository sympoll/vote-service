# vote-service

## 1) About

The Vote Service is responsible for handling user votes.

- The user votes on a specific answer option in the poll via the browser.

- The browser sends a request to the Vote Service API.

- The Vote Service handles that request by interacting with the Poll Service and the database to record and retrieve vote data.

## 2) Architecture

### 2.1) Ports

- **Service port:**  8084

- **Database port:**  5434

### 2.2) Endpoints
**BASE URL:**  `/api/vote`
For each endpoint, use the base URL along with the URL extension if provided.

#### 2.2.1) Vote on Poll

- **Method:**  POST

- **Description:**  Creates a vote in the Vote Service's database.
  **Request body:**

```diff
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // UUID
}
```
**Response:**

- **Status Code:**  201

- **Response body:**

```diff
{
    "voteId": "vote-id", // UUID
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // UUID
    "voteDateTime": "vote-date-time" // LocalDateTime
}
```

##### Poll Service Interaction
A call to this endpoint also triggers a **PUT**  request from the Vote Service to the Poll Service.**Request body:**

```diff
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // UUID
}
```
**Poll Service Response:**
- **Status Code:**  200

- **Response body:**


```diff
{
    "votingItemDescription": "voting-item-description", // String
    "voteCount": vote-count // int
}
```

#### 2.2.2) Remove Vote from Poll

- **Method:**  DELETE

- **Description:**  Deletes a vote from the Vote Service's database.
  **Request body:**

```diff
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // UUID
}
```
Note: If possible, consider saving the `voteId` on the option the user voted for on the client side. This `voteId` can then be sent to delete the vote in the database.***Response:**
- **Status Code:**  200

- **Response body:**


```diff
{
    "voteId": "vote-id", // UUID
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id", // UUID
    "voteDateTime": "vote-date-time" // LocalDateTime
}
```

##### Poll Service Interaction
A call to this endpoint also triggers a **PUT**  request from the Vote Service to the Poll Service.**Request body:**

```diff
{
    "userId": "user-id", // UUID
    "votingItemId": "voting-item-id" // UUID
}
```
**Poll Service Response:**
- **Status Code:**  200

- **Response body:**


```diff
{
    "votingItemDescription": "voting-item-description", // String
    "voteCount": vote-count // int
}
```

#### 2.2.3) Get Vote Count

- **Method:**  GET

- **Description:**  Returns the number of votes for a specified voting item by counting the instances in the database.
  **Request body:**

```diff
{
    "votingItemId": "voting-item-id" // UUID
}
```
**Response:**
- **Status Code:**  200

- **Response body:**


```diff
{
    "voteCount": vote-count // int
}
```
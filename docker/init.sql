-- Voting Service Schema
CREATE TABLE votes
(
    vote_id        UUID PRIMARY KEY,
    user_id        UUID NOT NULL,
    voting_item_id INT  NOT NULL,
    vote_datetime  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
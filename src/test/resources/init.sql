-- Voting Service Schema
CREATE TABLE votes
(
    vote_id        UUID PRIMARY KEY,
    user_id        UUID NOT NULL,
    voting_item_id INT  NOT NULL,
    vote_datetime  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--- Inserting values
INSERT INTO votes (vote_id, user_id, voting_item_id)
VALUES ('a1e8a3d2-07a8-11ec-bf63-0242ac130002', '33a96133-37c4-4c06-8e49-bc313efabbea', 3),
       ('ed2ee3d9-8cf3-42e7-900b-566939de7e21', '33a96133-37c4-4c06-8e49-bc313efabbeb', 3),
       ('ed2ee3d9-8cf3-42e7-900b-566939de7e21', 'd13a2397-cd48-4602-b399-0e303fc401b4', 1)

CREATE TABLE polls
(
    poll_id             UUID PRIMARY KEY,
    title               VARCHAR(255),
    description         TEXT,
    nof_answers_allowed INT,
    creator_id          UUID,
    group_id            VARCHAR(255),
    time_created        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voting_item_options
(
    voting_item_id    SERIAL PRIMARY KEY,
    poll_id           UUID REFERENCES polls (poll_id),
    ordinal           INT,
    description       TEXT,
    vote_count        INT
);

-- NOTE: Insert sample data into polls table
INSERT INTO polls (poll_id, title, description, nof_answers_allowed, creator_id, group_id, time_created, time_updated, deadline)
VALUES
    ('051908e4-eacb-45a0-8956-a86763eada3d', 'Best Programming Language', 'Vote for your favorite programming language.', 1, '219b3c35-f850-4fec-8cc0-01ddfd80ef11', 'group1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days'),
    ('7b0857a6-f59a-42d2-98fe-d5eef162d4d0', 'Preferred Working Environment', 'Choose your preferred working environment.', 1, '219b3c35-f850-4fec-8cc0-01ddfd80ef11', 'group2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '40 days'),
    ('618212e5-e9a3-427d-8ac3-0580e0223f08', 'Favorite Cloud Service', 'Select your preferred cloud service provider.', 2, '729616aa-052e-4bcf-9f13-61d66db4e049', 'group3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '50 days');

-- NOTE: Insert sample data into answer_options table
INSERT INTO voting_item_options (poll_id, ordinal, description, vote_count)
VALUES
    ('051908e4-eacb-45a0-8956-a86763eada3d', 1, 'Python', 0),
    ('051908e4-eacb-45a0-8956-a86763eada3d', 2, 'JavaScript', 0),
    ('051908e4-eacb-45a0-8956-a86763eada3d', 3, 'Java', 0),
    ('051908e4-eacb-45a0-8956-a86763eada3d', 4, 'C#', 0),

    ('7b0857a6-f59a-42d2-98fe-d5eef162d4d0', 1, 'Remote', 0),
    ('7b0857a6-f59a-42d2-98fe-d5eef162d4d0', 2, 'On-site', 0),
    ('7b0857a6-f59a-42d2-98fe-d5eef162d4d0', 3, 'Hybrid', 0),

    ('618212e5-e9a3-427d-8ac3-0580e0223f08', 1, 'AWS', 0),
    ('618212e5-e9a3-427d-8ac3-0580e0223f08', 2, 'Google Cloud', 0),
    ('618212e5-e9a3-427d-8ac3-0580e0223f08', 3, 'Azure', 0);

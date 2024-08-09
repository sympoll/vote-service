package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.UUID;

public record VoteRequest(UUID userId, UUID votingItemId) {
}

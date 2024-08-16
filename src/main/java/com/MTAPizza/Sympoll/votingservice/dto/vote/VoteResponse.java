package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.UUID;

public record VoteResponse(UUID voteId, UUID userId, int votingItemId, String voteDatetime) {
}

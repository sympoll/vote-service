package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.List;
import java.util.UUID;

public record DeleteMultipleVotesResponse(List<UUID> voteIds) {
}

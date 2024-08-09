package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.UUID;

public record VoteResponse(String message, String pollTitle, String votingItemText, String timestamp) {
}

package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.Map;

public record CountVotesResponse(Map<Integer, Integer> voteCounts) {
}

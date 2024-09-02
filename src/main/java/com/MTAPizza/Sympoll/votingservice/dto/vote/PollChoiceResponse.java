package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.List;

public record PollChoiceResponse(List<Integer> votingItemIds) {
}

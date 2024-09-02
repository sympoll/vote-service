package com.MTAPizza.Sympoll.votingservice.dto.vote;

import java.util.List;
import java.util.UUID;

public record PollChoiceRequest (List<Integer> votingItemIds, UUID userId){
}

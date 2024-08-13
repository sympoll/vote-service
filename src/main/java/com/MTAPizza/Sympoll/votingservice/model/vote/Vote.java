package com.MTAPizza.Sympoll.votingservice.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "votes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID voteId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "voting_item_id")
    private int votingItemId;

    @Column(name = "vote_datetime")
    private LocalDateTime voteDatetime;

    @PrePersist
    protected void onCreate() {
        this.voteDatetime = LocalDateTime.now();
    }
}

package com.paradigmadigital.redpanda.business.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementEvent {
    
    private String id;
    private String cardId;
    private long createdAt;
    
}

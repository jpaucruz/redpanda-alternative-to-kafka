package com.paradigmadigital.redpanda.business.model;

import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class MovementAggregation {
    
    private long id;
    private String cardId;
    private long initRangeTime;
    private long endRangeTime;
    private Set<MovementEvent> movementEvents;
    
    public MovementAggregation(){
        this.id = Instant.now().getEpochSecond();
        this.movementEvents = new HashSet<>();
        this.initRangeTime = 0L;
        this.endRangeTime = 0L;
    }
    
    public void addMovement(MovementEvent movementEvent){
        this.movementEvents.add(movementEvent);
        this.cardId = movementEvent.getCardId();
        if (this.initRangeTime == 0){
            this.initRangeTime = movementEvent.getCreatedAt();
        }else{
            this.initRangeTime = Math.min(this.initRangeTime, movementEvent.getCreatedAt());
        }
        this.endRangeTime = Math.max(this.endRangeTime, movementEvent.getCreatedAt());
    }
    
}

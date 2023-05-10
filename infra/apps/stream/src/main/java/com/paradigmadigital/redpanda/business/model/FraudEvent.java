package com.paradigmadigital.redpanda.business.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Data
public class FraudEvent {
    
    private long id;
    private String cardId;
    private String rangeTime;
    private Set<MovementEvent> movementEvents;
    
    public FraudEvent(MovementAggregation movementAggregation){
        this.id = movementAggregation.getId();
        this.cardId = movementAggregation.getCardId();
        this.movementEvents = movementAggregation.getMovementEvents();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date initRangeTime = new Date(movementAggregation.getInitRangeTime());
        Date endRangeTime = new Date(movementAggregation.getEndRangeTime());
        this.rangeTime = dateFormat.format(initRangeTime) + " - " + dateFormat.format(endRangeTime);
    }
    
}

package com.paradigmadigital.redpanda.api.mapper;

import com.paradigmadigital.redpanda.api.model.MovementApi;
import com.paradigmadigital.redpanda.business.model.MovementEvent;

public class MovementMapper {
    
    public static MovementEvent mapperApiToEvent(MovementApi movementApi){
        return MovementEvent
            .builder()
            .id(String.valueOf(movementApi.getId()))
            .cardId(movementApi.getCardId())
            .createdAt(System.currentTimeMillis())
            .build();
    }
    
}

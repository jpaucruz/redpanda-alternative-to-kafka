package com.paradigmadigital.redpanda.api;

import com.paradigmadigital.redpanda.api.mapper.MovementMapper;
import com.paradigmadigital.redpanda.api.model.MovementApi;
import com.paradigmadigital.redpanda.business.MovementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@Log4j2
public class MovementController {
    
    private MovementService movementService;
    
    public MovementController(MovementService movementService){
        this.movementService = movementService;
    }
    
    @PostMapping(value = "/movement", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody MovementApi movementApi){
        log.info(MessageFormat.format("It has entered a new movement id:{0} card:{1}", movementApi.getId(), movementApi.getCardId()));
        movementService.generateEvent(MovementMapper.mapperApiToEvent(movementApi));
    }
    
}

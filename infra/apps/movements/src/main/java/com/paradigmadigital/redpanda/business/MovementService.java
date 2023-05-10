package com.paradigmadigital.redpanda.business;

import com.paradigmadigital.redpanda.business.model.MovementEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Log4j2
public class MovementService {
    
    @Value(value = "${spring.panda.topic.movements}")
    private String movementTopic;
    
    private KafkaTemplate<String, MovementEvent> kafkaTemplate;
    
    public MovementService(KafkaTemplate<String, MovementEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void generateEvent(MovementEvent movementEvent){
        log.info(MessageFormat.format("Sending event to Panda with key: {0}...", movementEvent.getId()));
        kafkaTemplate.send(movementTopic, movementEvent.getId(), movementEvent);
        log.info(MessageFormat.format("The event with key: {0} has been sent successfully", movementEvent.getId()));
    }
    
}

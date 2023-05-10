package com.paradigmadigital.redpanda.business.extractors;

import com.paradigmadigital.redpanda.business.model.MovementEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

@Slf4j
public class MovementTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long l) {
        MovementEvent movementEvent = (MovementEvent) consumerRecord.value();
        return movementEvent.getCreatedAt();
    }

}
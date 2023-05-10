package com.paradigmadigital.redpanda.business.topology;

import com.paradigmadigital.redpanda.business.extractors.MovementTimestampExtractor;
import com.paradigmadigital.redpanda.business.model.FraudEvent;
import com.paradigmadigital.redpanda.business.model.MovementAggregation;
import com.paradigmadigital.redpanda.business.model.MovementEvent;
import com.paradigmadigital.redpanda.config.serializers.CustomJsonDeserializer;
import com.paradigmadigital.redpanda.config.serializers.CustomJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class Fraud {
    
    @Value(value = "${spring.panda.topic.movements}")
    private String initTopic;
    
    @Value(value = "${spring.panda.topic.fraud}")
    private String endTopic;
    
    private final Serde<MovementEvent> movementEventSerde = Serdes
        .serdeFrom(new CustomJsonSerializer<>(), new CustomJsonDeserializer<>(MovementEvent.class));
    private final Serde<MovementAggregation> movementAggregationSerde = Serdes
        .serdeFrom(new CustomJsonSerializer<>(), new CustomJsonDeserializer<>(MovementAggregation.class));
    
    private final Serde<FraudEvent> fraudEventSerde = Serdes
        .serdeFrom(new CustomJsonSerializer<>(), new CustomJsonDeserializer<>(FraudEvent.class));
    
    @Autowired
    public void buildTopology(StreamsBuilder streamsBuilder) {
    
        KStream<String, FraudEvent> agg = streamsBuilder
            .stream(initTopic, Consumed.with(Serdes.String(), movementEventSerde).withTimestampExtractor(new MovementTimestampExtractor()))
            .map((k,v) -> KeyValue.pair(v.getCardId(), v))
            .groupByKey(Grouped.with(Serdes.String(), movementEventSerde))
            .windowedBy(SessionWindows.ofInactivityGapWithNoGrace(Duration.ofSeconds(30)))
            .aggregate(
                MovementAggregation::new,
                (k, movementEvent, movementAggregation) -> {
                    movementAggregation.addMovement(movementEvent);
                    return movementAggregation;
                },
                (k, agg1, agg2) -> agg2,
                Materialized.with(Serdes.String(), movementAggregationSerde)
            )
            .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()))
            .toStream()
            .filter((k,v) -> v.getMovementEvents().size() > 2)
            .map((k,v) -> KeyValue.pair(k.key(), new FraudEvent(v)));
        agg.print(Printed.toSysOut());
        agg.to(endTopic, Produced.with(Serdes.String(), fraudEventSerde));
        
    }
    
}

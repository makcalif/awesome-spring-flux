package com.cognizant.labs.spring.flux.awesomespringflux.controller;

import com.cognizant.labs.spring.flux.awesomespringflux.kafka.SampleConsumer;
import javafx.event.EventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class KafkaController {

    Logger log = LoggerFactory.getLogger(KafkaController.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");

    KafkaReceiver kafkaReceiver;
    KafkaSender<Integer, String> kafkaSender;

    Flux<String> subscriber;

//    public KafkaController(KafkaReceiver kafkaReceiver, KafkaSender<Integer, String> kafkaSender) {
//        this.kafkaReceiver = kafkaReceiver;
//        this.kafkaSender = kafkaSender;
//    }

    @Autowired
    public KafkaController(KafkaReceiver kafkaReceiver, KafkaSender<Integer, String> kafkaSender) {
        this.kafkaReceiver = kafkaReceiver;
        this.kafkaSender = kafkaSender;

        Flux<ReceiverRecord<Integer, String>> kafkaFlux = this.kafkaReceiver.receive();
        Flux<String> mapped = kafkaFlux.log()
                .doOnNext( r -> r.receiverOffset().acknowledge())
                .map(ReceiverRecord::value);


        subscriber  = mapped.subscribeWith(EmitterProcessor.create(false));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/kafkastream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux  getTopicData() {
        Flux<ReceiverRecord<Integer, String>> kafkaFlux = kafkaReceiver.receive();
        return kafkaFlux.log()
                .doOnNext( r -> r.receiverOffset().acknowledge())
                .map(ReceiverRecord::value);
    }

    @GetMapping(value = "/kafkastream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux another() {
        Flux<ReceiverRecord<Integer, String>> kafkaFlux = kafkaReceiver.receive();
        kafkaFlux.subscribe(record -> {
            ReceiverOffset offset = record.receiverOffset();
            System.out.printf("Received message: topic-partition=%s offset=%d timestamp=%s key=%d value=%s\n",
                    offset.topicPartition(),
                    offset.offset(),
                    dateFormat.format(new Date(record.timestamp())),
                    record.key(),
                    record.value());

            offset.acknowledge();
            //latch.countDown();
        });

        return kafkaFlux;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/kafkastream3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux  getTopicData3() {
         return subscriber.take(1);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value="/sendMessage")
    public void sendMessage(@RequestParam("index") String index) {

        kafkaSender.<Integer>send(Flux.just(Integer.parseInt(index))
                .map(i -> SenderRecord.create(new ProducerRecord<>("demo-topic-2ndrun", i, "Message_" + i), i)))
                .doOnError(e -> log.error("Send failed", e))
                .subscribe(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
                            r.correlationMetadata(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            dateFormat.format(new Date(metadata.timestamp())));
                });
    }
}

package priv.ilyan.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;


@ApplicationScoped
public class KafkaMessageProducer {
    @Inject
    @Channel("csv-to-kafka-out")
    Emitter<String> incentivePaymentEmitter;

    Jsonb jsonBuilder = JsonbBuilder.create();

    public void sendToKafkal(String data) {
		incentivePaymentEmitter.send( data );
	}
}

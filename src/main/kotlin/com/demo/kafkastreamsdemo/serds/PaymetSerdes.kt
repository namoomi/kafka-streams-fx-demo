package com.demo.kafkastreamsdemo.serds

import com.demo.kafkastreamsdemo.entity.PaymentEvent
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

class PaymetSerdes(serializer: Serializer<PaymentEvent>, deserializer: Deserializer<PaymentEvent>) :
    Serdes.WrapperSerde<PaymentEvent>(serializer, deserializer) {
        companion object {
            fun serds(): Serde<PaymentEvent> {
                val serializer = JsonSerializer<PaymentEvent>()
                val deserializer = JsonDeserializer(PaymentEvent::class.java)
                return Serdes.serdeFrom(serializer, deserializer)
            }
        }
}
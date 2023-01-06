package com.demo.kafkastreamsdemo.processor

import com.demo.kafkastreamsdemo.entity.PaymentEvent
import com.demo.kafkastreamsdemo.properties.KafkaStreamsDemoProperties
import com.demo.kafkastreamsdemo.serds.PaymetSerdes
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

val logger = KotlinLogging.logger { }

@Component
class PaymentProcessor(
    private val demoProperties: KafkaStreamsDemoProperties
) {
    private val SUPPORTED_RAILS = listOf("BANK_RAILS_KRW", "BANK_RAILS_USD")

    private val STRING_SERD = Serdes.String()
    private val LONG_SERD = Serdes.Long()

    @Autowired
    fun buildPipeline(streamsBuilder: StreamsBuilder) {
        val messageStream = streamsBuilder.stream(
            demoProperties.paymentInBoundTopic, Consumed.with(STRING_SERD, PaymetSerdes.serds())
        ).peek { key, payment -> logger.info { "Payment event received with key=$key, payment=$payment" } }
            .filter { key, value -> SUPPORTED_RAILS.contains(value.rails) }
            .peek { key, payment -> logger.info { "Filtered payment event with key=$key, payment=$payment" } }

        val currenciesBranches = messageStream.branch(
            { _, value -> value.currency == "KRW" },
            { _, value -> value.currency == "USD" }
        )

        // KRW -> rails-krw-topic, USD -> rails-usd-topic
        currenciesBranches[0].to(demoProperties.railsKrwOutboundTopic, Produced.with(STRING_SERD, PaymetSerdes.serds()))
        currenciesBranches[1].to(demoProperties.railsUsdOutboundTopic, Produced.with(STRING_SERD, PaymetSerdes.serds()))

        //환전
        val fxStream = currenciesBranches[1].mapValues { value ->
            val usdToKrwRate = 0.8
            PaymentEvent(
                paymentId = value.paymentId,
                amount = Math.round(value.amount * usdToKrwRate),
                currency = "KRW",
                fromAccount = value.fromAccount,
                toAccount = value.toAccount,
                rails = value.rails
            )
        }

        val mergedStreams = currenciesBranches[0].merge(fxStream)

        // stateful store KTable을 생성하여 계좌 잔액을 기록한다.
        // 환전액을 balance라는 store에 저장한다.
        mergedStreams.map { key, value -> KeyValue(value.fromAccount, value.amount) }
            .groupByKey(Grouped.with(STRING_SERD, LONG_SERD))
            .aggregate({ 0L }, { key, value, aggregate -> aggregate + value },
                Materialized.`as`<String?, Long?, KeyValueStore<Bytes, ByteArray>?>(
                    "balance"
                ).withKeySerde(STRING_SERD).withValueSerde(LONG_SERD)
            )
    }
}
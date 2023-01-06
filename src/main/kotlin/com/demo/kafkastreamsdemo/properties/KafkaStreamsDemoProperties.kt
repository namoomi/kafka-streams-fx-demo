package com.demo.kafkastreamsdemo.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka-streams.demo")
data class KafkaStreamsDemoProperties(
    val id: String,
    val paymentInBoundTopic: String,
    val railsUsdOutboundTopic: String,
    val railsKrwOutboundTopic: String,
)
package com.demo.kafkastreamsdemo.controller

import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/kafka-streams")
class BalanceController(
    private val factoryBean: StreamsBuilderFactoryBean
) {

    @GetMapping("/balance/{account}")
    fun getAccountBalance(@PathVariable account: String): Long? {
        println(account)

        val kafkaStreams = factoryBean.kafkaStreams
        val balances = kafkaStreams!!.store(
            StoreQueryParameters.fromNameAndType("balance", QueryableStoreTypes.keyValueStore<String, Long>())
        )
        return balances.get(account)
    }
}
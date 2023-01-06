package com.demo.kafkastreamsdemo

import com.demo.kafkastreamsdemo.properties.KafkaStreamsDemoProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans

@ComponentScan("com.demo")
@EnableConfigurationProperties(KafkaStreamsDemoProperties::class)
@SpringBootApplication
class KafkaStreamsDemoApplication

fun main(args: Array<String>) {
    runApplication<KafkaStreamsDemoApplication>(*args)
}

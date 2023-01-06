package com.demo.kafkastreamsdemo.mapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

object JsonMapper {
    val objectMapper = ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
        .findAndRegisterModules()

    fun <T> readFromJson(json: String, clazz: Class<T>): T {
        return objectMapper.readValue(json, clazz)
    }

    fun <T> readFromJson(byteArray: ByteArray, clazz: Class<T>): T {
        return objectMapper.readValue(byteArray, clazz)
    }

    fun writeToJson(obj: Any): String {
        return objectMapper.writeValueAsString(obj)
    }
}
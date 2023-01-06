package com.demo.kafkastreamsdemo.serds

import com.demo.kafkastreamsdemo.mapper.JsonMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import java.nio.charset.StandardCharsets

class JsonDeserializer<T>(private val destinationClass: Class<T>) : Deserializer<T> {
    override fun deserialize(topic: String, data: ByteArray): T {
        return JsonMapper.readFromJson(data, destinationClass)
    }
}
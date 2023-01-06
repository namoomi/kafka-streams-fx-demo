package com.demo.kafkastreamsdemo.serds

import com.demo.kafkastreamsdemo.mapper.JsonMapper
import org.apache.kafka.common.serialization.Serializer
import java.nio.charset.StandardCharsets

class JsonSerializer<T : Any> : Serializer<T> {
    override fun serialize(topic: String, data: T): ByteArray {
        return JsonMapper.writeToJson(data).toByteArray(StandardCharsets.UTF_8)
    }
}
package com.example.scorekeeper.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MutableStateMapSerializer : KSerializer<Map<String, Int>> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): Map<String, Int> {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: Map<String, Int>) {
        TODO("Not yet implemented")
    }
}
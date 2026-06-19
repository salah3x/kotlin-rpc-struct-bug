package com.example

import com.example.proto.Service
import com.google.protobuf.kotlin.Empty
import com.google.protobuf.kotlin.Struct
import com.google.protobuf.kotlin.StructInternal
import com.google.protobuf.kotlin.Value
import com.google.protobuf.kotlin.ValueInternal
import com.google.protobuf.kotlin.invoke
import kotlinx.rpc.grpc.client.GrpcClient
import kotlinx.rpc.grpc.server.GrpcServer
import kotlinx.rpc.registerService
import kotlinx.rpc.withService

fun Map<String, Long>.toStruct(): Struct =
    StructInternal().apply {
        fields =
            this@toStruct.mapValues { (_, value) ->
                // NumberValue only accept a Double, so we use `.toDouble()`
                ValueInternal().apply { kind = Value.Kind.NumberValue(value.toDouble()) }
            }
    }

fun Struct.toMap(): Map<String, Long> =
    fields.mapValues { (_, value) ->
        // This is the main issue, Struct only supports NumberValue, so conversions cause precision loss
        (value.kind as? Value.Kind.NumberValue)?.value?.toLong()
            ?: throw Exception("unsupported")
    }

class ServiceImpl : Service {
    override suspend fun Call(message: Struct): Empty {
        println("[Server] message=${message.toMap()}")
        return Empty {}
    }
}

suspend fun main() {
    val server = GrpcServer(8099) { services { registerService<Service>(::ServiceImpl) } }
    server.start()

    val echoService = GrpcClient("localhost", 8099) { credentials = plaintext() }.withService<Service>()
    val aMap = mapOf("key" to Long.MAX_VALUE - 1)

    println("[Client] message=$aMap")
    echoService.Call(aMap.toStruct())

    server.awaitTermination()
}

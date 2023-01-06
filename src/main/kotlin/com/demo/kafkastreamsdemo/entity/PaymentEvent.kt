package com.demo.kafkastreamsdemo.entity

data class PaymentEvent(
    val paymentId: String,
    val amount: Long,
    val currency: String,
    val fromAccount: String,
    val toAccount: String,
    val rails: String,
)
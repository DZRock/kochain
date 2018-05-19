package ru.sleepyrabbit.kochain.model

class TransactionHistory(private val sender: String,
                         private val recipient: String,
                         private val transactionId: String,
                         private val found: Float) {

    override fun toString(): String {
        return "TransactionHistory(sender='$sender', recipient='$recipient', transactionId='$transactionId', found=$found)"
    }
}
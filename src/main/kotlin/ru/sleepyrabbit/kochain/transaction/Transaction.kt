package ru.sleepyrabbit.kochain.transaction

import ru.sleepyrabbit.kochain.util.StringUtil
import java.security.PublicKey

class Transaction(private val sender: PublicKey,
                  private val recipient: PublicKey,
                  private val value: Float,
                  private val inputs: MutableList<TransactionInput>,
                  private val outputs: MutableList<TransactionOutput>) {

    lateinit var signature: ByteArray
    lateinit var transactionId: String

    private var sequence: Int = 0

    fun calculateHash(): String{
        sequence++
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        value.toString() +
                        sequence
        )
    }

}
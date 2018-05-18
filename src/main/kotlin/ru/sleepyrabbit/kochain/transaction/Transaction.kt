package ru.sleepyrabbit.kochain.transaction

import ru.sleepyrabbit.kochain.util.SignatureUtil
import ru.sleepyrabbit.kochain.util.StringUtil
import java.security.PrivateKey
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

    fun generateSignature(privateKey: PrivateKey){
        val data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)+value.toString()
        signature = SignatureUtil.applyECDSASig(privateKey, data)
    }

    fun verifiySignature(): Boolean{
        val data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)+value.toString()
        return SignatureUtil.verifyECDSASig(sender, data, signature)
    }

}
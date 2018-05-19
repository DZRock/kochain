package ru.sleepyrabbit.kochain.transaction

import ru.sleepyrabbit.kochain.util.SignatureUtil
import ru.sleepyrabbit.kochain.util.StringUtil
import java.security.PrivateKey
import java.security.PublicKey

class Transaction(val sender: PublicKey,
                  val recipient: PublicKey,
                  val value: Float,
                  val inputs: MutableList<TransactionInput>) {

    private val outputs= mutableListOf<TransactionOutput>()
    lateinit var signature: ByteArray
    lateinit var transactionId: String

    private var sequence: Int = 0

    private fun calculateHash(): String{
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

    fun verifySignature(): Boolean{
        val data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)+value.toString()
        return SignatureUtil.verifyECDSASig(sender, data, signature)
    }

    private fun getInputsValue(): Float{
        var total = 0f
        for(input in inputs){
            if(input.utxo == null)continue
            total += input.utxo.value
        }
        return total
    }

    fun getOutputValue(): Float{
        var total = 0f
        for(output in outputs)
            total += output.value
        return total
    }

    fun processTransaction(utxos: MutableMap<String, TransactionOutput>): Boolean {
        if(!verifySignature()){
            println("#Transaction Signature failed to verify")
            return false
        }

        for(input in inputs)
            input.utxo = utxos[input.transactionOutputId]!!

        //TODO set minimalTransaction
        val minimalTransaction = 0.1f
        if(getInputsValue() < minimalTransaction){
            println("#Transaction Inputs to small: ${getInputsValue()}")
            return false
        }

        val leftOver: Float = getInputsValue() - value
        transactionId = calculateHash()

        outputs+=TransactionOutput(recipient, value, transactionId)
        outputs+=TransactionOutput(sender, leftOver, transactionId)

        for(output in outputs)
            utxos[output.id] = output

        for(input in inputs){
            if(input.utxo==null)continue
            utxos.remove(input.utxo.id)
        }

        return true
    }

}
package ru.sleepyrabbit.kochain.transaction

import ru.sleepyrabbit.kochain.util.StringUtil
import java.security.PublicKey

class TransactionOutput(private val recipient: PublicKey, value: Float, parentTransactionId: String) {

    val id: String = StringUtil.applySha256(StringUtil.getStringFromKey(recipient)+
                                                value.toString()+
                                                parentTransactionId)

    fun isMine(publicKey: PublicKey): Boolean{
        return publicKey==recipient
    }
}
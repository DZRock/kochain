package ru.sleepyrabbit.kochain.util

import ru.sleepyrabbit.kochain.transaction.Transaction
import java.security.Key
import java.security.MessageDigest
import java.util.*

class StringUtil {

    companion object {

        fun applySha256(input: String): String {
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(input.toByteArray(charset("UTF-8")))
                val hexString = StringBuffer()
                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }
                return hexString.toString()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        fun getStringFromKey(key: Key): String {
            return Base64.getEncoder().encodeToString(key.encoded)
        }

        fun getMerkleRoot(transactions: MutableList<Transaction>): String{
            var count = transactions.size
            var previousTreeLayer = mutableListOf<String>()
            for(transaction in transactions){
                previousTreeLayer.add(transaction.transactionId)
            }

            var treeLayer = previousTreeLayer
            while(count > 1){
                treeLayer = mutableListOf()
                for(i in 1..previousTreeLayer.size)
                    treeLayer.add(applySha256(previousTreeLayer[i-1] + previousTreeLayer[i]))
                count = treeLayer.size
                previousTreeLayer=treeLayer
            }

            return if(treeLayer.size == 1)
                treeLayer[0]
            else
                ""
        }
    }

}
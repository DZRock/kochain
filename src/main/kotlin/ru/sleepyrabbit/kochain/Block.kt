package ru.sleepyrabbit.kochain

import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.transaction.TransactionOutput
import ru.sleepyrabbit.kochain.util.StringUtil
import java.util.*

class Block(var hash: String = "",
            val previousHash: String,
            private var nonce: Int) {

    private val transactions = mutableListOf<Transaction>()
    private val timestamp: Long = Date().time
    private var merkleRoot = ""

    init {
        if(hash.isEmpty())
            hash = calculateHash()
    }

    fun calculateHash() : String{
        return StringUtil.applySha256 (
        StringUtil.applySha256(previousHash) +
            StringUtil.applySha256(timestamp.toString()) +
            StringUtil.applySha256(nonce.toString())+
            StringUtil.applySha256(transactions.toString())
        )
    }

    fun mineBlock(difficulty: Int){
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        val target = String(CharArray(difficulty)).replace('\u0000','0')
        while(hash.substring(0, difficulty) != target){
            nonce ++
            hash = calculateHash()
        }
        println("Block mined!!! : $hash")
    }

    fun addTransaction(transaction: Transaction, utxos: MutableMap<String, TransactionOutput>): Boolean{
        if(previousHash!=""){
            if(!transaction.processTransaction(utxos)){
                println("Transaction failed to process. Discarded")
                return false
            }
        }
        transactions.add(transaction)
        println("Transaction successfully added to Block")
        return true
    }

}
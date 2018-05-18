package ru.sleepyrabbit.kochain

import ru.sleepyrabbit.kochain.util.StringUtil

class Block(var hash: String = "",
            val previousHash: String,
            private val timestamp: Long,
            private val data: ByteArray) {

    private var nonce = 55

    init {
        if(hash.isEmpty())
            hash = calculateHash()
    }

    fun calculateHash() : String{
        return StringUtil.applySha256 (
        StringUtil.applySha256(previousHash) +
            StringUtil.applySha256(timestamp.toString()) +
            StringUtil.applySha256(nonce.toString())+
            StringUtil.applySha256(String(data))
        )
    }

    fun mineBlock(difficulty: Int){
        val target = String(CharArray(difficulty)).replace('\u0000','0')
        while(hash.substring(0, difficulty) != target){
            nonce ++
            hash = calculateHash()
        }
        println("Block mined!!! : $hash")
    }

}
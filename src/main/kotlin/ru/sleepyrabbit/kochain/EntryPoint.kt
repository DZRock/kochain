package ru.sleepyrabbit.kochain

import com.google.gson.GsonBuilder
import java.nio.charset.Charset
import java.util.*

fun main(args: Array<String>){
    val difficulty = 5
    val gson = GsonBuilder().setPrettyPrinting().create()
    val blockChain = mutableListOf<Block>()

    blockChain += Block("","0", Date().time, "Hello world".toByteArray(Charset.defaultCharset()))
    blockChain[0].mineBlock(difficulty)

    for(i in 0..10){
        val block = Block("", blockChain[i].hash, Date().time, "${i + 1} block".toByteArray(Charset.defaultCharset()))
        block.mineBlock(difficulty)
        blockChain += block
    }

    val blockChainSerialized = gson.toJson(blockChain)
    println(blockChainSerialized)

    println(isChainValid(blockChain,difficulty))
}

fun isChainValid(blockChain: MutableList<Block>, difficulty: Int): Boolean {
    val hashTarget = String(CharArray(difficulty)).replace('\u0000','0')

    for(i in 1 until blockChain.size){
        val currentBlock = blockChain[i]
        val previousBlock = blockChain[i-1]

        if(currentBlock.hash != currentBlock.calculateHash()){
            println("Current hashes not equal." + currentBlock.hash)
            return false
        }

        if(previousBlock.hash != currentBlock.previousHash){
            println("Previous Hashes not equal " + currentBlock.hash)
            return false
        }

        if(currentBlock.hash.substring(0, difficulty) != hashTarget){
            println("This block hasn`t been mined")
            return false
        }
    }

    return true
}
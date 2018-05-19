package ru.sleepyrabbit.kochain

import com.google.gson.GsonBuilder
import ru.sleepyrabbit.kochain.util.CommonUtil

fun main(args: Array<String>){
    val difficulty = 5
    val gson = GsonBuilder().setPrettyPrinting().create()
    val blockChain = mutableListOf<Block>()

    blockChain += Block("","0", 55)
    blockChain[0].mineBlock(difficulty)

    for(i in 0..10){
        val block = Block("", blockChain[i].hash, 55)
        block.mineBlock(difficulty)
        blockChain += block
    }

    val blockChainSerialized = gson.toJson(blockChain)
    println(blockChainSerialized)

    println(CommonUtil.isChainValid(blockChain,difficulty))
}
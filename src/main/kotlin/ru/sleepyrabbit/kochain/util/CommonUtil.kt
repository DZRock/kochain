package ru.sleepyrabbit.kochain.util

import ru.sleepyrabbit.kochain.Block

class CommonUtil {

    companion object {

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

    }

}
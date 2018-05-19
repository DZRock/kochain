package ru.sleepyrabbit.kochain

import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Test
import ru.sleepyrabbit.kochain.util.CommonUtil
import java.nio.charset.Charset
import java.util.*

class BlockChainTest {

    companion object {

        fun buildBlockChain(difficulty:Int, size: Int): MutableList<Block>{
            val blockChain = mutableListOf<Block>()

            blockChain += Block("","0", 55)
            blockChain[0].mineBlock(difficulty)

            for(i in 0..5){
                val block = Block("", blockChain[i].hash, 55)
                block.mineBlock(difficulty)
                blockChain += block
            }

            return blockChain
        }
    }

    @Test
    fun testMining(){
        val difficulty = 5
        val gson = GsonBuilder().setPrettyPrinting().create()

        val blockChain = buildBlockChain(difficulty, 5)

        val blockChainSerialized = gson.toJson(blockChain)
        println(blockChainSerialized)

        assert(CommonUtil.isChainValid(blockChain,difficulty))
    }

}
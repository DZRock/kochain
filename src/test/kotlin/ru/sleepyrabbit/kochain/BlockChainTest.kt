package ru.sleepyrabbit.kochain

import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Test
import ru.sleepyrabbit.kochain.util.CommonUtil
import java.nio.charset.Charset
import java.util.*

class BlockChainTest {

    @Test
    fun testMining(){
        val difficulty = 5
        val gson = GsonBuilder().setPrettyPrinting().create()
        val blockChain = mutableListOf<Block>()

        blockChain += Block("","0", Date().time, "Hello world".toByteArray(Charset.defaultCharset()))
        blockChain[0].mineBlock(difficulty)

        for(i in 0..5){
            val block = Block("", blockChain[i].hash, Date().time, "${i + 1} block".toByteArray(Charset.defaultCharset()))
            block.mineBlock(difficulty)
            blockChain += block
        }

        val blockChainSerialized = gson.toJson(blockChain)
        println(blockChainSerialized)

        assert(CommonUtil.isChainValid(blockChain,difficulty))
    }

}
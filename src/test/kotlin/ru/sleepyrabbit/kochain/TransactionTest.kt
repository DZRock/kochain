package ru.sleepyrabbit.kochain

import org.junit.jupiter.api.Test
import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.transaction.TransactionOutput
import ru.sleepyrabbit.kochain.util.StringUtil
import java.security.Security

class TransactionTest {

    private val walletA = Wallet()
    private val walletB = Wallet()

    init{
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
    }

    @Test
    fun testSigning(){
        println("Private and public keys:")
        println(StringUtil.getStringFromKey(walletA.privateKey))
        println(StringUtil.getStringFromKey(walletA.publicKey))

        val transaction = Transaction(walletA.publicKey, walletB.publicKey, 5f, mutableListOf())
        transaction.generateSignature(walletA.privateKey)

        println("Is signature verified")
        assert(transaction.verifySignature())
    }

    @Test
    fun testTransactionProcessing(){
        val difficulty = 5
        val blockChain = BlockChainTest.buildBlockChain(difficulty, 5)

        var UTXOs = mutableMapOf<String, TransactionOutput>()


    }

}
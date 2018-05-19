package ru.sleepyrabbit.kochain

import org.junit.jupiter.api.Test
import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.transaction.TransactionOutput
import ru.sleepyrabbit.kochain.util.CommonUtil
import java.security.Security

class BlockChainTest {

    private val walletA = Wallet()
    private val walletB = Wallet()
    private val coinBase = Wallet()
    private val difficulty = 5
    private val utxos = mutableMapOf<String, TransactionOutput>()
    private lateinit var blockChain: MutableList<Block>

    init{
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
    }

    @Test
    fun testTransactionProcessing(){
        blockChain = mutableListOf()

        val genesisTransaction = Transaction(coinBase.publicKey, walletA.publicKey, 100f, mutableListOf())
        genesisTransaction.generateSignature(coinBase.privateKey)
        genesisTransaction.transactionId = "0"
        genesisTransaction.outputs.add(TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId))
        utxos[genesisTransaction.outputs[0].id] = genesisTransaction.outputs[0]

        println("Creating and Mining Genesis block...")
        val genesis = Block("0",5)
        genesis.addTransaction(genesisTransaction, utxos)

        addBlock(genesis)
        walletA.filterMine(utxos)
        walletB.filterMine(utxos)

        val block1 = Block(genesis.hash,5)
        println("WalletA's balance is: ${walletA.getBalance()}")
        println("WalletA is Attempting to send funds (40) to WalletB...")

        block1.addTransaction(walletA.sendFounds(walletB.publicKey, 40f), utxos)
        addBlock(block1)
        walletA.filterMine(utxos)
        walletB.filterMine(utxos)
        println("WalletA's balance is: ${walletA.getBalance()}")
        println("WalletB's balance is: ${walletB.getBalance()}")

        val block2 = Block(block1.hash, 5)
//        println("WalletA Attempting to send more funds (1000) than it has...")
        block2.addTransaction(walletA.sendFounds(walletB.publicKey, 10f), utxos)
        addBlock(block2)
        walletA.filterMine(utxos)
        walletB.filterMine(utxos)
        println("WalletA's balance is: " + walletA.getBalance())
        println("WalletB's balance is: " + walletB.getBalance())

        val block3 = Block(block2.hash, 5)
        println("WalletB is Attempting to send funds (20) to WalletA...")
        block3.addTransaction(walletB.sendFounds(walletA.publicKey, 20f), utxos)
        addBlock(block3)
        walletA.filterMine(utxos)
        walletB.filterMine(utxos)
        println("WalletA's balance is: ${walletA.getBalance()}")
        println("WalletB's balance is: ${walletB.getBalance()}")

        assert(CommonUtil.isChainValid(blockChain, genesisTransaction, difficulty))
    }

    private fun addBlock(block: Block){
        block.mineBlock(difficulty)
        blockChain.add(block)
    }
}
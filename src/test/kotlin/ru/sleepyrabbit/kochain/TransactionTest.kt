package ru.sleepyrabbit.kochain

import org.junit.jupiter.api.Test
import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.util.StringUtil
import java.security.Security


class TransactionTest {

    @Test
    fun testSigning(){
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())

        val walletA = Wallet()
        val walletB = Wallet()

        println("Private and public keys:")
        println(StringUtil.getStringFromKey(walletA.privateKey))
        println(StringUtil.getStringFromKey(walletA.publicKey))

        val transaction = Transaction(walletA.publicKey, walletB.publicKey, 5f, mutableListOf(), mutableListOf())
        transaction.generateSignature(walletA.privateKey)

        println("Is signature verified")
        assert(transaction.verifiySignature())
    }

}
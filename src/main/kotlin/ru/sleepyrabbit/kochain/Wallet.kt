package ru.sleepyrabbit.kochain

import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.transaction.TransactionInput
import ru.sleepyrabbit.kochain.transaction.TransactionOutput
import java.security.*
import java.security.spec.ECGenParameterSpec

class Wallet {
    lateinit var privateKey: PrivateKey
    lateinit var publicKey: PublicKey

    private val utxos = mutableMapOf<String, TransactionOutput>()

    init{
        generateKeyPair()
    }

    private fun generateKeyPair(){
        try {
            Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
            val keyGen = KeyPairGenerator.getInstance("ECDSA", "BC")
            val random = SecureRandom.getInstance("SHA1PRNG")
            val ecSpec = ECGenParameterSpec("prime192v1")

            keyGen.initialize(ecSpec, random)
            val keyPair = keyGen.generateKeyPair()

            publicKey = keyPair.public
            privateKey = keyPair.private
        }catch (e: Exception){
            throw RuntimeException(e)
        }
    }

    fun getBalance():Float{
        var total = 0f
        for(item in utxos)
            total+=item.value.value
        return total
    }

    fun filterMine(allUtxos: MutableMap<String, TransactionOutput>){
        for(item in allUtxos)
            with(item.value){
                if(this.isMine(publicKey))
                    utxos[this.id]=this
            }
    }

    fun sendFounds(recipient: PublicKey, value: Float): Transaction{
        if(getBalance()<value)throw RuntimeException("Insufficient funds")

        val inputs = mutableListOf<TransactionInput>()
        var total = 0f

        for(item in utxos){
            val utxo:TransactionOutput = item.value
            total += utxo.value
            inputs+=TransactionInput(utxo.id)
            if(total > value)break
        }

        val transaction = Transaction(publicKey, recipient, value, inputs)
        transaction.generateSignature(privateKey)

        for(input in inputs)
            utxos.remove(input.transactionOutputId)

        return transaction
    }
}
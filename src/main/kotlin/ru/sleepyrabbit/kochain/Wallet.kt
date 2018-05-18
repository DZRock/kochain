package ru.sleepyrabbit.kochain

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec

class Wallet() {
    lateinit var privateKey: PrivateKey
    lateinit var publicKey: PublicKey

    init{
        generateKeyPair()
    }

    private fun generateKeyPair(){
        try {
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
}
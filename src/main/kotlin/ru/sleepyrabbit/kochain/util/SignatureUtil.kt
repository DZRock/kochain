package ru.sleepyrabbit.kochain.util

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

class SignatureUtil {

    companion object {

        fun applyECDSASig(privateKey: PrivateKey, input: String) : ByteArray{
            val dsa: Signature
            val output: ByteArray
            try{
                dsa = Signature.getInstance("ECDSA", "BC")
                dsa.initSign(privateKey)
                val strBytes = input.toByteArray()
                dsa.update(strBytes)
                val realSign = dsa.sign()
                output = realSign
            }catch (e: Exception){
                throw RuntimeException(e)
            }
            return output
        }

        fun verifyECDSASig(publicKey: PublicKey, data: String, signature: ByteArray): Boolean{
            try{
                val ecdsaVerify = Signature.getInstance("ECDSA", "BC")
                ecdsaVerify.initVerify(publicKey)
                ecdsaVerify.update(data.toByteArray())
                return ecdsaVerify.verify(signature)
            }catch (e: Exception){
                throw RuntimeException(e)
            }
        }
    }

}
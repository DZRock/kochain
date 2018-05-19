package ru.sleepyrabbit.kochain.util

import ru.sleepyrabbit.kochain.Block
import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.transaction.TransactionOutput

class CommonUtil {

    companion object {

        fun isChainValid(blockChain: MutableList<Block>, genesisTransaction: Transaction, difficulty: Int): Boolean {
            val hashTarget = String(CharArray(difficulty)).replace('\u0000','0')
            val tempUTXOs = mutableMapOf<String, TransactionOutput>()
            tempUTXOs[genesisTransaction.outputs[0].id] = genesisTransaction.outputs[0]

            for(i in 1 until blockChain.size){
                val currentBlock = blockChain[i]
                val previousBlock = blockChain[i-1]

                if(currentBlock.hash != currentBlock.calculateHash()){
                    println("#Current hashes not equal." + currentBlock.hash)
                    return false
                }

                if(previousBlock.hash != currentBlock.previousHash){
                    println("#Previous Hashes not equal " + currentBlock.hash)
                    return false
                }

                if(currentBlock.hash.substring(0, difficulty) != hashTarget){
                    println("#This block hasn`t been mined")
                    return false
                }

                for(j in 0..currentBlock.transactions.size){
                    val currentTransaction = currentBlock.transactions[0]

                    if(!currentTransaction.verifySignature()){
                        println("#Signature on Transaction($j) is Invalid")
                        return false
                    }

                    if(currentTransaction.getInputsValue() != currentTransaction.getInputsValue()){
                        println("#Inputs are note equal to outputs on Transaction($j)")
                        return false
                    }

                    for (input in currentTransaction.inputs) {
                        val tempOutput = tempUTXOs[input.transactionOutputId]

                        if (tempOutput == null) {
                            System.out.println("#Referenced input on Transaction($j) is Missing")
                            return false
                        }

                        if (input.utxo.value != tempOutput.value) {
                            System.out.println("#Referenced input Transaction($j) value is Invalid")
                            return false
                        }

                        tempUTXOs.remove(input.transactionOutputId)
                    }

                    for (output in currentTransaction.outputs) {
                        tempUTXOs[output.id] = output
                    }

                    if (currentTransaction.outputs[0].recipient !== currentTransaction.recipient) {
                        println("#Transaction($j) output recipient is not who it should be")
                        return false
                    }
                    if (currentTransaction.outputs[1].recipient !== currentTransaction.sender) {
                        println("#Transaction($j) output 'change' is not sender.")
                        return false
                    }
                }
            }

            println("Blockchain is valid")
            return true
        }

    }

}
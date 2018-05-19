package ru.sleepyrabbit.kochain.transaction

class TransactionInput(val transactionOutputId: String){

    lateinit var utxo: TransactionOutput

}
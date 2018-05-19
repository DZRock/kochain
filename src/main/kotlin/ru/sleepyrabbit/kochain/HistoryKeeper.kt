package ru.sleepyrabbit.kochain

import com.google.gson.Gson
import ru.sleepyrabbit.kochain.model.TransactionHistory
import ru.sleepyrabbit.kochain.transaction.Transaction
import ru.sleepyrabbit.kochain.util.StringUtil

class HistoryKeeper {

    companion object {
        fun addTransactionHistory(transaction: Transaction){
            //TODO save to local db
            val transactionHistory = TransactionHistory(
                    StringUtil.getStringFromKey(transaction.sender),
                    StringUtil.getStringFromKey(transaction.recipient),
                    transaction.transactionId,
                    transaction.value
                    )
            println(Gson().toJson(transactionHistory))
        }
    }

}


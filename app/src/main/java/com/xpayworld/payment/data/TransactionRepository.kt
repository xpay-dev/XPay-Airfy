package com.xpayworld.payment.data

class TransactionRepository private constructor(
        private val transDao : TransactionDao
){

    fun getTransaction() = transDao.getTransaction()

    suspend fun createTransaction(trans : Transaction){
        transDao.insertTransaction(trans)
    }

    fun deleteAllTransaction(){
        transDao.deleteAllTransaction()
    }

    fun updateTransaction(isSync : Boolean , orderId : String){
        transDao.updateSync(isSync = isSync, orderId =  orderId)
    }

    fun updateSignatureTransaction(sign : String , orderId: String){
        transDao.updateSignature(sign,orderId)
    }

    fun  deleteTranscation(orderId: String){
        transDao.deleteTransaction(orderId= orderId)
    }

    companion object{

        @Volatile private  var instance: TransactionRepository? = null

        fun getInstance(transDao: TransactionDao) = instance ?: synchronized(this){
            instance ?: TransactionRepository(transDao).also { instance = it }
        }
    }
}
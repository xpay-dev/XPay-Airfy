package com.xpayworld.payment.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface  TransactionDao {

    @Query( "SELECT * FROM `transaction`")
    fun getTransaction():  List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(vararg txn : Transaction)

    @Query("DELETE FROM `transaction`")
    fun deleteAllTransaction()

    @Query("DELETE FROM `transaction` WHERE orderId =:orderId")
    fun deleteTransaction(orderId: String)

    @Query("UPDATE `transaction` SET isSync = :isSync WHERE orderId = :orderId")
    fun updateSync(isSync: Boolean , orderId: String)

    @Query("UPDATE `transaction` SET signature = :sign WHERE orderId = :orderId")
    fun updateSignature(sign: String , orderId: String)
}
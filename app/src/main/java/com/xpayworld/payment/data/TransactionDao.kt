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

    @Query("UPDATE `transaction` SET transType = :status ,isSync = :isSync  WHERE orderId = :orderId")
    fun updateSync(status : String,isSync: Boolean , orderId: String)

    @Query("UPDATE `transaction` SET signature = :sign  WHERE orderId = :orderId")
    fun updateSignature(sign: String , orderId: String)

    @Query("UPDATE `transaction` SET emailAddress = :email  WHERE orderId = :orderId")
    fun updateEmail(email: String , orderId: String)

    @Query("SELECT * FROM `transaction` WHERE orderId =:orderId")
    fun searchTransaction(orderId: String):  List<Transaction>
}
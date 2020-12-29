package com.anushka.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface SubscriberDAO {

    @Insert
    suspend fun  insertSubscriber(subscriber: Subscriber) : Long

    @Update
    suspend fun  updateSubscriber(subscriber: Subscriber) : Int

    @Delete
    suspend fun  deleteSubscriber(subscriber: Subscriber) : Int

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll() : Int


    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers() : LiveData<List<Subscriber>>





    //Variation of insert function
    /*

    @Insert
    suspend fun  insertSubscriber2(subscriber: Subscriber) : Long

    @Insert
    suspend fun  insertSubscribers(subscriber1: Subscriber,subscriber2: Subscriber,subscriber3: Subscriber) : List<Long>

    @Insert
    fun insertSubscribers2(subscriber : Subscriber , subscribers : List<Subscriber>) : List<Long>
*/


}
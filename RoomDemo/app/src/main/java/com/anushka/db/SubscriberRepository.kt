package com.anushka.db

class SubscriberRepository (private val dao : SubscriberDAO) {

    val subscriberDAO = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber) {
        dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber) {
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber) {
        dao.deleteSubscriber(subscriber)


    }
    suspend  fun deleteAll() {
        dao.deleteAll()
    }
}
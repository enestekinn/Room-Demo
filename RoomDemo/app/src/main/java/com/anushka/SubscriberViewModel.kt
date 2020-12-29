package com.anushka

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.anushka.db.Subscriber
import com.anushka.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel (
    private val repository: SubscriberRepository
        ) : ViewModel() ,Observable{


    val subscribers = repository.subscriberDAO

    private var isUpdateOrDelete = false
    private  lateinit var  subscriberToUpdateOrDelete: Subscriber
            @Bindable
            val inputName =MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()

    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"

        clearAllOrDeleteButtonText.value = "Clear All"
    }
    fun saveOrUpdate () {
if (isUpdateOrDelete) {
    subscriberToUpdateOrDelete.name = inputName.value!!
    subscriberToUpdateOrDelete.email = inputEmail.value!!
    update(subscriberToUpdateOrDelete)
}else {
    val name = inputName.value!!
    val email = inputEmail.value!!
    insert(Subscriber(0,name,email))
    inputName.value = null
    inputEmail.value = null
}




    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        }else {
            clearAll()
        }
    }

    fun insert(subscriber : Subscriber) : Job =
        viewModelScope.launch {
            repository.insert(subscriber)
        }

    fun update(subscriber : Subscriber) : Job =
        viewModelScope.launch {
            repository.update(subscriber)
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value  ="Save"
            clearAllOrDeleteButtonText.value = "Clear All"
        }

    fun delete(subscriber : Subscriber) : Job =
        viewModelScope.launch {
            repository.delete(subscriber)
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value  ="Save"
            clearAllOrDeleteButtonText.value = "Clear All"
        }
    fun clearAll() : Job =
        viewModelScope.launch {
            repository.deleteAll()
        }

    fun initUpdateAndDelete(subscriber : Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete =subscriber
        saveOrUpdateButtonText.value  ="Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}





package com.anushka

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
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

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
    get() =  statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"

        clearAllOrDeleteButtonText.value = "Clear All"
    }
    fun saveOrUpdate () {
        if (inputName.value == null) {
            statusMessage.value = Event("Please enter subscriber's name")

        }else if (inputEmail.value == null){
            statusMessage.value = Event("Please enter subscriber's email")

        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please enter correct email address")

        }else {
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

            val newRowId : Long =            repository.insert(subscriber)

            if (newRowId > -1) {
                statusMessage.value = Event("Subscriber Inserted Successfully")

            }else {
                statusMessage.value = Event("Error Occurred ")

            }
        }

    fun update(subscriber : Subscriber) : Job =
        viewModelScope.launch {

            val noOfRows : Int = repository.update(subscriber)
            if (noOfRows > 0) {
                repository.update(subscriber)
                inputName.value = null
                inputEmail.value = null
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value  ="Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("Subscriber Updated Successfully")
            }
            statusMessage.value = Event("$noOfRows Row Updated Successfully")


        }

    fun delete(subscriber : Subscriber) : Job =
        viewModelScope.launch {
            val noOfRowsDeleted = repository.delete(subscriber)
            if (noOfRowsDeleted > 0) {
                repository.delete(subscriber)
                inputName.value = null
                inputEmail.value = null
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value  ="Save"
                clearAllOrDeleteButtonText.value = "Clear All"

                statusMessage.value = Event("$noOfRowsDeleted Deleted Successfully")
            }else {
                statusMessage.value = Event("Error Occurred")

            }


        }
    fun clearAll() : Job =
        viewModelScope.launch {
            val noOfRowDeleted = repository.deleteAll()
            repository.deleteAll()

            if (noOfRowDeleted > 0) {
                statusMessage.value = Event("$noOfRowDeleted Subscribers Deleted Successfully")

            }else {
                statusMessage.value = Event("Error Occurred")
            }


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





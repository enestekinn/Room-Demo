package com.anushka.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anushka.SubscriberViewModel
import com.anushka.SubscriberViewModelFactory
import com.anushka.db.SubscriberDatabase
import com.anushka.db.SubscriberRepository
import com.anushka.roomdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
private lateinit var  binding : ActivityMainBinding
private lateinit var subscriberViewModel : SubscriberViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        displaySubscriberList()


    }


    private fun displaySubscriberList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MYTAG",it.toString())
        })
    }
}

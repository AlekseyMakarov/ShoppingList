package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var viewModel: MainViewModel;
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel= ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            Log.d("MainActivity", it.toString())
            if (count==0){
                count++
                val itemToDelete = it[0]
                viewModel.changeEnableState(itemToDelete)
            }
        }
//        viewModel.addShopItem(ShopItem("jekk", 0, false, 0))
//        viewModel.addShopItem(ShopItem("jesdfdsfkk", 0, false, 1))
    }
}
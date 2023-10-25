package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var viewModel: MainViewModel
    private lateinit var rvShopList: RecyclerView
    private lateinit var adapter: RecyclerViewShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvShopList = findViewById(R.id.rv_shop_list)
        setupAdapter()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            adapter.shopList=it

        }
    }


    fun setupAdapter(){
        adapter = RecyclerViewShopListAdapter()
        rvShopList.adapter = adapter
        adapter.onShopItemLongClickListener = {viewModel.changeEnableState(it)}
        adapter.onShopItemClickListener = {Log.d("onShopItemClickListener", it.toString())}
    }
}
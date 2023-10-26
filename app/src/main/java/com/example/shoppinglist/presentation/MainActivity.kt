package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var viewModel: MainViewModel
    private lateinit var rvShopList: RecyclerView
    private lateinit var shopListAdapter: RecyclerViewShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvShopList = findViewById(R.id.rv_shop_list)
        setupAdapter()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it

        }
    }


    fun setupAdapter() {
        with(rvShopList) {
            shopListAdapter = RecyclerViewShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                RecyclerViewShopListAdapter.DISABLED_TYPE,
                RecyclerViewShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                RecyclerViewShopListAdapter.ENABLED_TYPE,
                RecyclerViewShopListAdapter.MAX_POOL_SIZE
            )
            shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }
            shopListAdapter.onShopItemClickListener = { Log.d("onShopItemClickListener", it.toString()) }
        }
    }
}
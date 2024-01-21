package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), OnCloseShopItemFragment {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: RecyclerViewShopListAdapter
    private lateinit var floatingActionButton: FloatingActionButton
    private var shopItemContainer: FragmentContainerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.fragment_container_land)

        setupRecyclerView()
        floatingActionButton = findViewById<FloatingActionButton?>(R.id.button_add_shop_item)
            .also {
                it.setOnClickListener {
                    if (isOnePaneMode()) {
                        val intent = ShopItemActivity.newIntentAddItem(this)
                        startActivity(intent)
                    } else {
                        val fragment = ShopItemFragment.newInstanceAdd()
                        launchFragment(fragment)
                    }

                }
            }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }


    private fun setupRecyclerView() {
        val rvShopList: RecyclerView = findViewById(R.id.rv_shop_list)
        setupAdapter(rvShopList)
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(/* dragDirs = */ 0,/* swipeDirs = */
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shopItem = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(shopItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("onShopItemClickListener", it.toString())
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newInstanceEdit(it.id)
                launchFragment(fragment)
            }

        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setupAdapter(rvShopList: RecyclerView) {
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
        }
    }

    private fun isOnePaneMode(): Boolean = shopItemContainer == null
    private fun launchFragment(fragment: ShopItemFragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_land, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onClose() {
        supportFragmentManager.popBackStack()
    }
}
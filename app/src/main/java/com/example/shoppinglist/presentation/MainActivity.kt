package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var viewModel: MainViewModel;
    private lateinit var ll:LinearLayout;
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ll=findViewById(R.id.linear_layout)
        viewModel= ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
//            Log.d("MainActivity", it.toString())
//            if (count==0){
//                count++
//                val itemToDelete = it[0]
//                viewModel.changeEnableState(itemToDelete)
//            }
            ll.removeAllViews()
            for (i in it){
                val status = if(i.enabled) R.layout.item_shop_enabled else R.layout.item_shop_disabled;
                val view = LayoutInflater.from(this).inflate(status, ll, false)
                val tv_name = view.findViewById<TextView>(R.id.tv_name)
                val tv_count = view.findViewById<TextView>(R.id.tv_count)
                tv_name.text = i.name
                tv_count.text = i.count.toString()
                view.setOnLongClickListener {
                    viewModel.changeEnableState(i)
                    true
                }
                ll.addView(view)
        }
        }

//        viewModel.addShopItem(ShopItem("jekk", 0, false, 0))
//        viewModel.addShopItem(ShopItem("jesdfdsfkk", 0, false, 1))
    }
}
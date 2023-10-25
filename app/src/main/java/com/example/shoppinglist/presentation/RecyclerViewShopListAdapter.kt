package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class RecyclerViewShopListAdapter:
    RecyclerView.Adapter<RecyclerViewShopListAdapter.ShopListViewHolder>() {
    companion object{
        const val ENABLED_TYPE = 0
        const val DISABLED_TYPE = 1
        const val MAX_POOL_SIZE = 15
    }
    var count = 0

    var shopList = listOf <ShopItem>()
    set(value){
        field = value
        notifyDataSetChanged()

    }
    var onShopItemLongClickListener: ((ShopItem)->Unit)? = null
    var onShopItemClickListener: ((ShopItem)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        Log.d("OnCreateViewHolder", "count: ${++count}")

        val layoutType = when(viewType){
            ENABLED_TYPE -> R.layout.item_shop_enabled
            DISABLED_TYPE -> R.layout.item_shop_disabled
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutType, parent, false)
        return ShopListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        holder.tvCount.text = shopList[position].count.toString()
        holder.tvName.text = shopList[position].name
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopList[position])
            true
        }
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) ENABLED_TYPE else DISABLED_TYPE
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    class ShopListViewHolder(val view: View): ViewHolder(view){
        val tvName: TextView = view.findViewById<TextView>(R.id.tv_name)
        val tvCount: TextView = view.findViewById<TextView>(R.id.tv_count)
    }
}
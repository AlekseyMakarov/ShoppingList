package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class RecyclerViewShopListAdapter :
    ListAdapter<ShopItem, ShopListViewHolder>(ShopItemDiffCallback()) {
    companion object {
        const val ENABLED_TYPE = 0
        const val DISABLED_TYPE = 1
        const val MAX_POOL_SIZE = 15
    }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val layoutType = when (viewType) {
            ENABLED_TYPE -> R.layout.item_shop_enabled
            DISABLED_TYPE -> R.layout.item_shop_disabled
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutType, parent, false)
        return ShopListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvCount.text = item.count.toString()
        holder.tvName.text = item.name
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) ENABLED_TYPE else DISABLED_TYPE
    }
}
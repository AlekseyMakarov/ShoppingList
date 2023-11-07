package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.SortedList
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID
import com.example.shoppinglist.domain.ShopListRepository
import java.util.Random
import java.util.SortedSet

object ShopListRepositoryImpl : ShopListRepository {
    private val shopList = sortedSetOf<ShopItem>({ p0, p1 -> p0.id.compareTo(p1.id) })
    private val shopListLiveData = MutableLiveData<List<ShopItem>>()

    private var autoIncrementId = 0

    init {
//        shopListLiveData.value= shopList
        for (i in 0 until 10000){
            val item = ShopItem("Name $i", i, Random().nextBoolean())
            addShopItem(item)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateShopListLD()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateShopListLD()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val itemToDelete = getShopItem(shopItem.id)
        shopList.remove(itemToDelete)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    private fun updateShopListLD(){
        shopListLiveData.value = shopList.toList()
    }
}
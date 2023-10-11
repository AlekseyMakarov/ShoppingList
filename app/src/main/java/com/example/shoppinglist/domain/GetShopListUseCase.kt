package com.example.shoppinglist.domain

import android.util.Log
import androidx.lifecycle.LiveData

class GetShopListUseCase (private val shopListRepository: ShopListRepository){
    fun getShopList(): LiveData<List<ShopItem>> {
        val ret = shopListRepository.getShopList()
        Log.d("getShopList", ret.toString())
        return ret
    }
}
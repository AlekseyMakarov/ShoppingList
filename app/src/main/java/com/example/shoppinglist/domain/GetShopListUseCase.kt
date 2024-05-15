package com.example.shoppinglist.domain

import android.util.Log
import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> {
        val ret = shopListRepository.getShopList()
        Log.d("getShopList", ret.toString())
        return ret
    }
}
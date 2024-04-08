package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID
import com.example.shoppinglist.domain.ShopListRepository
import java.util.Random

class ShopListRepositoryImpl(application: Application) : ShopListRepository {
    private val shopListDao = AppDatabase.getInstance(application).shopListDao()

    override fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(ShopListMapper.mapEntityToDBModel(shopItem))
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.getShopItem(shopItem.id)
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(ShopListMapper.mapEntityToDBModel(shopItem))
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return ShopListMapper.mapDBModelToEntity(shopListDao.getShopItem(shopItemId))
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopListDao.getShopList()) {
                value = ShopListMapper.mapListDBModelToListEntity(it)
            }
        }
    }
}
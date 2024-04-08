package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem

class ShopListMapper {
    companion object {
        fun mapEntityToDBModel(shopItem: ShopItem): ShopItemDBModel {
            return ShopItemDBModel(
                shopItem.id,
                shopItem.name,
                shopItem.count,
                shopItem.enabled
            )
        }

        fun mapDBModelToEntity(shopItemDBModel: ShopItemDBModel): ShopItem {
            return ShopItem(
                shopItemDBModel.name,
                shopItemDBModel.count,
                shopItemDBModel.enabled,
                shopItemDBModel.id
            )
        }

        fun mapListDBModelToListEntity(list: List<ShopItemDBModel>): List<ShopItem> {
            return list.map {
                mapDBModelToEntity(it)
            }
        }
    }
}
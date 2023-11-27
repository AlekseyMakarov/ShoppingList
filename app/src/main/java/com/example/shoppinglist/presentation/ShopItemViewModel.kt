package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    private val _inputNameError = MutableLiveData<Boolean>()
    val inputNameError: LiveData<Boolean>
        get() = _inputNameError

    private val _inputCountError = MutableLiveData<Boolean>()
    val inputCountError: LiveData<Boolean>
        get() = _inputCountError

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseActivity = MutableLiveData<Unit>()
    val shouldCloseActivity: LiveData<Unit>
        get() = _shouldCloseActivity

    fun getShopItem(shopItemId: Int) {
        _shopItem.value = getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val valid = validateInput(name, count)
        if (valid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishActivity()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val valid = validateInput(name, count)
        shopItem.value?.let {
            if (valid) {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishActivity()
            }
        }
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        return when {
            name.isBlank() -> {
                _inputNameError.value = true
                false
            }
            count <= 0 -> {
                _inputCountError.value = true
                false
            }
            else -> true
        }
    }

    private fun finishActivity() {
        _shouldCloseActivity.value = Unit
    }

    fun resetInputCountError() {
        _inputCountError.value = false
    }

    fun resetInputNameError() {
        _inputNameError.value = false
    }
}
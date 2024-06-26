package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase: GetShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

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
        scope.launch {
            _shopItem.postValue(getShopItemUseCase.getShopItem(shopItemId))
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val valid = validateInput(name, count)
        if (valid) {
            val shopItem = ShopItem(name, count, true)
            scope.launch {
                addShopItemUseCase.addShopItem(shopItem)
                finishActivity()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val valid = validateInput(name, count)
        shopItem.value?.let {
            if (valid) {
                val item = it.copy(name = name, count = count)
                scope.launch {
                    editShopItemUseCase.editShopItem(item)
                    finishActivity()
                }
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
        var valid = true
        if (name.isBlank()) {
            _inputNameError.value = true
            valid = false
        }
        if (count <= 0) {
            _inputCountError.value = true
            valid = false
        }

        return valid
    }

    private fun finishActivity() {
        _shouldCloseActivity.postValue(Unit)
    }

    fun resetInputCountError() {
        _inputCountError.value = false
    }

    fun resetInputNameError() {
        _inputNameError.value = false
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
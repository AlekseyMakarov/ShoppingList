package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.ShoppingListApplication
import com.example.shoppinglist.domain.ShopItem
import javax.inject.Inject

class ShopItemActivity : AppCompatActivity(), OnCloseShopItemFragment {
    private lateinit var viewModel: ShopItemViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var mode: String
    private val component by lazy {
        (application as ShoppingListApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
        parseIntent()
        if (savedInstanceState == null) {
            launchFragment()
        }
    }

    companion object {
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_KEY = "mode"
        private const val SHOP_ITEM_KEY = "shop_item_id"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE_KEY, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE_KEY, MODE_EDIT)
            intent.putExtra(SHOP_ITEM_KEY, shopItemId)
            return intent
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(MODE_KEY))
            throw Exception("Mode in intent is absent")
        when (intent.getStringExtra(MODE_KEY)) {
            MODE_EDIT -> {
                mode = MODE_EDIT
                if (!intent.hasExtra(SHOP_ITEM_KEY))
                    throw Exception("Shop item id in intent is absent")
                shopItemId = intent.getIntExtra(SHOP_ITEM_KEY, ShopItem.UNDEFINED_ID)
                viewModel.getShopItem(shopItemId)
            }

            MODE_ADD -> {
                mode = MODE_ADD
            }

            else -> {
                throw Exception("Undefined mode")
            }
        }
    }

    private fun launchFragment() {
        val fragment = when (mode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEdit(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAdd()
            else -> throw Exception("Undefined mode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_fragment_container, fragment)
            .commit()
    }

    override fun onClose() {
        finish()
    }
}
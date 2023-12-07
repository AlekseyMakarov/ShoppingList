package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var saveButton: Button
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        parseIntent()
        launchFragment()

//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        initViews()
//        parseIntent()
//        setListeners()
//        setObservers()
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
//
//    private fun initViews() {
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        saveButton = findViewById(R.id.save_button)
//    }
//
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

    private fun launchFragment(){
        val fragment = when(mode){
            MODE_EDIT -> ShopItemFragment.newInstanceEdit(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAdd()
            else -> throw Exception("Undefined mode")
        }
        fragment.onClose = object : OnCloseShopItemFragment{
            override fun onClose() {
                finish()
            }
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
//
//    private fun setListeners() {
//        if (this::mode.isInitialized)
//            when (mode) {
//                MODE_ADD -> saveButton.setOnClickListener {
//                    viewModel.addShopItem(
//                        etName.text.toString(),
//                        etCount.text.toString()
//                    )
//                }
//                MODE_EDIT -> saveButton.setOnClickListener {
//                    viewModel.editShopItem(
//                        etName.text.toString(),
//                        etCount.text.toString()
//                    )
//                }
//            }
//        etName.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                viewModel.resetInputNameError()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })
//
//        etCount.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                viewModel.resetInputCountError()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })
//    }
//
//    private fun setObservers() {
//        viewModel.shopItem.observe(this) {
//            etCount.setText(it.count.toString())
//            etName.setText(it.name)
//        }
//        viewModel.inputNameError.observe(this) {
//            if (it)
//                tilName.error = getString(R.string.error_input_name)
//            else
//                tilName.error = null
//
//        }
//        viewModel.inputCountError.observe(this) {
//            if (it)
//                tilCount.error = getString(R.string.error_input_count)
//            else
//                tilCount.error = null
//        }
//        viewModel.shouldCloseActivity.observe(this) {
//            finish()
//        }
//    }
}
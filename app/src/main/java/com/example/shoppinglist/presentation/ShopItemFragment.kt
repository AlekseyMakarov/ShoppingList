package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var saveButton: Button
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var mode: String
    lateinit var onClose: OnCloseShopItemFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setListeners()
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    companion object {

        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_KEY = "mode"
        private const val SHOP_ITEM_KEY = "shop_item_id"


        @JvmStatic
        fun newInstanceAdd() =
            ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE_KEY, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit(shopItemId: Int) =
            ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE_KEY, MODE_EDIT)
                    putInt(SHOP_ITEM_KEY, shopItemId)
                }
            }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        saveButton = view.findViewById(R.id.save_button)
    }

    private fun parseArguments() {
        val arguments = requireArguments()
        if (!arguments.containsKey(MODE_KEY))
            throw Exception("Mode in intent is absent")
        when (arguments.getString(MODE_KEY)) {
            MODE_EDIT -> {
                mode = MODE_EDIT
                if (!arguments.containsKey(SHOP_ITEM_KEY))
                    throw Exception("Shop item id in intent is absent")
                shopItemId = arguments.getInt(SHOP_ITEM_KEY, ShopItem.UNDEFINED_ID)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        parseArguments()
    }

    private fun setListeners() {
        if (this::mode.isInitialized)
            when (mode) {
                MODE_ADD -> saveButton.setOnClickListener {
                    viewModel.addShopItem(
                        etName.text.toString(),
                        etCount.text.toString()
                    )
                }
                MODE_EDIT -> saveButton.setOnClickListener {
                    viewModel.editShopItem(
                        etName.text.toString(),
                        etCount.text.toString()
                    )
                }
            }
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputNameError()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputCountError()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setObservers() {
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etCount.setText(it.count.toString())
            etName.setText(it.name)
        }
        viewModel.inputNameError.observe(viewLifecycleOwner) {
            if (it)
                tilName.error = getString(R.string.error_input_name)
            else
                tilName.error = null

        }
        viewModel.inputCountError.observe(viewLifecycleOwner) {
            if (it)
                tilCount.error = getString(R.string.error_input_count)
            else
                tilCount.error = null
        }
        viewModel.shouldCloseActivity.observe(viewLifecycleOwner) {
            onClose.onClose()
        }
    }
}


interface OnCloseShopItemFragment {
    fun onClose()
}
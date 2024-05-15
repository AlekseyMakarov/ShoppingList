package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.ShoppingListApplication
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.domain.ShopItem
import javax.inject.Inject

class ShopItemFragment : Fragment() {
    private val component by lazy {
        (requireActivity().application as ShoppingListApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var mode: String
    private lateinit var onClose: OnCloseShopItemFragment
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding is null")
    private var _binding: FragmentShopItemBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setListeners()
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        component.inject(this)
        super.onAttach(context)
        parseArguments()
        if (context is OnCloseShopItemFragment) {
            onClose = context
        } else {
            throw RuntimeException("Activity $context does not implement OnCloseShopItemFragment")
        }
    }

    private fun setListeners() {
        if (this::mode.isInitialized)
            when (mode) {
                MODE_ADD -> binding.saveButton.setOnClickListener {
                    viewModel.addShopItem(
                        binding.etName.text.toString(),
                        binding.etCount.text.toString()
                    )
                }

                MODE_EDIT -> binding.saveButton.setOnClickListener {
                    viewModel.editShopItem(
                        binding.etName.text.toString(),
                        binding.etCount.text.toString()
                    )
                }
            }
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputNameError()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etCount.addTextChangedListener(object : TextWatcher {
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
        viewModel.shouldCloseActivity.observe(viewLifecycleOwner) {
            onClose.onClose()
        }
    }
}

interface OnCloseShopItemFragment {
    fun onClose()
}
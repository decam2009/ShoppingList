package `in`.kalosh.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import `in`.kalosh.shoppinglist.R
import `in`.kalosh.shoppinglist.domain.ShopItem

class ShopItemFragment() : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var btnSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    //Метод нужен для того что бы из макета создать view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    //Метод используется когда view уже создан
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        btnSave = view.findViewById(R.id.save_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        parseParams()
        super.onCreate(savedInstanceState)
    }

    private fun observeViewModel() {

        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilCount.error = message
        }
        viewModel.closeScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            EXTRA_ADD_MODE -> launchAddMode()
            EXTRA_EDIT_MODE -> launchEditMode()
        }
    }

    private fun addTextChangeListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputErrorName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputErrorCount()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        btnSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode() {
        btnSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode absent")
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != EXTRA_ADD_MODE && mode != EXTRA_EDIT_MODE) {
            throw RuntimeException("Unknown sxreen mode $mode")
        }
        screenMode = mode
        if (screenMode == EXTRA_EDIT_MODE) {
            if (!args.containsKey(EXTRA_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(EXTRA_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "item_mode"
        private const val EXTRA_EDIT_MODE = "edit_mode"
        private const val EXTRA_ADD_MODE = "add_mode"
        private const val EXTRA_ITEM_ID = "item_id"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                val args = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, EXTRA_ADD_MODE)
                }
                arguments = args
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {

            return ShopItemFragment().apply {
                val args = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, EXTRA_EDIT_MODE)
                    putInt(EXTRA_ITEM_ID, shopItemId)
                }
                arguments = args
            }
        }

        /*fun addShopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EXTRA_ADD_MODE)
            return intent
        }

        fun editShopItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EXTRA_EDIT_MODE)
            intent.putExtra(EXTRA_ITEM_ID, id)
            return intent
        }*/
    }

}

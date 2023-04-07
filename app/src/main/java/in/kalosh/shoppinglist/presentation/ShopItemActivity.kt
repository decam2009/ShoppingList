package `in`.kalosh.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import `in`.kalosh.shoppinglist.R
import `in`.kalosh.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState == null){//делаем проверку для того что бы при перевороте не добавлять фрагмент, система это сделает сама
            // при создании активити
        launchRightMode()}
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            EXTRA_ADD_MODE -> ShopItemFragment.newInstanceAddItem()
            EXTRA_EDIT_MODE -> ShopItemFragment.newInstanceEditItem(shopItemId)
            else -> {
                throw RuntimeException("Unknown screen mode $screenMode")
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.shop_item_container, fragment).commit()//replace заменяет фрагмент в
    // контейнере стеке фрагментов
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != EXTRA_EDIT_MODE && mode != EXTRA_ADD_MODE) {
            throw RuntimeException("Unknown screen mode")
        }
        screenMode = mode
        if (screenMode == EXTRA_EDIT_MODE) {
            if (!intent.hasExtra(EXTRA_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "item_mode"
        private const val EXTRA_EDIT_MODE = "edit_mode"
        private const val EXTRA_ADD_MODE = "add_mode"
        private const val EXTRA_ITEM_ID = "item_id"
        private const val MODE_UNKNOWN = ""

        fun addShopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EXTRA_ADD_MODE)
            return intent
        }

        fun editShopItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EXTRA_EDIT_MODE)
            intent.putExtra(EXTRA_ITEM_ID, id)
            return intent
        }
    }
}
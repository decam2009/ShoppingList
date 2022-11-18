package `in`.kalosh.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import `in`.kalosh.shoppinglist.R
import `in`.kalosh.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var linearLayoutShopItems: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutShopItems = findViewById(R.id.ll_shop_list)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            showList(it)
        }//Подписываемся на объект shopList
    }

    private fun showList(items: List<ShopItem>) {
        linearLayoutShopItems.removeAllViews()
        for (item in items) {
            val layoutId = if (item.enabled) {
                R.layout.item_shop_enabled
            } else {
                R.layout.item_shop_disabled
            }
            val view = LayoutInflater.from(this).inflate(layoutId, linearLayoutShopItems, false)
            val textViewName = view.findViewById<TextView>(R.id.tv_name)
            val textViewCount = view.findViewById<TextView>(R.id.tv_count)
            textViewName.text = item.name
            textViewCount.text = item.count.toString()
            view.setOnLongClickListener {
                viewModel.changeEnableState(item)
                true
            }
            linearLayoutShopItems.addView(view)
        }
    }
}
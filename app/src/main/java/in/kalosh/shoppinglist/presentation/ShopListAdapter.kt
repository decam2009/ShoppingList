package `in`.kalosh.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import `in`.kalosh.shoppinglist.R
import `in`.kalosh.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback())
/*RecyclerView
.Adapter<ShopListAdapter
.ShopItemViewHolder>() это убрали когда перешли к наследованию от ListAdapter*/ {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    /*var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback (shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback) // в этой переменной будут храниться все изменения,
            // которые необходимо сделать адаптеру. calculateDiff - занимается всеми сравнениями объектов.
            diffResult.dispatchUpdatesTo(this) //в параметр надо передать адаптер
            field = value
           // notifyDataSetChanged() //перерисовывает все данные в ресайклере, на реальных проектах не применяется, только в обучении.
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            DISABLED_VIEW -> R.layout.item_shop_disabled
            ENABLED_VIEW -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position) //shopList[position]
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    //Надо переопределять, есть в recycle view будут храниться элементы разных типов.
    override fun getItemViewType(position: Int): Int {
        val itemView = getItem(position)
        return when (itemView.enabled) {
            true -> ENABLED_VIEW
            false -> DISABLED_VIEW
        }
    }

    companion object {
        const val ENABLED_VIEW = 1
        const val DISABLED_VIEW = 0
        const val MAX_POOL_SIZE = 30
    }
}
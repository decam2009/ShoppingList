package `in`.kalosh.shoppinglist.data

import `in`.kalosh.shoppinglist.domain.ShopItem
import `in`.kalosh.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import java.util.*

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    init {
        for (i in 0 until 10) {
            val item = ShopItem("Name $i", i, true, UUID.randomUUID())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = shopItem.id?.let { getShopItemById(it) }
        shopList.remove(oldElement)
        shopList.add(shopItem)
    }

    override fun getShopItemById(id: UUID): ShopItem {
        return shopList.find { it.id == id } ?: throw RuntimeException("Элемент с id $id не найден")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }
}
package `in`.kalosh.shoppinglist.domain

import java.util.UUID

class GetShopItemByIdUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItemById(id: Int): ShopItem {
        return shopListRepository.getShopItemById(id)
    }
}
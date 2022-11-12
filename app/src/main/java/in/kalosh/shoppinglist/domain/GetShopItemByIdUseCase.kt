package `in`.kalosh.shoppinglist.domain

import java.util.UUID

class GetShopItemByIdUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItemById(id: UUID): ShopItem {
        return shopListRepository.getShopItemById(id)
    }
}
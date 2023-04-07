package `in`.kalosh.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import `in`.kalosh.shoppinglist.data.ShopListRepositoryImpl
import `in`.kalosh.shoppinglist.domain.DeleteShopItemUseCase
import `in`.kalosh.shoppinglist.domain.EditShopItemUseCase
import `in`.kalosh.shoppinglist.domain.GetShopListUseCase
import `in`.kalosh.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}
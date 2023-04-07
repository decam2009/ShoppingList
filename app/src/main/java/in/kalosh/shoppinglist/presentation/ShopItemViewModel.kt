package `in`.kalosh.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import `in`.kalosh.shoppinglist.data.ShopListRepositoryImpl
import `in`.kalosh.shoppinglist.domain.*
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val _errorInputName = MutableLiveData<Boolean>()
    private val _errorInputCount = MutableLiveData<Boolean>()
    private val _closeScreen = MutableLiveData<Unit>()

    val closeScreen: LiveData<Unit>
        get() = _closeScreen
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val getShopItemUseCase = GetShopItemByIdUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItemById(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }

    }

    private fun finishWork() {
        _closeScreen.value = Unit
    }

    private fun parseName(inputString: String?): String {
        return inputString?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    public fun resetInputErrorName() {
        _errorInputName.value = false
    }

    public fun resetInputErrorCount() {
        _errorInputCount.value = false
    }
}
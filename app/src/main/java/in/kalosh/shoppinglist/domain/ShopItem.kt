package `in`.kalosh.shoppinglist.domain

import java.util.UUID

data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    var id: UUID?
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
package com.adorastudios.funnykeyboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class FunnyKeyboardData(
    val numbersRow: List<FunnyKeyData> = listOf(
        FunnyKeyData.Symbol("ё", "ö"),
        FunnyKeyData.Symbol("1", "1"),
        FunnyKeyData.Symbol("2", "2"),
        FunnyKeyData.Symbol("3", "3"),
        FunnyKeyData.Symbol("4", "4"),
        FunnyKeyData.Symbol("5", "5"),
        FunnyKeyData.Symbol("6", "6"),
        FunnyKeyData.Symbol("7", "7"),
        FunnyKeyData.Symbol("8", "8"),
        FunnyKeyData.Symbol("9", "9"),
        FunnyKeyData.Symbol("0", "0"),
    ),
    val firstRow: List<FunnyKeyData> = listOf(
        FunnyKeyData.Symbol("й", "й"),
        FunnyKeyData.Symbol("ц", "ц"),
        FunnyKeyData.Symbol("у", "u"),
        FunnyKeyData.Symbol("к", "к"),
        FunnyKeyData.Symbol("е", "ë"),
        FunnyKeyData.Symbol("н", "н"),
        FunnyKeyData.Symbol("г", "г"),
        FunnyKeyData.Symbol("ш", "ш"),
        FunnyKeyData.Symbol("щ", "щ"),
        FunnyKeyData.Symbol("з", "з"),
        FunnyKeyData.Symbol("х", "х"),
    ),
    val secondRow: List<FunnyKeyData> = listOf(
        FunnyKeyData.Symbol("ф", "ф"),
        FunnyKeyData.Symbol("ы", "ı"),
        FunnyKeyData.Symbol("в", "в"),
        FunnyKeyData.Symbol("а", "a"),
        FunnyKeyData.Symbol("п", "п"),
        FunnyKeyData.Symbol("р", "р"),
        FunnyKeyData.Symbol("о", "о"),
        FunnyKeyData.Symbol("л", "л"),
        FunnyKeyData.Symbol("д", "д"),
        FunnyKeyData.Symbol("ж", "ж"),
        FunnyKeyData.Symbol("э", "e"),
    ),
    val thirdRow: List<FunnyKeyData> = listOf(
        FunnyKeyData.Action(Icons.Rounded.KeyboardArrowUp, ActionType.Up),
        FunnyKeyData.Symbol("я", "ä"),
        FunnyKeyData.Symbol("ч", "ч"),
        FunnyKeyData.Symbol("с", "с"),
        FunnyKeyData.Symbol("м", "м"),
        FunnyKeyData.Symbol("и", "ï"),
        FunnyKeyData.Symbol("т", "т"),
        FunnyKeyData.Symbol("ь", "'"),
        FunnyKeyData.Symbol("б", "б"),
        FunnyKeyData.Symbol("ю", "ü"),
        FunnyKeyData.Action(Icons.Rounded.KeyboardArrowLeft, ActionType.Delete),
    ),
    val up: Boolean = false,
)

sealed class FunnyKeyData {
    data class Symbol(
        val content: String,
        val actualValue: String,
    ) : FunnyKeyData()

    data class Action(
        val icon: ImageVector,
        val action: ActionType,
    ) : FunnyKeyData()
}

sealed class ActionType {
    data object Delete : ActionType()
    data object Up : ActionType()
}

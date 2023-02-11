package dev.itsu.interpreter.tokenizer

data class Token (
    val type: Int,
    val value: String
) {
    companion object {
        const val TYPE_INT_CONST = 0
        const val TYPE_PLUS_OPERATOR = 1
        const val TYPE_MINUS_OPERATOR = 2
        const val TYPE_TIMES_OPERATOR = 3
        const val TYPE_DIV_OPERATOR = 4
        const val TYPE_REM_OPERATOR = 5
        const val TYPE_OPEN_PAREN = 6
        const val TYPE_CLOSE_PAREN = 7
    }
}
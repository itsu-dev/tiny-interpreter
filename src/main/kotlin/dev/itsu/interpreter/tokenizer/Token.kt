package dev.itsu.interpreter.tokenizer

data class Token (
    val type: Int,
    val value: String
) {
    companion object {
        const val TYPE_INT_CONST = 0
        const val TYPE_FLOAT_CONST = 1
        const val TYPE_PLUS_OPERATOR = 2
        const val TYPE_MINUS_OPERATOR = 3
        const val TYPE_TIMES_OPERATOR = 4
        const val TYPE_DIV_OPERATOR = 5
        const val TYPE_REM_OPERATOR = 6
        const val TYPE_EQUAL_OPERATOR = 7
        const val TYPE_DOUBLE_EQUAL_OPERATOR = 8
        const val TYPE_LT_OPERATOR = 9
        const val TYPE_GT_OPERATOR = 10
        const val TYPE_OPEN_PAREN = 11
        const val TYPE_CLOSE_PAREN = 12
        const val TYPE_SYMBOL = 13
        const val TYPE_STRING = 14
        const val TYPE_OPEN_BRACKET = 15
        const val TYPE_CLOSE_BRACKET = 16
        const val TYPE_SEMI_COLON = 17
        const val TYPE_COMMA = 18
        const val TYPE_DOT = 19
        const val TYPE_OPEN_SQUARE_BRACKET = 20
        const val TYPE_CLOSE_SQUARE_BRACKET = 21

        const val TYPE_IDENTIFIER_WHILE = 22
        const val TYPE_IDENTIFIER_IF = 23
        const val TYPE_IDENTIFIER_VAR = 24
        const val TYPE_IDENTIFIER_FUNCTION = 25
        const val TYPE_IDENTIFIER_BREAK = 26
    }
}
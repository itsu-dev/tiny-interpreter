package dev.itsu.interpreter.tokenizer

class Tokenizer(private val line: String) {

    private var pos: Int = 0

    companion object {
        val IDENTIFIERS = mapOf(
            "while" to Token.TYPE_IDENTIFIER_WHILE,
            "if" to Token.TYPE_IDENTIFIER_IF,
            "var" to Token.TYPE_IDENTIFIER_VAR,
            "fun" to Token.TYPE_IDENTIFIER_FUNCTION,
            "break" to Token.TYPE_IDENTIFIER_BREAK
        )
        val OPERATORS = mapOf(
            '+' to Token.TYPE_PLUS_OPERATOR,
            '-' to Token.TYPE_MINUS_OPERATOR,
            '*' to Token.TYPE_TIMES_OPERATOR,
            '/' to Token.TYPE_DIV_OPERATOR,
            '%' to Token.TYPE_REM_OPERATOR,
            '<' to Token.TYPE_LT_OPERATOR,
            '>' to Token.TYPE_GT_OPERATOR
        )
    }

    // check if end of line
    private fun isEOL() = line.length == pos

    fun tokenize(): List<Token> {
        return tokenizeExpression()
    }

    private fun tokenizeExpression(): List<Token> {
        val result = mutableListOf<Token>()
        while (!isEOL()) {
            val c = line[pos]

            if (c.isWhitespace()) { // when ' '
                // Do nothing

            } else if (c.isDigit()) { // when integer or float constant
                var num = "$c"
                var pos2 = pos + 1
                var type = Token.TYPE_INT_CONST

                while (pos2 != line.length) {
                    if (line[pos2].isDot() && line[pos2 + 1].isDigit()) {
                        type = Token.TYPE_FLOAT_CONST
                    } else if (!line[pos2].isDigit()) {
                        break
                    }
                    num += line[pos2]
                    pos2++
                }

                result.add(Token(type, num))
                pos += pos2 - pos - 1

            } else if (c.isEqual()) { // when ==
                if (line[++pos].isEqual()) {
                    result.add(Token(Token.TYPE_DOUBLE_EQUAL_OPERATOR, "=="))
                } else {
                    result.add(Token(Token.TYPE_EQUAL_OPERATOR, "$c"))
                    pos--
                }

            } else if (c.isDoubleQuote()) { // when string constant
                var pos2 = pos + 1
                var str = ""
                while (pos2 != line.length && !line[pos2].isDoubleQuote()) {
                    str += line[pos2]
                    pos2++
                }
                result.add(Token(Token.TYPE_STRING, str))
                pos += pos2 - pos

            } else if (c.isOperator()) { // when operator
                result.add(Token(OPERATORS[c]!!, "$c"))

            } else if (c.isOpenParen()) { // when (
                result.add(Token(Token.TYPE_OPEN_PAREN, "("))

            } else if (c.isCloseParen()) { // when )
                result.add(Token(Token.TYPE_CLOSE_PAREN, ")"))

            } else if (c.isOpenBracket()) { // when {
                result.add(Token(Token.TYPE_OPEN_BRACKET, "{"))

            } else if (c.isCloseBracket()) { // when }
                result.add(Token(Token.TYPE_CLOSE_BRACKET, "}"))

            } else if (c.isOpenSquareBracket()) { // when [
                result.add(Token(Token.TYPE_OPEN_SQUARE_BRACKET, "["))

            } else if (c.isCloseSquareBracket()) { // when ]
                result.add(Token(Token.TYPE_CLOSE_SQUARE_BRACKET, "]"))

            } else if (c.isSemiColon()) { // when ;
                result.add(Token(Token.TYPE_SEMI_COLON, ";"))

            } else if (c.isComma()) { // when ,
                result.add(Token(Token.TYPE_COMMA, ","))

            } else if (c.isDot()) { // when .
                result.add(Token(Token.TYPE_DOT, "."))

            } else if (c.isLetter()) {
                var symbol = "$c"
                var pos2 = pos + 1
                while (pos2 != line.length && line[pos2].isLetter()) {
                    symbol += line[pos2]
                    pos2++
                }

                if (IDENTIFIERS.containsKey(symbol)) {
                    result.add(Token(IDENTIFIERS[symbol]!!, symbol))
                } else {
                    result.add(Token(Token.TYPE_SYMBOL, symbol))
                }

                pos += pos2 - pos - 1

            } else { // else
                throw IllegalArgumentException("Unknown token: $c")
            }

            pos++
        }

        return result
    }

    private fun Char.isOperator(): Boolean {
        return OPERATORS.containsKey(this)
    }

    private fun Char.isOpenParen(): Boolean {
        return this == '('
    }

    private fun Char.isCloseParen(): Boolean {
        return this == ')'
    }

    private fun Char.isDoubleQuote(): Boolean {
        return this == '"'
    }

    private fun Char.isOpenBracket(): Boolean {
        return this == '{'
    }

    private fun Char.isCloseBracket(): Boolean {
        return this == '}'
    }

    private fun Char.isOpenSquareBracket(): Boolean {
        return this == '['
    }

    private fun Char.isCloseSquareBracket(): Boolean {
        return this == ']'
    }

    private fun Char.isSemiColon(): Boolean {
        return this == ';'
    }

    private fun Char.isEqual(): Boolean {
        return this == '='
    }

    private fun Char.isComma(): Boolean {
        return this == ','
    }

    private fun Char.isDot(): Boolean {
        return this == '.'
    }
}
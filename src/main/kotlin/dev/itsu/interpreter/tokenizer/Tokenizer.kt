package dev.itsu.interpreter.tokenizer

class Tokenizer(private val line: String) {

    private var pos: Int = 0

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

            } else if (c.isDigit()) { // when integer
                var num = "$c"
                var pos2 = pos + 1
                while (pos2 != line.length && line[pos2].isDigit()) {
                    num += line[pos2]
                    pos2++
                }
                result.add(Token(Token.TYPE_INT_CONST, num))
                pos += pos2 - pos - 1

            } else if (c.isOperator()) { // when +-*/%
                when (c) {
                    '+' -> result.add(Token(Token.TYPE_PLUS_OPERATOR, "+"))
                    '-' -> result.add(Token(Token.TYPE_MINUS_OPERATOR, "-"))
                    '*' -> result.add(Token(Token.TYPE_TIMES_OPERATOR, "*"))
                    '/' -> result.add(Token(Token.TYPE_DIV_OPERATOR, "/"))
                    '%' -> result.add(Token(Token.TYPE_DIV_OPERATOR, "%"))
                }

            } else if (c.isOpenParen()) { // when (
                result.add(Token(Token.TYPE_OPEN_PAREN, "("))

            } else if (c.isCloseParen()) { // when )
                result.add(Token(Token.TYPE_CLOSE_PAREN, ")"))
            }

            pos++
        }

        return result
    }

    private fun Char.isOperator(): Boolean {
        return listOf('+', '-', '*', '/', '%').contains(this)
    }

    private fun Char.isOpenParen(): Boolean {
        return this == '('
    }

    private fun Char.isCloseParen(): Boolean {
        return this == ')'
    }

}
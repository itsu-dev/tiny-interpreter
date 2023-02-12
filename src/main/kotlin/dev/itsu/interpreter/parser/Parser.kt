package dev.itsu.interpreter.parser

import dev.itsu.interpreter.exception.SyntaxError
import dev.itsu.interpreter.expr.*
import dev.itsu.interpreter.expr.FloatExpr
import dev.itsu.interpreter.stmt.*
import dev.itsu.interpreter.tokenizer.Token
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Parser(private val tokens: List<Token>, start: Int = 0) {

    private var pos = start
    private var typeCache: Int? = null
    private var exprCache: Expr? = null
    private val statementsCache = mutableListOf<Statement>()

    companion object {
        private val PRIORITIES = mapOf(
            Token.TYPE_INT_CONST to 0,
            Token.TYPE_FLOAT_CONST to 0,
            Token.TYPE_SYMBOL to 0,
            Token.TYPE_TIMES_OPERATOR to 1,
            Token.TYPE_DIV_OPERATOR to 1,
            Token.TYPE_REM_OPERATOR to 1,
            Token.TYPE_PLUS_OPERATOR to 2,
            Token.TYPE_MINUS_OPERATOR to 2,
            Token.TYPE_LT_OPERATOR to 3,
            Token.TYPE_GT_OPERATOR to 3,
            Token.TYPE_DOUBLE_EQUAL_OPERATOR to 4,
            Token.TYPE_OPEN_PAREN to 5,
            Token.TYPE_CLOSE_PAREN to 5
        )

        val OPERATORS = mapOf(
            "+" to Token.TYPE_PLUS_OPERATOR,
            "-" to Token.TYPE_MINUS_OPERATOR,
            "*" to Token.TYPE_TIMES_OPERATOR,
            "/" to Token.TYPE_DIV_OPERATOR,
            "%" to Token.TYPE_REM_OPERATOR,
            "<" to Token.TYPE_LT_OPERATOR,
            ">" to Token.TYPE_GT_OPERATOR,
            "==" to Token.TYPE_DOUBLE_EQUAL_OPERATOR
        )
    }

    private fun isCloseBracket(): Boolean {
        return tokens[pos].type == Token.TYPE_CLOSE_BRACKET
    }

    private fun isEOT(): Boolean {
        return pos > tokens.size - 1
    }

    fun parse(): Pair<CompoundStatement, Int> {
        val statements = mutableListOf<Statement>()

        while (!isEOT() && !isCloseBracket()) {
            statements.add(parseStatement())
        }

        return CompoundStatement(statements) to pos
    }

    private fun parseStatement(): Statement {
        while (!isSemiColon()) {
            when (tokens[pos].type) {
                Token.TYPE_IDENTIFIER_VAR -> {
                    if (tokens[++pos].type != Token.TYPE_SYMBOL) {
                        throw SyntaxError("Syntax error around \"var\".")
                    }

                    val varName = tokens[pos].value

                    if (tokens[++pos].type == Token.TYPE_EQUAL_OPERATOR) {
                        pos++
                        val expr = parseExpr()
                        pos++ // pass ;
                        return VarStatement(varName, expr)

                    } else if (isSemiColon()) {
                        pos++ // pass ;
                        return VarStatement(varName, null)

                    } else {
                        throw SyntaxError("Syntax error around \"var\" $varName ${tokens[pos].value}.")
                    }

                }

                Token.TYPE_IDENTIFIER_WHILE -> {
                    if (tokens[++pos].type != Token.TYPE_OPEN_PAREN) {
                        throw SyntaxError("Syntax error around \"while\".")
                    }

                    pos++ // pass (
                    exprCache = parseExpr(pos)
                    typeCache = Token.TYPE_IDENTIFIER_WHILE

                    pos++ // pass )

                    if (tokens[pos].type != Token.TYPE_OPEN_BRACKET) {
                        throw SyntaxError("Syntax error around \"while\".")
                    }
                }

                Token.TYPE_IDENTIFIER_IF -> {
                    if (tokens[++pos].type != Token.TYPE_OPEN_PAREN) {
                        throw SyntaxError("Syntax error around \"if\".")
                    }

                    pos++ // pass (
                    exprCache = parseExpr(pos)
                    typeCache = Token.TYPE_IDENTIFIER_IF

                    pos++ // pass )

                    if (tokens[pos].type != Token.TYPE_OPEN_BRACKET) {
                        throw SyntaxError("Syntax error around \"if\".")
                    }
                }

                Token.TYPE_STRING -> {
                    val str = tokens[pos].value
                    pos++ // pass str
                    pos++ // pass ;
                    return TestStatement(StringExpr(str))
                }

                Token.TYPE_IDENTIFIER_BREAK -> {
                    val symbol = tokens[pos].value
                    pos++ // pasa symbol
                    pos++ // pass ;
                    return SymbolStatement(SymbolExpr(symbol))
                }

                Token.TYPE_IDENTIFIER_FUNCTION -> {
                    if (tokens[++pos].type != Token.TYPE_SYMBOL) {
                        throw SyntaxError("Syntax error around \"function\".")
                    }

                    var funcName = tokens[pos]

                    if (tokens[++pos].type != Token.TYPE_SYMBOL) {
                        throw SyntaxError("Syntax error around \"function\".")
                    }

                    // TODO
                }

                Token.TYPE_SYMBOL -> {
                    val varName = tokens[pos].value

                    if (tokens[++pos].type != Token.TYPE_EQUAL_OPERATOR) {
                        throw SyntaxError("Syntax error around \"$varName\".")
                    }

                    pos++

                    val expr = parseExpr()
                    pos++ // pass ;
                    return LetStatement(varName, expr)
                }

                Token.TYPE_OPEN_BRACKET -> {
                    pos++ // pass {
                    statementsCache.clear()

                    val (statement, p) = Parser(tokens, pos).parse()
                    pos = p

                    return when (typeCache) {
                        Token.TYPE_IDENTIFIER_WHILE -> {
                            pos++ // pass }
                            WhileStatement(exprCache!!, statement)
                        }
                        Token.TYPE_IDENTIFIER_IF -> {
                            pos++ // pass }
                            IfStatement(exprCache!!, statement)
                        }
                        else -> {
                            pos++
                            statement
                        }
                    }
                }

                else -> {
                    println(tokens)
                    throw SyntaxError("Unknown symbol: $pos ${tokens[pos].value}")
                }
            }
        }

        throw SyntaxError("Unknown symbol: $pos ${tokens[pos].value}")
    }

    /**
    @see https://nodamushi.hatenablog.com/entry/20090625/1245902959
     */
    fun parseExpr(dest: Int? = null): Expr {
        // infix notation to reversed-polish notation
        val queue = ConcurrentLinkedQueue<Token>()
        val stack = Stack<Token>()
        var token: Token

        while (!isSemiColon()) {
            token = tokens[pos]

            if (dest != null && token.type == Token.TYPE_CLOSE_PAREN) {
                if (isParenClosed(dest)) break
            }

            if (stack.size == 0) {
                stack.push(token)

            } else {
                if (token.type == Token.TYPE_OPEN_PAREN) {
                    stack.push(token)

                } else if (token.type == Token.TYPE_CLOSE_PAREN) {
                    while (stack.peek().type != Token.TYPE_OPEN_PAREN) {
                        queue.add(stack.pop())
                    }
                    stack.pop() // remove ( from the stack

                } else {
                    while (stack.size > 0 && PRIORITIES[stack.peek().type]!! < PRIORITIES[token.type]!!) {
                        queue.add(stack.pop())
                    }
                    stack.push(token)
                }
            }

            pos++
        }

        while (stack.size != 0) {
            queue.add(stack.pop())
        }


        /* Print reversed-polish notation
        queue.forEach { token ->
            print(token.value + " ")
        }
        println()
         */

        // reversed-polish notation to expr
        val operandStack = Stack<Expr>()
        queue.forEach { token ->
            if (token.type == Token.TYPE_INT_CONST) {
                operandStack.push(IntExpr(token.value.toInt()))

            } else if (token.type == Token.TYPE_FLOAT_CONST) {
                operandStack.push(FloatExpr(token.value.toFloat()))

            } else if (token.type == Token.TYPE_SYMBOL) {
                operandStack.push(Variable(token.value))

            } else if (OPERATORS.containsKey(token.value)) {
                val rhs = operandStack.pop()
                val lhs = operandStack.pop()
                operandStack.push(BinExpr(token.value, lhs, rhs))
            }
        }

        return operandStack.pop()
    }

    private fun isSemiColon(): Boolean {
        return tokens[pos].type == Token.TYPE_SEMI_COLON
    }

    private fun isParenClosed(dest: Int): Boolean {
        var openCount = 0
        var closeCount = 0
        var token: Token

        for (i in pos downTo dest - 1) {
            token = tokens[i]
            if (token.type == Token.TYPE_OPEN_PAREN) {
                openCount++
            } else if (token.type == Token.TYPE_CLOSE_PAREN) {
                closeCount++
            }

            if (openCount != 0 && closeCount != 0 && openCount == closeCount) return true
        }

        return false
    }

}
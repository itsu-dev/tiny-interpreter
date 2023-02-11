package dev.itsu.interpreter.parser

import dev.itsu.interpreter.expr.BinExpr
import dev.itsu.interpreter.expr.Expr
import dev.itsu.interpreter.expr.Integer
import dev.itsu.interpreter.tokenizer.Token
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Parser(private val tokens: List<Token>) {

    companion object {
        private val PRIORITIES = mapOf(
            Token.TYPE_INT_CONST to 0,
            Token.TYPE_TIMES_OPERATOR to 1,
            Token.TYPE_DIV_OPERATOR to 1,
            Token.TYPE_REM_OPERATOR to 1,
            Token.TYPE_PLUS_OPERATOR to 2,
            Token.TYPE_MINUS_OPERATOR to 2,
            Token.TYPE_OPEN_PAREN to 3,
            Token.TYPE_CLOSE_PAREN to 3
        )
    }

    /**
    @see https://nodamushi.hatenablog.com/entry/20090625/1245902959
     */
    fun parseExpression(): Expr {
        // infix notation to reversed-polish notation
        val queue = ConcurrentLinkedQueue<Token>()
        val stack = Stack<Token>()

        tokens.forEach { token ->
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
        }

        while (stack.size != 0) {
            queue.add(stack.pop())
        }

        // reversed-polish notation to expr
        val operandStack = Stack<Expr>()
        queue.forEach { token ->
            if (token.type == Token.TYPE_INT_CONST) {
                operandStack.push(Integer(token.value.toInt()))

            } else if (
                token.type == Token.TYPE_PLUS_OPERATOR
                || token.type == Token.TYPE_MINUS_OPERATOR
                || token.type == Token.TYPE_TIMES_OPERATOR
                || token.type == Token.TYPE_DIV_OPERATOR
                || token.type == Token.TYPE_REM_OPERATOR
            ) {
                val rhs = operandStack.pop()
                val lhs = operandStack.pop()
                operandStack.push(BinExpr(token.value, lhs, rhs))
            }
        }

        return operandStack.pop()
    }

}
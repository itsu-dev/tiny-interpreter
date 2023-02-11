package dev.itsu.interpreter.evaluater

import dev.itsu.interpreter.expr.BinExpr
import dev.itsu.interpreter.expr.Expr
import dev.itsu.interpreter.expr.Integer

object Evaluator {

    fun evaluate(expr: Expr): Int {
        if (expr is BinExpr) {
            val op = expr.op
            return when (op) {
                "+" -> evaluate(expr.lhs) + evaluate(expr.rhs)
                "-" -> evaluate(expr.lhs) - evaluate(expr.rhs)
                "*" -> evaluate(expr.lhs) * evaluate(expr.rhs)
                "/" -> evaluate(expr.lhs) / evaluate(expr.rhs)
                else ->
                    throw IllegalArgumentException()
            }
        } else if (expr is Integer) {
            return expr.value
        }

        throw IllegalArgumentException()
    }

}
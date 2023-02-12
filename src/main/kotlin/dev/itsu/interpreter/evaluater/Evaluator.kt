package dev.itsu.interpreter.evaluater

import dev.itsu.interpreter.exception.runtime.VariableAlreadyDeclaredException
import dev.itsu.interpreter.expr.*
import dev.itsu.interpreter.stmt.*

class Evaluator {

    private val variables = mutableMapOf<String, Any?>()
    
    fun eval(statement: Statement) {
        if (statement is IfStatement) {
            if (evalExpr(statement.expr) == 1) {
                eval(statement.compoundStatement)
            }

        } else if (statement is WhileStatement) {
            while (evalExpr(statement.expr) == 1) {
                eval(statement.compoundStatement)
            }

        } else if (statement is VarStatement) {
            if (variables.containsKey(statement.varName)) {
                throw VariableAlreadyDeclaredException(statement.varName)
            }
            variables[statement.varName] = if (statement.expr == null) null else evalExpr(statement.expr)

        } else if (statement is LetStatement) {
            variables[statement.varName] = evalExpr(statement.expr)

        } else if (statement is CompoundStatement) {
            statement.statements.forEach {
                eval(it)
            }

        } else if (statement is SymbolStatement) {
            evalExpr(statement.expr)

        } else if (statement is TestStatement) {
            evalExpr(statement.expr)

        }
    }

    fun evalExpr(expr: Expr): Any? {
        if (expr is BinExpr) {
            val op = expr.op
            val l = evalExpr(expr.lhs)
            val r = evalExpr(expr.rhs)

            return when {
                l is Int && r is Int -> {
                    when (op) {
                        "+" -> l + r
                        "-" -> l - r
                        "*" -> l * r
                        "/" -> l / r
                        "%" -> l % r
                        "<" -> if (l < r) 1 else 0
                        ">" -> if (l > r) 1 else 0
                        "==" -> if (l == r) 1 else 0
                        else ->
                            throw IllegalArgumentException()
                    }
                }

                l is Int && r is Float -> {
                    when (op) {
                        "+" -> l + r
                        "-" -> l - r
                        "*" -> l * r
                        "/" -> l / r
                        "%" -> l % r
                        "<" -> if (l < r) 1 else 0
                        ">" -> if (l > r) 1 else 0
                        "==" -> if (l == r) 1 else 0
                        else ->
                            throw IllegalArgumentException()
                    }
                }

                l is Float && r is Int -> {
                    when (op) {
                        "+" -> l + r
                        "-" -> l - r
                        "*" -> l * r
                        "/" -> l / r
                        "%" -> l % r
                        "<" -> if (l < r) 1 else 0
                        ">" -> if (l > r) 1 else 0
                        "==" -> if (l == r) 1 else 0
                        else ->
                            throw IllegalArgumentException()
                    }
                }

                l is Float && r is Float -> {
                    when (op) {
                        "+" -> l + r
                        "-" -> l - r
                        "*" -> l * r
                        "/" -> l / r
                        "%" -> l % r
                        "<" -> if (l < r) 1 else 0
                        ">" -> if (l > r) 1 else 0
                        "==" -> if (l == r) 1 else 0
                        else ->
                            throw IllegalArgumentException()
                    }
                }

                else -> throw IllegalArgumentException()
            }

        } else if (expr is IntExpr) {
            return expr.value

        } else if (expr is FloatExpr) {
            return expr.value

        } else if (expr is StringExpr) {
            println(expr.value)
            return expr.value

        } else if (expr is Variable) {
            return variables[expr.varName]

        }

        throw IllegalArgumentException()
    }

}
package dev.itsu.interpreter.stmt

import dev.itsu.interpreter.expr.Expr

data class LetStatement(val varName: String, val expr: Expr) : Statement("Let")
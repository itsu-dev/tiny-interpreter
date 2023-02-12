package dev.itsu.interpreter.stmt

import dev.itsu.interpreter.expr.Expr

data class VarStatement(val varName: String, val expr: Expr?) : Statement("Var")
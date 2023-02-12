package dev.itsu.interpreter.stmt

import dev.itsu.interpreter.expr.Expr

data class TestStatement(val expr: Expr) : Statement("Test")
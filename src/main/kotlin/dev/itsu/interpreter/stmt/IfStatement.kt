package dev.itsu.interpreter.stmt

import dev.itsu.interpreter.expr.Expr

data class IfStatement(val expr: Expr, val compoundStatement: CompoundStatement) : Statement("If")
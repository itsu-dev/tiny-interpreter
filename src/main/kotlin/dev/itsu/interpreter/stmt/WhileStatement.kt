package dev.itsu.interpreter.stmt

import dev.itsu.interpreter.expr.Expr

data class WhileStatement(val expr: Expr, val compoundStatement: CompoundStatement) : Statement("While")
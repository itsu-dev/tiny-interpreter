package dev.itsu.interpreter.stmt

import dev.itsu.interpreter.expr.SymbolExpr

data class SymbolStatement(val expr: SymbolExpr) : Statement("Symbol")
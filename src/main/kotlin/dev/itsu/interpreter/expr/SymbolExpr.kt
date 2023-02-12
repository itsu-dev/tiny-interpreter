package dev.itsu.interpreter.expr

data class SymbolExpr(val value: String) : Expr("Symbol")
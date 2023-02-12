package dev.itsu.interpreter.expr

data class StringExpr(val value: String) : Expr("String")
package dev.itsu.interpreter.expr

data class BinExpr(
    val op: String,
    val lhs: Expr,
    val rhs: Expr
    ) : Expr("BinExpr")
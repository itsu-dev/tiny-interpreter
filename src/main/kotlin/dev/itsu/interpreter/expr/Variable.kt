package dev.itsu.interpreter.expr

import kotlin.Float

data class Variable (val varName: String) : Expr("Variable")
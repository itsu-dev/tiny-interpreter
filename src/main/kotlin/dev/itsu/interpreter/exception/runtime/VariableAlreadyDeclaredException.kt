package dev.itsu.interpreter.exception.runtime

class VariableAlreadyDeclaredException(varName: String) : RuntimeException("Variable $varName is already declared.")
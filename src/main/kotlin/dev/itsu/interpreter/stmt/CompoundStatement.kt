package dev.itsu.interpreter.stmt

data class CompoundStatement(val statements: List<Statement>) : Statement("Compound")
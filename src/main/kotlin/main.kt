import dev.itsu.interpreter.evaluater.Evaluator
import dev.itsu.interpreter.parser.Parser
import dev.itsu.interpreter.tokenizer.Tokenizer
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    println("Input expressions (input nothing will exit the program):")
    print("> ")
    var line = scanner.nextLine()

    while (line.isNotEmpty()) {
        val tokens = Tokenizer(line).tokenize()
        val expr = Parser(tokens).parseExpression()
        println(Evaluator.evaluate(expr))
        print("> ")
        line = scanner.nextLine()
    }
}
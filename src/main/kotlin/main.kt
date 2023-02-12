import dev.itsu.interpreter.evaluater.Evaluator
import dev.itsu.interpreter.parser.Parser
import dev.itsu.interpreter.stmt.WhileStatement
import dev.itsu.interpreter.tokenizer.Tokenizer
import java.nio.charset.StandardCharsets
import java.util.*

fun main(args: Array<String>) {
    // Main().loadTest()
    Main().interactiveCalculator()
}

class Main {
    fun loadTest() {
        Main::class.java.classLoader.getResourceAsStream("Main.cr").bufferedReader(StandardCharsets.UTF_8).use {
            val tokens = Tokenizer(it.readText()).tokenize()
            val parsed = Parser(tokens).parse()
            Evaluator().eval(parsed.first)
        }
    }

    fun interactiveTokenizer() {
        val scanner = Scanner(System.`in`)

        println("Input code(input nothing will exit the program):")
        print("> ")
        var line = scanner.nextLine()

        while (line.isNotEmpty()) {
            val tokens = Tokenizer(line).tokenize()
            println(tokens)
            print("> ")
            line = scanner.nextLine()
        }
    }

    fun interactiveCalculator() {
        val scanner = Scanner(System.`in`)

        println("Input expressions (input nothing will exit the program):")
        print("> ")
        var line = scanner.nextLine()

        while (line.isNotEmpty()) {
            val tokens = Tokenizer(line + ";").tokenize()
            val expr = Parser(tokens).parseExpr()
            println(Evaluator().evalExpr(expr))
            print("> ")
            line = scanner.nextLine()
        }
    }
}
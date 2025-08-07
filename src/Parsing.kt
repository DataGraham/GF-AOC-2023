interface Parser<T> {
    fun parse(line: String): T
}

fun <T> Parser<T>.parseLines(input: List<String>) = input.map { line -> parse(line) }

// TODO: Use this generalized version elsewhere?
class UniversalParser<T>(private vararg val parsers: Parser<out T>) : Parser<T> {
    override fun parse(line: String) = parsers.firstNotNullOf { parser ->
        try {
            parser.parse(line)
        } catch (e: Exception) {
            null
        }
    }
}

// TODO: Use this generalized version elsewhere?
class RegexParser<T>(
    private val pattern: String,
    private val processCaptures: (List<String>) -> T
) : Parser<T> {
    private val regex by lazy { Regex(pattern) }

    override fun parse(line: String) = processCaptures(
        regex.matchEntire(line)!!.groupValues.drop(1)
    )
}

fun intParser(pattern: String) =
    RegexParser(pattern) { captures -> captures.first().toInt() }

class FixedStringParser<T>(private val fixedString: String, private val result: T): Parser<T> {
    override fun parse(line: String) = result.takeIf { line == fixedString }!!
}

fun Collection<String>.findAll(regex: Regex) = flatMap { line ->
    regex.findAll(line)
        .map { matchResult -> matchResult.groupValues.first() }
        .toList()
}


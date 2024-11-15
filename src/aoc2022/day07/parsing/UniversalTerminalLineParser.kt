package aoc2022.day07.parsing

import aoc2022.day07.parsing.impl.*

class UniversalTerminalLineParser : TerminalLineParser {
    companion object {
        fun parseTerminalLines(input: List<String>) =
            with(UniversalTerminalLineParser()) {
                input.map { line -> parse(line) }
            }
    }

    private val parsers by lazy {
        listOf(
            ChangeDirectoryParser(),
            ListCommandParser(),
            FileListingParser(),
            DirectoryListingParser()
        )
    }

    override fun parse(line: String) = parsers.firstNotNullOf { parser -> parser.parse(line) }
}
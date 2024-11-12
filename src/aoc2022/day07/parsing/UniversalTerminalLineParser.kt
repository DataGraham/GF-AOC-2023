package aoc2022.day07.parsing

import aoc2022.day07.parsing.impl.*

class UniversalTerminalLineParser : TerminalLineParser {
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
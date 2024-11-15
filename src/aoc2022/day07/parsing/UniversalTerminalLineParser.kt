package aoc2022.day07.parsing

import aoc2022.day07.parsing.impl.ChangeDirectoryParser
import aoc2022.day07.parsing.impl.DirectoryListingParser
import aoc2022.day07.parsing.impl.FileListingParser
import aoc2022.day07.parsing.impl.ListCommandParser

class UniversalTerminalLineParser : TerminalLineParser {
    companion object {
        fun List<String>.parseTerminalLines() =
            with(UniversalTerminalLineParser()) {
                map { line -> parse(line) }
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
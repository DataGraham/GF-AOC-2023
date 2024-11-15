package aoc2022.day07.parsing

import aoc2022.day07.parsing.impl.changeDirectoryParser
import aoc2022.day07.parsing.impl.directoryListingParser
import aoc2022.day07.parsing.impl.fileListingParser
import aoc2022.day07.parsing.impl.listCommandParser

class UniversalTerminalLineParser : TerminalLineParser {
    companion object {
        fun List<String>.parseTerminalLines() =
            with(UniversalTerminalLineParser()) {
                map { line -> parse(line) }
            }
    }

    private val parsers by lazy {
        listOf(
            changeDirectoryParser(),
            listCommandParser(),
            fileListingParser(),
            directoryListingParser()
        )
    }

    override fun parse(line: String) = parsers.firstNotNullOf { parser -> parser.parse(line) }
}
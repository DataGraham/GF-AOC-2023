package aoc2022.day07.parsing

import Parser
import UniversalParser
import aoc2022.day07.TerminalLine
import aoc2022.day07.parsing.impl.changeDirectoryParser
import aoc2022.day07.parsing.impl.directoryListingParser
import aoc2022.day07.parsing.impl.fileListingParser
import aoc2022.day07.parsing.impl.listCommandParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UniversalTerminalLineParser : Parser<TerminalLine> by UniversalParser(
    changeDirectoryParser(),
    listCommandParser(),
    fileListingParser(),
    directoryListingParser()
) {
    companion object {
        fun Flow<String>.parseTerminalLines() =
            with(UniversalTerminalLineParser()) {
                map { line -> parse(line) }
            }
    }
}

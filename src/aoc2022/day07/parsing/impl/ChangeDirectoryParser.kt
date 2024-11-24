package aoc2022.day07.parsing.impl

import RegexParser
import aoc2022.day07.TerminalLine
import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand

fun changeDirectoryParser() =
    RegexParser<TerminalLine>(pattern = """\$ cd (.+)""") { captures ->
        ChangeDirectoryCommand(directoryName = captures.first())
    }

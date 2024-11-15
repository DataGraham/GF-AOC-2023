package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand
import aoc2022.day07.parsing.TerminalLineParser

class ChangeDirectoryParser : TerminalLineParser by RegexTerminalLineParser.withPattern(
    """\$ cd (.+)""",
    { captures -> ChangeDirectoryCommand(directoryName = captures.first()) }
)

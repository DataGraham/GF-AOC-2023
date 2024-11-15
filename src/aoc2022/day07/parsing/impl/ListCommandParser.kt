package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Command.ListCommand
import aoc2022.day07.parsing.TerminalLineParser

class ListCommandParser : TerminalLineParser by RegexTerminalLineParser.withPattern(
    """\$ ls""",
    { ListCommand }
)

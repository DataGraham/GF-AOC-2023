package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand

fun changeDirectoryParser() =
    RegexTerminalLineParser(pattern = """\$ cd (.+)""") { captures ->
        ChangeDirectoryCommand(directoryName = captures.first())
    }


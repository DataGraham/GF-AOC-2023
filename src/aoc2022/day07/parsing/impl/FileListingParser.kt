package aoc2022.day07.parsing.impl

import RegexParser
import aoc2022.day07.TerminalLine
import aoc2022.day07.TerminalLine.Listing.FileListing

fun fileListingParser() =
    RegexParser<TerminalLine>(pattern = """(\d+) (.+)""") { captures ->
        FileListing(fileSize = captures[0].toInt(), fileName = captures[1])
    }

package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Listing.FileListing

fun fileListingParser() =
    RegexTerminalLineParser(pattern = """(\d+) (.+)""") { captures ->
        FileListing(fileSize = captures[0].toInt(), fileName = captures[1])
    }

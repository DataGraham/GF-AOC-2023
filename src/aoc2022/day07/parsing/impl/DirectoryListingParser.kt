package aoc2022.day07.parsing.impl

import RegexParser
import aoc2022.day07.TerminalLine
import aoc2022.day07.TerminalLine.Listing.DirectoryListing

fun directoryListingParser() =
    RegexParser<TerminalLine>(pattern = """dir (.+)""") { captures ->
        DirectoryListing(directoryName = captures.first())
    }
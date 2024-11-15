package aoc2022.day07.parsing.impl

import aoc2022.day07.TerminalLine.Listing.DirectoryListing

fun directoryListingParser() =
    RegexTerminalLineParser(pattern = """dir (.+)""") { captures ->
        DirectoryListing(directoryName = captures.first())
    }
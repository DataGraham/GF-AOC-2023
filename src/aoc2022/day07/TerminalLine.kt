package aoc2022.day07

sealed class TerminalLine {
    sealed class Command : TerminalLine() {
        data class ChangeDirectoryCommand(val directoryName: String) : Command()
        data object ListCommand : Command()
    }

    sealed class Listing : TerminalLine() {
        data class FileListing(val name: String, val size: Int) : Listing()
        data class DirectoryListing(val name: String) : Listing()
    }
}
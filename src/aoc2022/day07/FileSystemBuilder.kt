package aoc2022.day07

import aoc2022.day07.FileSystemItem.DirectoryItem
import aoc2022.day07.FileSystemItem.FileItem
import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand
import aoc2022.day07.TerminalLine.Command.ListCommand
import aoc2022.day07.TerminalLine.Listing
import aoc2022.day07.TerminalLine.Listing.FileListing

class FileSystemBuilder private constructor() {
    companion object {
        private const val ROOT_DIRECTORY_NAME = "/"
        private const val PARENT_DIRECTORY_INDICATOR = ".."

        fun List<TerminalLine>.buildFileSystem() =
            FileSystemBuilder().buildFileSystem(terminalLines = this)
    }

    private val rootDirectory = DirectoryItem(ROOT_DIRECTORY_NAME, parent = null)
    private var currentDirectory: DirectoryItem? = null

    private fun buildFileSystem(terminalLines: List<TerminalLine>): DirectoryItem {
        terminalLines.forEach { terminalLine ->
            when (terminalLine) {
                is ChangeDirectoryCommand -> changeCurrentDirectory(directoryName = terminalLine.directoryName)
                ListCommand -> {} // Nothing to do really
                is Listing -> addChild(terminalLine.toFileSystemItem())
            }
        }
        return rootDirectory
    }

    private fun changeCurrentDirectory(directoryName: String) {
        currentDirectory = when (directoryName) {
            rootDirectory.name -> rootDirectory
            PARENT_DIRECTORY_INDICATOR -> currentDirectory!!.parent
            else -> currentDirectory!!.findChild(name = directoryName) as DirectoryItem
        }
    }

    private fun addChild(child: FileSystemItem) {
        currentDirectory!!.children += child
    }

    private fun Listing.toFileSystemItem() = when (this@toFileSystemItem) {
        is FileListing -> FileItem(name = fileName, sizeInBytes = fileSize)
        is Listing.DirectoryListing -> DirectoryItem(name = directoryName, parent = currentDirectory)
    }
}
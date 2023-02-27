package nl.runnable.gigmatch.framework.commands

abstract class CommandException(message: String) : RuntimeException(message)

class CommandDeserializationException(message: String) : CommandException(message)

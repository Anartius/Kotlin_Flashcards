package flashcards

import java.io.File
import kotlin.random.Random
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.system.exitProcess

val log = mutableListOf<String>()

fun main(args: Array<String>) {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(
        MutableMap::class.java,
        String::class.java,
        Map::class.java,
        String::class.java,
        Integer::class.java
    )
    val flashCardAdapter = moshi.adapter<MutableMap<String, Map<String, Int>>>(type)
    var flashCards = mutableMapOf<String, Map<String, Int>>()
    var file: File

    val argsIOList = getIOFromArgs(args.toList())
    if (argsIOList[0].isNotEmpty()) {
        try {
            val argFileName = argsIOList[0]
            file = File(argFileName)
            flashCards.putAll(import(flashCardAdapter, file))
        } catch (e: Exception) { }
    }

    while (true) {
        printLnAndLog("Input the action (add, remove, import, export," +
                " ask, exit, log, hardest card, reset stats):")
        when(readLnAndLog()) {
            "add" -> flashCards = addFlashCard(flashCards)

            "remove" -> flashCards = removeFlashCard(flashCards)

            "import" -> {
                printLnAndLog("File name:")
                file = File(readLnAndLog())

                if (file.exists()) {
                    flashCards.putAll(import(flashCardAdapter, file))
                } else printLnAndLog("File not found.")
            }

            "export" -> {
                printLnAndLog("File name:")
                file = File(readLnAndLog())
                export(flashCardAdapter, flashCards, file)
            }

            "ask" -> flashCards = checkAnswers(flashCards)

            "exit" -> {
                if (argsIOList[1].isNotEmpty()) {
                    file = File(argsIOList[1])
                    export(flashCardAdapter, flashCards, file)
                }
                printLnAndLog("Bye bye!")
                exitProcess(0)
            }

            "log" -> saveLog()

            "hardest card" -> getHardestCards(flashCards)

            "reset stats" -> flashCards = resetStats(flashCards)

            else -> printLnAndLog("Wrong action input.")
        }
    }
}


fun getIOFromArgs(args: List<String>) : List<String> {

    val list = mutableListOf("", "")

    try {
        for (i in 0 until args.size - 1) {
            if (args[i] == "-import") list[0] = args[i + 1]
            if (args[i] == "-export") list[1] = args[i + 1]
        }
    } catch (e: Exception) { }

    return list.toList()
}


fun addFlashCard(flashCards: MutableMap<String, Map<String, Int>>) :
        MutableMap<String, Map<String, Int>> {

    printLnAndLog("The card:")
    val term = readLnAndLog()

    if (flashCards.containsKey(term)) {
        printLnAndLog("The card \"$term\" already exists. Try again:")
        return flashCards
    }

    printLnAndLog("The definition of the card:")
    val definition = readLnAndLog()

    flashCards.forEach {
        if (it.value.keys.contains(definition)) {
            printLnAndLog("The definition \"$definition\" already exists. Try again.")
            return flashCards
        }
    }

    flashCards[term] = mapOf(Pair(definition, 0))
    printLnAndLog("The pair (\"$term\":\"$definition\") has been added.")

    return flashCards
}


fun removeFlashCard(flashCards: MutableMap<String, Map<String, Int>>) :
        MutableMap<String, Map<String, Int>> {

    printLnAndLog("Which card?")
    val input = readLnAndLog()

    if (flashCards.containsKey(input)) {
        flashCards.remove(input)
        printLnAndLog("The card has been removed.")
    } else printLnAndLog("Can't remove \"$input\": there is no such card.")

    return flashCards
}


fun import(flashCardAdapter: JsonAdapter<MutableMap<String, Map<String, Int>>>,
           file: File) : MutableMap<String, Map<String, Int>> {

    val impFlashCards = mutableMapOf<String, Map<String, Int>>()

    try {
        flashCardAdapter.fromJson(file.readText())?.let { mapEntry ->
            mapEntry.forEach {
                impFlashCards[it.key] = mapOf(
                    Pair(
                        it.value.keys.joinToString(""),
                        it.value.values.joinToString("").toDouble().toInt()
                    )
                )
            }
        }
    } catch (e:Exception) { }

    printLnAndLog("${impFlashCards.size} cards have been loaded.")
    return impFlashCards
}


fun export(flashCardAdapter: JsonAdapter<MutableMap<String, Map<String, Int>>>,
           flashCards: MutableMap<String, Map<String, Int>>, file: File) {

    file.writeText(flashCardAdapter.indent("  ").toJson(flashCards))
    printLnAndLog("${flashCards.size} cards have been saved.")
}


fun checkAnswers(flashCardsMap: MutableMap<String, Map<String, Int>>) :
        MutableMap<String, Map<String, Int>> {

    val result = mutableMapOf<String, Map<String, Int>>()
    result.putAll(flashCardsMap)
    val flashCards = flashCardsMap.toList()
    val n = getNumberOfCards()
    for (i in 0 until n) {

        val currentN = Random.nextInt(flashCards.size)
        val term = flashCards[currentN].first
        printLnAndLog("Print the definition of \"${term}\":")
        val answer = readLnAndLog()
        var alternativeTerm = ""


        if (flashCards[currentN].second.containsKey(answer)) {
            printLnAndLog("Correct!")
        } else {

            result[term] = mapOf(
                Pair(
                    result[term]!!.keys.joinToString(""),
                    result[term]!!.values.joinToString("").toInt() + 1
                )
            )

            flashCards[currentN].first
            flashCards.forEach {
                if (it.second.containsKey(answer)) alternativeTerm = it.first
            }

            val endOfString = if (alternativeTerm.isNotEmpty()) {
                ", but your definition is correct for \"$alternativeTerm\"."
            } else "."

            printLnAndLog("Wrong. The right answer is " +
                    "\"${flashCards[currentN].second.keys.joinToString("")}\"" + endOfString)
        }
    }

    return result
}


fun getNumberOfCards() : Int {
    printLnAndLog("How many times to ask?")
    return try {
        readLnAndLog().toInt()
    } catch (e:NumberFormatException) { 0 }
}


fun printLnAndLog(str: String) {
    println(str)
    log.add(str)
}


fun readLnAndLog() : String {
    val str = readln()
    log.add(str)
    return str
}


fun saveLog() {
    printLnAndLog("File name:")
    val file = File(readLnAndLog())
    file.writeText(log.joinToString("\n"))
    printLnAndLog("The log has been saved.")
}


fun getHardestCards(flashCards: MutableMap<String, Map<String, Int>>) {

    val result = mutableListOf<String>()
    val errorAmountList = mutableListOf<Int>()
    flashCards.forEach {
        errorAmountList.add(it.value.values.joinToString("").toInt())
    }

    val max = errorAmountList.maxOrNull()?: 0
    if (max == 0) {
        printLnAndLog("There are no cards with errors")
    } else {
        flashCards.forEach {
            if (it.value.values.joinToString("").toInt() == max) {
                result.add(it.key)
            }
        }

        if (result.size == 1) {
            printLnAndLog("The hardest card is \"${result[0]}\". " +
                    " You have $max errors answering it.")
        } else {
            printLnAndLog("The hardest cards are " +
                    result.joinToString("\", \"", "\"", "\"." +
                            " You have $max errors answering them."))
        }
    }

}


fun resetStats(flashCards: MutableMap<String, Map<String, Int>>) :
        MutableMap<String, Map<String, Int>> {

    val result = mutableMapOf<String, Map<String, Int>>()
    result.putAll(flashCards)

    flashCards.forEach {
        result[it.key] = mapOf(Pair(it.value.values.joinToString(""), 0))
    }

    printLnAndLog("Card statistics have been reset.")
    return result
}
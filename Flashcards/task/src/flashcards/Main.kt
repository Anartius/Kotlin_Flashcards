package flashcards

import java.io.File
import kotlin.random.Random
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.system.exitProcess

fun main() {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(
        MutableMap::class.java, String::class.java, String::class.java
    )
    val flashCardAdapter = moshi.adapter<MutableMap<String, String>>(type)
    var flashCards = mutableMapOf<String, String>()

    while (true) {
        println("Input the action (add, remove, import, export, ask, exit):")
        when(readln()) {
            "add" -> flashCards = addFlashCard(flashCards)

            "remove" -> flashCards = removeFlashCard(flashCards)

            "import" -> flashCards.putAll(import(flashCardAdapter))

            "export" -> export(flashCardAdapter, flashCards)

            "ask" -> checkAnswers(flashCards)

            "exit" -> {
                println("Bye bye!")
                exitProcess(0)
            }

            else -> println("Wrong action input.")
        }
    }
}


fun addFlashCard(flashCards: MutableMap<String, String>) :
        MutableMap<String, String> {

    println("The card:")
    val term = readln()

    if (flashCards.containsKey(term)) {
        println("The card \"$term\" already exists. Try again:")
        return flashCards
    }

    println("The definition of the card:")
    val definition = readln()
    if (flashCards.containsValue(definition)) {
        println("The definition \"$definition\" already exists. Try again.")
        return flashCards
    }

    flashCards[term] = definition
    println("The pair (\"$term\":\"$definition\") has been added.")

    return flashCards
}


fun removeFlashCard(flashCards: MutableMap<String, String>) :
        MutableMap<String, String> {

    println("Which card?")
    val input = readln()

    if (flashCards.containsKey(input)) {
        flashCards.remove(input)
        println("The card has been removed.")
    } else println("Can't remove \"$input\": there is no such card.")

    return flashCards
}


fun import(flashCardAdapter: JsonAdapter<MutableMap<String, String>>) :
        MutableMap<String, String> {

    val newFlashCards = mutableMapOf<String, String>()
    println("File name:")
    val file = File(readln())

    if (file.exists()) {
        try {
            flashCardAdapter.fromJson(file.readText())?.let {newFlashCards.putAll(it) }
        } catch (e:Exception) { }
    } else {
        println("File not found.")
        return newFlashCards
    }

    println("${newFlashCards.size} cards have been loaded.")
    return newFlashCards
}


fun export(flashCardAdapter: JsonAdapter<MutableMap<String, String>>,
           flashCards: MutableMap<String, String>) {

    println("File name:")
    val file = File(readln())
    file.writeText(flashCardAdapter.indent("  ").toJson(flashCards))
    println("${flashCards.size} cards have been saved.")
}


fun checkAnswers(flashCardsMap: MutableMap<String, String>) {

    val flashCards = flashCardsMap.toList()
    val n = getNumberOfCards()
    for (i in 0 until n) {

        val currentN = Random.nextInt(flashCards.size)
        println("Print the definition of \"${flashCards[currentN].first}\":")
        val answer = readln()
        var alternativeTerm = ""

        if (flashCards[currentN].second == answer) {
            println("Correct!")
        } else {
            flashCards.forEach {
                if (it.second == answer) alternativeTerm = it.first
            }

            val endOfString = if (alternativeTerm.isNotEmpty()) {
                ", but your definition is correct for \"$alternativeTerm\"."
            } else "."

            println("Wrong. The right answer is " +
                    "\"${flashCards[currentN].second}\"" + endOfString)
        }
    }
}


fun getNumberOfCards() : Int {
    println("How many times to ask?")
    return try {
        readln().toInt()
    } catch (e:NumberFormatException) { 0 }
}


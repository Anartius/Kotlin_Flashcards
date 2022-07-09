package flashcards

data class Flashcard(val term: String, val definition: String)

fun main() {

    val flashCards = getFlashCards()
    checkAnswers(flashCards)
}


fun getFlashCards() : List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()
    val n = getNumberOfCards()
    var term: String
    var termExists: Boolean
    var definition: String
    var definitionExists: Boolean

    for (i in 0 until n) {

        println("Card #${i + 1}:")
        while (true) {
            termExists = false
            term = readln()
            flashcards.forEach { if (it.term == term) termExists = true }
            if (!termExists) {
                break
            } else println("The term \"$term\" already exists. Try again:")
        }

        println("The definition for card #${i + 1}:")
        while (true) {
            definitionExists = false
            definition = readln()
            flashcards.forEach {
                if (it.definition == definition) definitionExists = true
            }
            if (!definitionExists) {
                break
            } else {
                println("The definition \"$definition\" already exists. Try again.")
            }
        }

        flashcards.add(Flashcard(term, definition))
    }
    return flashcards.toList()
}


fun getNumberOfCards() : Int {
    println("Input the number of cards:")
    return try {
        readln().toInt()
    } catch (e:NumberFormatException) { 0 }
}


fun checkAnswers(flashcards: List<Flashcard>) {
    for (i in flashcards.indices) {
        println("Print the definition of \"${flashcards[i].term}\":")
        val answer = readln()
        var alternativeTerm = ""


        if (flashcards[i].definition == answer) {
            println("Correct!")
        } else {
            flashcards.forEach {
                if (it.definition == answer) alternativeTerm = it.term
            }

            val endOfString = if (alternativeTerm.isNotEmpty()) {
                ", but your definition is correct for \"$alternativeTerm\"."
            } else "."

            println("Wrong. The right answer is \"${flashcards[i].definition}\"" +
                    endOfString)
        }
    }
}
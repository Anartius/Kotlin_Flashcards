package flashcards

data class Flashcard(val term: String, val definition: String)

fun main() {

    val flashCards = getFlashCards()
    checkAnswers(flashCards)
}


fun getFlashCards() : List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()

    println("Input the number of cards:")
    val n = try {
        readln().toInt()
    } catch (e:NumberFormatException) {
        0
    }

    var front: String
    var back: String
    for (i in 0 until n) {
        println("Card #${i + 1}:")
        front = readln()
        println("The definition for card #${i + 1}:")
        back = readln()
        flashcards.add(Flashcard(front, back))
    }
    return flashcards.toList()
}


fun checkAnswers(flashcards: List<Flashcard>) {
    for (i in flashcards.indices) {
        println("Print the definition of \"${flashcards[i].term}\":")
        val answer = readln()
        println(
            if (flashcards[i].definition == answer) {
                "Correct!"
            } else "Wrong. The right answer is \"${flashcards[i].definition}\"."
        )
    }
}
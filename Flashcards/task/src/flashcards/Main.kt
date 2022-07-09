package flashcards

data class Flashcard(val front: String, val back: String)

fun main() {
    val front = readln()
    val back = readln()
    val flashCard = Flashcard(front, back)
    print(if (readln() == flashCard.back) "right" else "wrong")
}

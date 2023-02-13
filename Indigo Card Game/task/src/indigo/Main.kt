package indigo
val numbersCards = mutableListOf("K","Q","J","10","9","8","7","6","5","4","3","2","A")
val typeCards = mutableListOf("♣", "♦", "♥", "♠" )
var cards = mutableListOf<String>()
var cardToPlay = ""
val tableCards = mutableListOf<String>()
var scorePlayer = 0
val winCardsPlayer = mutableListOf<String>()
var scoreComputer = 0
val winCardsComputer = mutableListOf<String>()
var playerCards = mutableListOf<String>()
var pcCards = mutableListOf<String>()
var lastWinner = 0
var firstPlayer = ""

fun main() {
    resetCards()
    println("Indigo Card Game")
    loop@while(true) {
        println("Play first?")
        firstPlayer = readln()
        when (firstPlayer) {
            "yes" -> playGame("yes")
            "no" -> playGame("no")
            else -> continue@loop
        }
        return
    }
}

fun resetCards () {
    cards.clear()
    for (i in typeCards){
        for (a in numbersCards) {
            cards.add(a + i)
        }
    }
    cards.shuffle()
}

fun playGame(option : String) {

    var turn = if (option == "yes") 0 else 1
    println("Initial cards on the table: ${cards[0]} ${cards[1]} ${cards[2]} ${cards[3]}")
    for (i in 0..3) {
        tableCards.add(cards[0])
        cards.removeAt(0)
    }
    playerCards = addCards(playerCards)
    pcCards = addCards(pcCards)
    do {
        if (tableCards.size != 0){
            println("\n${tableCards.size} cards on the table, and the top card is ${tableCards.last()}")
        } else {
            println("\nNo cards on the table")
        }
        if (turn % 2 == 0) {
            playerCardOption( 1)
            if (playerCards.isEmpty()&& cards.isNotEmpty()) playerCards = addCards(playerCards)
            if (cardToPlay == "exit") return
            turn++
        } else {
            playerCardOption(2)
            if (pcCards.isEmpty() && cards.isNotEmpty()) pcCards = addCards(pcCards)
            if (cardToPlay == "exit") return
            turn++
        }
        if (playerCards.isEmpty() && pcCards.isEmpty()) break
    } while ( true)
    println("Game Over")
}

fun addCards (joinCards : MutableList<String>) : MutableList<String>{
    for (i in 0..5) {
        joinCards.add(cards[0])
        cards.removeAt(0)
    }
    return  joinCards
}

fun playerCardOption(turn : Int){
    var indexPlayerCard = 1
    if (turn == 1) {
        print("Cards in hand: ")
        for (i in playerCards) {
            print("$indexPlayerCard)$i ")
            indexPlayerCard++
        }
    }
    loop2@ do {
        if (turn == 1) {
            println("\nChoose a card to play (1-${playerCards.size}):")
            cardToPlay = readln()
            if (cardToPlay == "exit") {
                println("Game Over")
                return
            }
            //conditions for input if out of range or it´s letters
            if (cardToPlay.length > 1) continue
            for (i in cardToPlay) {
                if (i.isLetter()) continue@loop2
                if (cardToPlay.toInt() !in 1..playerCards.size) continue@loop2
            }
        }
        if (turn == 1) {
            tableCards.add(playerCards[cardToPlay.toInt() - 1])
            playerCards.removeAt(cardToPlay.toInt() - 1)
        }

        if (turn == 2) {
            val candidateCards = mutableListOf<String>()
            var trebol = 0
            var picas = 0
            var diamantes = 0
            var corazones = 0
            for (i in pcCards) print("$i ")

            fun addCandidates(typeCard : Char){
                for (i in pcCards){
                    if (typeCard in i) candidateCards.add(i)
                }
            }
            fun moreThanOne() {
                for (i in pcCards) {
                    if (i.contains('♣')) trebol++
                    if (i.contains('♦')) diamantes++
                    if (i.contains('♥')) corazones++
                    if (i.contains('♠')) picas++
                }
                if (tableCards.isEmpty()) {
                    if ((trebol > 1 || diamantes > 1 || corazones > 1 || picas > 1)) {
                        if (trebol > 1) addCandidates('♣')
                        if (diamantes > 1) addCandidates('♦')
                        if (corazones > 1) addCandidates('♥')
                        if (picas > 1) addCandidates('♠')
                        cardToPlay = candidateCards.random()
                        tableCards.add(cardToPlay)
                        pcCards.remove(cardToPlay)
                        return
                    }
                    if (pcCards.size <= 4) {
                        var repeats = 0
                        for (i in '\u0031'..'\u0051') {
                            for (e in pcCards) {
                                if (e.contains(i)) repeats++
                            }
                            if (repeats > 1) {
                                for (o in pcCards) {
                                    if (o.contains(i)) {
                                        candidateCards.add(o)
                                    }
                                }
                            }
                            repeats = 0
                        }
                        if (candidateCards.isNotEmpty()) {
                            cardToPlay = candidateCards.random()
                            tableCards.add(cardToPlay)
                            pcCards.remove(cardToPlay)
                        }
                    }
                    if (candidateCards.isEmpty()) {
                        cardToPlay = pcCards.random().also { cardToPlay = it }
                        tableCards.add(cardToPlay)
                        pcCards.remove(cardToPlay)
                    }
                    candidateCards.clear()
                    return
                } else {
                    val suitCardToPlay =
                        if (tableCards.last().length == 2) tableCards.last().substring(1, 2) else tableCards.last()
                            .substring(2, 3)
                    val numberCardToPlay =
                        if (tableCards.last().length == 2) tableCards.last().substring(0, 1) else tableCards.last()
                            .substring(0, 2)
                    for (i in pcCards) {
                        if (i.contains(suitCardToPlay)) candidateCards.add(i)
                    }
                    //when table cards is not empty and there are more than 1 player card with the same suit that the top card
                    if (candidateCards.size > 1) {
                        //println("llega1")
                        cardToPlay = candidateCards.random()
                        tableCards.add(cardToPlay)
                        pcCards.remove(cardToPlay)
                        candidateCards.clear()
                        return
                    }
                    if (candidateCards.size == 0) {
                        //println("llega2")
                        for (i in pcCards) {
                            if (i.contains(numberCardToPlay)) {
                                candidateCards.add(i)
                            }
                            if (candidateCards.size > 0) {
                                cardToPlay = if (candidateCards.size > 1) candidateCards.random() else candidateCards[0]
                                tableCards.add(cardToPlay)
                                pcCards.remove(cardToPlay)
                                candidateCards.clear()
                                return
                            }
                        }
                    }
                    if (candidateCards.size == 1) {
                        //println("llega3")
                        for (i in pcCards) {
                            if (numberCardToPlay in i) {
                                candidateCards.add(i)
                            }
                        }
                        if (candidateCards.size == 2) {
                            cardToPlay = candidateCards.random()
                            tableCards.add(cardToPlay)
                            pcCards.remove(cardToPlay)
                            candidateCards.clear()
                            return
                        }
                        cardToPlay = if (candidateCards.size == 2) candidateCards.random() else candidateCards[0]
                        tableCards.add(cardToPlay)
                        pcCards.remove(cardToPlay)
                        candidateCards.clear()
                        return
                    }
                    if (candidateCards.isEmpty()) {
                        //println("llega4")
                        val maxSuit = maxOf(trebol, diamantes, picas, corazones)
                        if (maxSuit == trebol) for (i in pcCards) if (i.contains('♣')) candidateCards.add(i)
                        if (maxSuit == diamantes) for (i in pcCards) if (i.contains('♦')) candidateCards.add(i)
                        if (maxSuit == picas) for (i in pcCards) if (i.contains('♠')) candidateCards.add(i)
                        if (maxSuit == corazones) for (i in pcCards) if (i.contains('♥')) candidateCards.add(i)

                        if (candidateCards.isEmpty()) {
                            cardToPlay = pcCards.random().also { cardToPlay = it }

                        } else {
                            cardToPlay = candidateCards.random().also { cardToPlay = it }
                        }
                        tableCards.add(cardToPlay)
                        pcCards.remove(cardToPlay)
                        candidateCards.clear()
                    }
                }
            }
            moreThanOne()
            println("\nComputer plays $cardToPlay")
        }
        //if anyone wins round print it
        var winRound = 0
        if (tableCards.size >1){
            for (i in tableCards[tableCards.size - 2]) {
                if (tableCards.last().contains(i)) {
                    if (turn == 1) {
                        println("Player wins cards")
                        winRound = 1
                        break
                    }
                    if (turn == 2) {
                        println("Computer wins cards")
                        winRound = 2
                        break
                    }
                }
            }
        }
        //when a player wins round count cards for score and add table cards to win cards of the winner round player.
        //and table cards return to 0 cards
        if (winRound == 1 || winRound == 2) {
            for (i in tableCards) {
                if (winRound == 1) {
                    winCardsPlayer.add(i)
                    if (i.contains('A') || i.contains('K') || i.contains('Q') || i.contains('J') || i.contains("10")) {
                        scorePlayer++
                    }
                }
                if (winRound == 2) {
                    winCardsComputer.add(i)
                    if (i.contains('A') || i.contains('K') || i.contains('Q') || i.contains('J') || i.contains("10")) {
                        scoreComputer++
                    }
                }
            }
            tableCards.clear()
            //for the last round if win player one or two
            if (cards.isEmpty() && playerCards.isEmpty() && pcCards.isEmpty()) {
                println("Score: Player $scorePlayer - Computer $scoreComputer")
                println("Cards: Player ${winCardsPlayer.size} - Computer ${winCardsComputer.size}\n")
                println("No cards on the table")
                if (scorePlayer > scoreComputer) scorePlayer += 3 else scoreComputer += 3
            }
            if (winRound == 1) lastWinner = 1
            if (winRound == 2) lastWinner = 2
        }
        //print results
        if (winRound == 1 || winRound == 2) {
            println("Score: Player $scorePlayer - Computer $scoreComputer")
            println("Cards: Player ${winCardsPlayer.size} - Computer ${winCardsComputer.size}")
        }
        if (winRound == 0 && pcCards.isEmpty() && playerCards.isEmpty() && cards.isEmpty()) {
            if (turn == 2) println()
            if (lastWinner == 1) {
                for (i in tableCards) {
                    if (i.contains('A') || i.contains('K') || i.contains('Q') || i.contains('J') || i.contains("10")) {
                        scorePlayer++
                    }
                }
                winCardsPlayer.addAll(tableCards)
            }
            if (lastWinner == 2) {
                for (i in tableCards) {
                    if (i.contains('A') || i.contains('K') || i.contains('Q') || i.contains('J') || i.contains("10")) {
                        scoreComputer++
                    }
                }
                winCardsComputer.addAll(tableCards)
            }
            if (lastWinner == 0 && firstPlayer == "yes") {
                for (i in tableCards) {
                    if (i.contains('A') || i.contains('K') || i.contains('Q') || i.contains('J') || i.contains("10")) {
                        scorePlayer++
                    }
                }
                winCardsPlayer.addAll(tableCards)
            }
            if (lastWinner == 0 && firstPlayer == "no") {
                for (i in tableCards) {
                    if (i.contains('A') || i.contains('K') || i.contains('Q') || i.contains('J') || i.contains("10")) {
                        scoreComputer++
                    }
                }
                winCardsPlayer.addAll(tableCards)
            }
            if (scorePlayer > scoreComputer) scorePlayer += 3 else scoreComputer += 3
            if (tableCards.size != 0){
                println("${tableCards.size} cards on the table, and the top card is ${tableCards.last()}")
            } else {
                println("No cards on the table")
            }
            println("Score: Player $scorePlayer - Computer $scoreComputer")
            println("Cards: Player ${winCardsPlayer.size} - Computer ${winCardsComputer.size}")
        }
        break
    } while (true)
}


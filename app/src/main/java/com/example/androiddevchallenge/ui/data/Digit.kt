package com.example.androiddevchallenge.ui.data

enum class Digit(val stringValue: String, val intValue: Int) {
    ZERO("0", 0),
    ONE("1", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9);

    companion object {
        fun from(digit: Int) = values().find { it.intValue == digit } ?: error("Unexpected digit: $digit")
    }
}

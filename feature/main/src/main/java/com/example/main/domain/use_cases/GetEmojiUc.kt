package com.example.main.domain.use_cases

interface GetEmojiUc {
    operator fun invoke(percent: Double): Int
}
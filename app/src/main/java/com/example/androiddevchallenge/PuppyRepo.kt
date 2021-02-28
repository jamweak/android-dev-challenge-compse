package com.example.androiddevchallenge

import kotlin.random.Random
import kotlin.random.nextInt

object PuppyRepo {
    private val dogImageRes = intArrayOf(
        R.drawable.dog_1,
        R.drawable.dog_2,
        R.drawable.dog_3,
        R.drawable.dog_4,
        R.drawable.dog_5,
        R.drawable.dog_6,
        R.drawable.dog_7,
        R.drawable.dog_8,
        R.drawable.dog_9,
        R.drawable.dog_10
    )

    val list = mutableListOf<Puppy>()

    init {
        if (list.isEmpty()) {
            val random = Random(10)
            for (i in 1..10) {
                list.add(Puppy(i, random.nextInt(0..9999), dogImageRes[i-1]))
            }
        }
    }
}
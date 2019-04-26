package com.example.a2048310

class Scores(val idscore: String, val user: String, val moves: Int,
             val points: Int, val time: String, val level: String)

data class HighScoresLoad(val user: String = "",
                          val level: String = "",
                          val moves: Int = 0,
                          val points: Int = 0,
                          val time: String = "")
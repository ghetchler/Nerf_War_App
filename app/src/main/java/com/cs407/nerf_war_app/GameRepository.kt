package com.cs407.nerf_war_app

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.atomic.AtomicInteger

data class Player(var name: String, var isIn: Boolean, var team: String? = null)
data class Game(
    val id: Int,
    val name: String,
    var location: String? = null,
    val players: MutableList<Player> = mutableListOf()
)

class GameRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("GameRepository", Context.MODE_PRIVATE)

    private val gson = Gson()
    private val gameIdCounter = AtomicInteger()

    init {
        // If the game list is empty, we initialize
        if (!sharedPreferences.contains("games")) {
            val editor = sharedPreferences.edit()
            editor.putString("games", gson.toJson(emptyList<Game>()))
            editor.apply()
        } else {
            // Get the highest game ID to continue from
            val games = getAllGames()
            if (games.isNotEmpty()) {
                val maxId = games.maxOf { it.id }
                gameIdCounter.set(maxId + 1)
            }
        }
    }

    fun createGame(name: String): Int {
        val games = getAllGames().toMutableList()
        val newGameId = gameIdCounter.getAndIncrement()
        val newGame = Game(newGameId, name)
        games.add(newGame)
        saveGames(games)
        return newGameId
    }

    fun updateGame(game: Game) {
        val games = getAllGames().toMutableList()
        val index = games.indexOfFirst { it.id == game.id }
        if (index != -1) {
            games[index] = game
            saveGames(games)
        }
    }

    fun getGameById(id: Int): Game? {
        return getAllGames().find { it.id == id }
    }

    private fun getAllGames(): List<Game> {
        val json = sharedPreferences.getString("games", "[]")
        val type = object : TypeToken<List<Game>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveGames(games: List<Game>) {
        val editor = sharedPreferences.edit()
        editor.putString("games", gson.toJson(games))
        editor.apply()
    }
}

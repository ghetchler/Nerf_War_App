package com.cs407.nerf_war_app

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameHUDActivity : AppCompatActivity() {
    private lateinit var gameRepository: GameRepository
    private var gameId: Int = -1
    private lateinit var game: Game
    private lateinit var teamListAdapter: TeamListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_hud)

        gameRepository = GameRepository(this)
        gameId = intent.getIntExtra("GAME_ID", -1)

        val retrievedGame = gameRepository.getGameById(gameId)
        if (retrievedGame == null) {
            // If the game doesn't exist, close the activity or handle the error
            finish()
            return
        }

        game = retrievedGame

        // Initialize HUD Views
        val gameNameView = findViewById<TextView>(R.id.hud_game_name)
        val locationView = findViewById<TextView>(R.id.hud_location)
        val playerStatusView = findViewById<TextView>(R.id.hud_player_status)
        val teamCountView = findViewById<TextView>(R.id.hud_team_count)
        val teamListView = findViewById<ListView>(R.id.team_list_view)

        // Set the game name and location
        gameNameView.text = game.name
        locationView.text = "Location: ${game.location ?: "Unknown"}"

        // Calculate and display player in/out status
        val playersIn = game.players.count { it.isIn }
        val playersOut = game.players.count { !it.isIn }
        playerStatusView.text = "In: $playersIn | Out: $playersOut"

        // Calculate number of teams
        val uniqueTeams = game.players.mapNotNull { it.team }.distinct()
        teamCountView.text = "Teams: ${uniqueTeams.size}"

        // Initialize and set adapter for the list of teams and players
        teamListAdapter = TeamListAdapter(this, game)
        teamListView.adapter = teamListAdapter
    }
}

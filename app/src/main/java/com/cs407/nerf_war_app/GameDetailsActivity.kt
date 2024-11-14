package com.cs407.nerf_war_app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GameDetailsActivity : AppCompatActivity() {
    private lateinit var gameRepository: GameRepository
    private var gameId: Int = -1
    private lateinit var game: Game
    private lateinit var playerListAdapter: PlayerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        gameRepository = GameRepository(this)
        gameId = intent.getIntExtra("GAME_ID", -1)

        val retrievedGame = gameRepository.getGameById(gameId)
        if (retrievedGame == null) {
            // If the game doesn't exist, close the activity or handle the error
            finish()
            return
        }

        game = retrievedGame

        val gameNameView = findViewById<TextView>(R.id.game_name)
        gameNameView.text = game.name

        val gameLocationView = findViewById<EditText>(R.id.game_location)
        gameLocationView.setText(game.location)

        val playerListView = findViewById<ListView>(R.id.player_list_view)
        playerListAdapter = PlayerListAdapter(this, game, gameRepository)
        playerListView.adapter = playerListAdapter

        val addPlayerButton = findViewById<Button>(R.id.add_player_button)
        addPlayerButton.setOnClickListener {
            // Logic to add a player to the game
            val newPlayerName = "Player ${game.players.size + 1}"
            val newPlayer = Player(newPlayerName, true, "Team A")
            game.players.add(newPlayer)
            gameRepository.updateGame(game)
            playerListAdapter.notifyDataSetChanged()
        }

        val startGameButton = findViewById<Button>(R.id.start_game_button)
        startGameButton.setOnClickListener {
            // Update game location before starting
            game.location = gameLocationView.text.toString().trim()

            // Navigate to GameHUDActivity
            val intent = Intent(this, GameHUDActivity::class.java)
            intent.putExtra("GAME_ID", gameId)
            startActivity(intent)
        }
    }
}

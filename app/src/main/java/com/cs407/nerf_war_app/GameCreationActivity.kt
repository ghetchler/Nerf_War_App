package com.cs407.nerf_war_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class GameCreationActivity : AppCompatActivity() {
    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_creation)

        gameRepository = GameRepository(this)

        val gameNameInput = findViewById<EditText>(R.id.game_name_input)
        val createGameButton = findViewById<Button>(R.id.create_game_button)

        createGameButton.setOnClickListener {
            val gameName = gameNameInput.text.toString()
            if (gameName.isBlank()) {
                gameNameInput.error = "Game name cannot be empty"
                return@setOnClickListener
            }

            // Create the game and navigate to details page
            val gameId = gameRepository.createGame(gameName)
            val intent = Intent(this, GameDetailsActivity::class.java)
            intent.putExtra("GAME_ID", gameId)
            startActivity(intent)
        }
    }
}

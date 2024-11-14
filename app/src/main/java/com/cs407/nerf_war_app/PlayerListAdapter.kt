package com.cs407.nerf_war_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog

class PlayerListAdapter(
    private val context: Context,
    private val game: Game,
    private val gameRepository: GameRepository
) : BaseAdapter() {
    override fun getCount(): Int = game.players.size
    override fun getItem(position: Int): Any = game.players[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val player = game.players[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.item_player, parent, false)

        val playerNameView = rowView.findViewById<TextView>(R.id.player_name)
        val playerStatusSwitch = rowView.findViewById<Switch>(R.id.player_status_switch)
        val playerTeamView = rowView.findViewById<TextView>(R.id.player_team)
        val editPlayerButton = rowView.findViewById<Button>(R.id.edit_player_button)

        // Set player name, status and team
        playerNameView.text = player.name
        playerStatusSwitch.isChecked = player.isIn
        playerTeamView.text = player.team ?: "No Team"

        // Toggle player's in-game status
        playerStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
            player.isIn = isChecked
            gameRepository.updateGame(game)
        }

        // Open a dialog to edit player details
        editPlayerButton.setOnClickListener {
            showEditPlayerDialog(position)
        }

        return rowView
    }

    private fun showEditPlayerDialog(playerPosition: Int) {
        val player = game.players[playerPosition]
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_player, null)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val playerNameInput = dialogView.findViewById<EditText>(R.id.edit_player_name)
        val playerTeamSpinner = dialogView.findViewById<Spinner>(R.id.player_team_spinner)
        val saveButton = dialogView.findViewById<Button>(R.id.save_player_changes_button)

        // Set existing name and team
        playerNameInput.setText(player.name)
        val teams = arrayOf("Team A", "Team B", "Team C", "None")
        val teamAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, teams)
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        playerTeamSpinner.adapter = teamAdapter
        val selectedTeamPosition = teams.indexOf(player.team ?: "None")
        playerTeamSpinner.setSelection(if (selectedTeamPosition != -1) selectedTeamPosition else teams.size - 1)

        saveButton.setOnClickListener {
            val updatedName = playerNameInput.text.toString()
            val updatedTeam = playerTeamSpinner.selectedItem as String

            player.name = updatedName
            player.team = if (updatedTeam == "None") null else updatedTeam
            game.players[playerPosition] = player
            gameRepository.updateGame(game)
            notifyDataSetChanged()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}

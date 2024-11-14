package com.cs407.nerf_war_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding

class TeamListAdapter(private val context: Context, private val game: Game) : BaseAdapter() {

    private val teamsAndPlayers: Map<String, List<Player>>

    init {
        // Group players by their team
        teamsAndPlayers = game.players
            .filter { it.team != null }
            .groupBy { it.team!! }
    }

    override fun getCount(): Int = teamsAndPlayers.size
    override fun getItem(position: Int): Any = teamsAndPlayers.keys.toList()[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val teamName = teamsAndPlayers.keys.toList()[position]
        val players = teamsAndPlayers[teamName] ?: emptyList()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.item_team, parent, false)

        val teamNameView = rowView.findViewById<TextView>(R.id.team_name)
        val teamPlayersLayout = rowView.findViewById<LinearLayout>(R.id.team_players_container)
        val teamPlayerCount = rowView.findViewById<TextView>(R.id.team_player_count)

        teamNameView.text = teamName
        teamPlayerCount.text = "Players: ${players.size}"
        teamPlayersLayout.removeAllViews()

        players.forEach { player ->
            val playerView = TextView(context).apply {
                text = player.name + if (player.isIn) " (In)" else " (Out)"
                textSize = 16f
                setPadding(8)
            }
            teamPlayersLayout.addView(playerView)
        }

        return rowView
    }
}

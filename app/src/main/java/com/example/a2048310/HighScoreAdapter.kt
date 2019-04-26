package com.example.a2048310

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.highscores_template.view.*

class HighScoreAdapter(private val scores: List<HighScoresLoad>, private val listener: InterfaceHighScoresinterface)
: RecyclerView.Adapter<HighScoreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.highscores_template, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = scores.size

    override fun onBindViewHolder(holder: HighScoreAdapter.ViewHolder, position: Int) =
        holder.bind(scores[position], listener)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(score: HighScoresLoad, listener: InterfaceHighScoresinterface) = with(itemView) {
            tvUser.text = FirebaseAuth.getInstance().currentUser.toString()
            tvLevel.text = score.level
            tvPoints.text = score.points.toString()
            tvTime.text = score.time
            tvMoves.text = score.moves.toString()

            // Implementar los eventos
            setOnClickListener { listener.onClick(score, adapterPosition) }
        }
    }
}
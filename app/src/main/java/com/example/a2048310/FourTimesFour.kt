package com.example.a2048310

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MotionEvent
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import com.example.a2048310.Tiles4.Companion.movimientosRestantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_four_times_four.*

class FourTimesFour : AppCompatActivity() {

    companion object {
        private const val GAME_KEY = "2048_GAME_KEY"
    }

    private lateinit var game: Tiles4
    private var x1 = 0f
    private var y1 = 0f
    private var cells = IntArray(16)
    private lateinit var cronometer: Chronometer
//    val gameZ = BD2048.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_four_times_four)

        cronometer = findViewById(R.id.chronometer)

        cells = IntArray(16)
        cells[0] = R.id.Pos0
        cells[1] = R.id.Pos1
        cells[2] = R.id.Pos2
        cells[3] = R.id.Pos3
        cells[4] = R.id.Pos4
        cells[5] = R.id.Pos5
        cells[6] = R.id.Pos6
        cells[7] = R.id.Pos7
        cells[8] = R.id.Pos8
        cells[9] = R.id.Pos9
        cells[10] = R.id.Pos10
        cells[11] = R.id.Pos11
        cells[12] = R.id.Pos12
        cells[13] = R.id.Pos13
        cells[14] = R.id.Pos14
        cells[15] = R.id.Pos15

        if (savedInstanceState != null) {
            val tmpGame :Tiles4? = savedInstanceState.getSerializable(GAME_KEY) as Tiles4
            if (tmpGame != null) {
                this.game = tmpGame
                game.rePoblar()
            }
        } else this.game = Tiles4()
        dibujarCuadros()

        cronometer.start()
    }

/*
fun onClick() {
val intent = Intent(this, MainActivity::class.java)
startActivity(intent)
}
*/

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(GAME_KEY, game)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            val tmpGame :Tiles4? = savedInstanceState.getSerializable(GAME_KEY) as Tiles4
            if (tmpGame != null) {
                game = tmpGame
                game.rePoblar()
            }
        }
        dibujarCuadros()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            // obtener coordenadas
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                val x2 = event.x
                val y2 = event.y

                val minDistance = 200
                var moved = false

                //Swipe izquierda a derecha
                if (x1 < x2 && x2 - x1 > minDistance) {
                    moved = game.accion(Movimientos.Right)
                }

                // Swipe derecha a izquierda
                if (x1 > x2 && x1 - x2 > minDistance) {
                    moved = game.accion(Movimientos.Left)
                }

                // Swipe arriba hacia abajo
                if (y1 < y2 && y2 - y1 > minDistance) {
                    moved = game.accion(Movimientos.Down)
                }

                //Swipe abajo hacia arriba
                if (y1 > y2 && y1 - y2 > minDistance) {
                    moved = game.accion(Movimientos.Up)
                }

                if (moved) dibujarCuadros() // dibujar el cuadro

            }
        }
        return super.onTouchEvent(event)
    }

    private fun dibujarCuadros() {

        movimientosRestantes -= 1
        tvRemainingMovesNumber.text = movimientosRestantes.toString()

        if (game.maxTile >= Tiles4.TARGET) {
            Toast.makeText(this, resources.getString(R.string.en_Winner) +
                    game.maxTile, Toast.LENGTH_LONG).show()
            saveScore()

        } else if (!game.fusionesRestantes() || movimientosRestantes.toString().toInt() == 0) {
            // aqui se tiene que guardar los movimientos restantes y el tiempo

            cronometer.stop()

            val alert = AlertDialog.Builder(this)

            alert.setTitle(getString(R.string.en_AlertDialogTilesTitle))
            alert.setMessage(getString(R.string.en_AlertDialogMessage))

            alert.setPositiveButton(getString(R.string.en_Yes)) { _, _ ->

                val intent = intent
                finish()
                startActivity(intent)
            }

            alert.setNegativeButton(getString(R.string.en_No)) { _, _ ->

                finish()
            }

            saveScore()

            val dialog: AlertDialog = alert.create()

            dialog.show()

            movimientosRestantes = 12
        }

        Log.i("Logm", game.toString())
        Log.i("Logi", R.id.tvScoreNumber.toString())
        val tv: TextView = this.findViewById(R.id.tvScoreNumber)
        tv.text = StringBuffer(resources.getString(R.string.zero)).append(game.score)

        val transitions = game.transitions
        if (transitions.size == 0) return

        for (trans in transitions) {
            laTransicion(trans)
        }
    }

    @SuppressLint("NewApi")
    private fun elCuadro(obj: Any, value: Int) {

        val tv = obj as TextView

        if (value == 0) {
            tv.text = ""
        } else {
            tv.text = StringBuffer("").append(value)
        }

        var txCol = resources.getColor(R.color.colorTileTextDark, null)
        var bgCol = resources.getColor(R.color.color0, null)

        when (value) {
            2 -> bgCol = resources.getColor(R.color.color2, null)
            4 -> bgCol = resources.getColor(R.color.color4, null)
            8 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color8, null)
            }
            16 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color16, null)
            }
            32 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color32, null)
            }
            64 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color64, null)
            }
            128 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color128, null)
            }
            256 -> bgCol = resources.getColor(R.color.color256, null)
            512 -> bgCol = resources.getColor(R.color.color512, null)
            1024 -> bgCol = resources.getColor(R.color.color1024, null)
            2048 -> bgCol = resources.getColor(R.color.color2048, null)
            else -> {
            }
        }
        tv.setBackgroundColor(bgCol)
        tv.setTextColor(txCol)
    }

    private fun laTransicion(trans: Tiles4.Transition) {

        val tv = findViewById<TextView>(cells[trans.newLocation])

        if (trans.type == Acciones.Compact) {
            //Log.i("Logm", "About to do paint compact")
            //Log.i("Logm", "About to do paint compact w=$w h=$h t=$t")
            try {
                elCuadro(tv, trans.value)
                //Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                elCuadro(tv, trans.value)
            }
        } else {
            elCuadro(tv, trans.value)
        }
    }

    private fun saveScore() {
        val moves = tvRemainingMovesNumber.text.toString().toInt()
        val points = tvScoreNumber.text.toString().toInt()
        val time = chronometer.text.toString()
        val user = FirebaseAuth.getInstance().currentUser.toString()
        val level = "Tiny"
        val ref = FirebaseDatabase.getInstance().getReference("scores")

        val idScore = ref.push().key.toString()
        val score =  Scores(idScore, user, moves, points, time, level)

        ref.child(idScore).setValue(score)
            .addOnCompleteListener {
                Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
            }
    }

}
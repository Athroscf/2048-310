package com.example.a2048310

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Tiles4 internal constructor() : Serializable {

    companion object {
        internal const val TARGET = 2048
        private const val GRID_COUNT = 16
        private const val ROW_COUNT = 4
        private const val COL_COUNT = 4
        private const val ESPACIO = 0
        var movimientosRestantes = 12
    }

    var score = 0
        private set
    private var numEmpty = 16
    var maxTile = 0
        private set

    lateinit var transitions: ArrayList<Transition>
        private set
    private val tiles = IntArray(16)
    private val rand = Random()

    init {
        this.transiciones()
        this.otroCuadrito()
        this.otroCuadrito()
    }

    inner class Transition : Serializable {

        var type: Acciones
        var value = 0
        var newLocation = -1
        private var oldLocation = -1

        constructor(action: Acciones, value: Int, newLocation: Int, oldLocation: Int) {
            type = action
            this.value = value
            this.newLocation = newLocation
            this.oldLocation = oldLocation
        }

        constructor(action: Acciones, value: Int, newLocation: Int) {
            type = action
            this.value = value
            this.newLocation = newLocation
        }
    }

    private fun transiciones() {
        transitions = ArrayList()
    }

    fun rePoblar() {
        this.transiciones()
        for (i in 0 until GRID_COUNT) {
            this.transitions.add(Transition(Acciones.Refresh, tiles[i], i))
        }
    }

    private fun otroCuadrito() {
        if (numEmpty == 0) return

        val value = (rand.nextInt(2) + 1) * 2
        val pos = rand.nextInt(numEmpty)
        var blanks = 0

        for (i in 0 until GRID_COUNT) {
            if (tiles[i] == ESPACIO) {
                if (blanks == pos) {
                    tiles[i] = value
                    if (value > maxTile) maxTile = value
                    numEmpty--
                    transitions.add(Transition(Acciones.Add, value, i))
                    return
                }
                blanks++
            }
        }
    }

    fun getValue(i: Int): Int {
        return tiles[i]
    }

    // Movimientos restantes
    fun fusionesRestantes(): Boolean {

        if (numEmpty > 0) return true

        // check left-right for compact moves remaining.
        var limite = (GRID_COUNT - COL_COUNT) - 1
        for (i in 0..limite) {
            if (tiles[i] == tiles[i + COL_COUNT]) return true
        }

        // check up-down for compact moves remaining.
        limite = GRID_COUNT - 2
        for (i in 0..limite) {
            if ((i + 1) % ROW_COUNT > 0) {
                if (tiles[i] == tiles[i + 1]) return true
            }
        }
        return false
    }

    // Funcion que valida si un cuadro se mueve o no
    private fun deslizarCuadro(index1: Int, index2: Int, index3: Int, index4: Int): Boolean {

        var moved = false
        val tmpArr = intArrayOf(index1, index2, index3, index4)

        // Deslizar o no?
        var es = 0  // vaciar cuadro
        for (j in 0 until tmpArr.size) {
            if (tiles[tmpArr[es]] != ESPACIO) {
                es++
                continue
            } else if (tiles[tmpArr[j]] == ESPACIO) {
                continue
            } else {
                // Otherwise we have a slide condition
                tiles[tmpArr[es]] = tiles[tmpArr[j]]
                tiles[tmpArr[j]] = ESPACIO
                transitions.add(Transition(Acciones.Slide, tiles[tmpArr[es]], tmpArr[es], tmpArr[j]))
                transitions.add(Transition(Acciones.Blank, ESPACIO, tmpArr[j]))
                moved = true
                es++
            }
        }
        return moved
    }

    // Clase que valida la union de dos cuadros
    private fun unirCuadro(index1: Int, index2: Int, index3: Int, index4: Int): Boolean {

        var fusion = false
        val tmpArr = intArrayOf(index1, index2, index3, index4)

        for (j in 0..(tmpArr.size-2)) {

            if (tiles[tmpArr[j]] != ESPACIO && tiles[tmpArr[j]] == tiles[tmpArr[j+1]]) { // we found a matching pair
                val ctv = tiles[tmpArr[j]] * 2   // = compacted tile value
                tiles[tmpArr[j]] = ctv
                tiles[tmpArr[j+1]] = ESPACIO
                score += ctv
                if (ctv > maxTile) {
                    maxTile = ctv
                }  // is this the biggest tile # so far
                transitions.add(Transition(Acciones.Compact, ctv, tmpArr[j], tmpArr[j+1]))
                transitions.add(Transition(Acciones.Blank, ESPACIO, tmpArr[j+1]))
                fusion = true
                numEmpty++
            }
        }
        return fusion
    }

    fun accion(move: Movimientos): Boolean {

        this.transiciones()
        if (!fusionesRestantes()) return false

        val result = when (move) {
            Movimientos.Up -> moverArriba()
            Movimientos.Down -> moverAbajo()
            Movimientos.Right -> moverDerecha()
            Movimientos.Left -> moverIzquierda()
        }

        if (result) {
            otroCuadrito()
        }
        return result
    }


    //Definicion de los posibles movimientos
    private fun deslizarArriba(): Boolean {
        val a = deslizarCuadro(0, 4, 8, 12)
        val b = deslizarCuadro(1, 5, 9, 13)
        val c = deslizarCuadro(2, 6, 10, 14)
        val d = deslizarCuadro(3, 7, 11, 15)
        return a || b || c || d
    }

    private fun deslizarAbajo(): Boolean {
        val a = deslizarCuadro(15,11, 7, 3)
        val b = deslizarCuadro(14,10, 6, 2)
        val c = deslizarCuadro(13,9, 5, 1)
        val d = deslizarCuadro(12,8, 4, 0)
        return a || b || c || d
    }

    private fun deslizarDerecha(): Boolean {
        val a = deslizarCuadro(3,2, 1, 0)
        val b = deslizarCuadro(7,6, 5, 4)
        val c = deslizarCuadro(11,10, 9, 8)
        val d = deslizarCuadro(15, 14, 13, 12)
        return a || b || c || d
    }

    private fun deslizarIzquierda(): Boolean {
        val a = deslizarCuadro(0, 1, 2, 3)
        val b = deslizarCuadro(4, 5, 6, 7)
        val c = deslizarCuadro(8, 9, 10,11)
        val d = deslizarCuadro(12, 13, 14,15)
        return a || b || c || d
    }

    private fun fusionArriba(): Boolean {
        val a = unirCuadro(0, 4, 8, 12)
        val b = unirCuadro(1, 5, 9, 13)
        val c = unirCuadro(2, 6, 10, 14)
        val d = unirCuadro(3, 7, 11, 15)
        return a || b || c || d
    }

    private fun fusionAbajo(): Boolean {
        val a = unirCuadro(15,11, 7, 3)
        val b = unirCuadro(14,10, 6, 2)
        val c = unirCuadro(13,9, 5, 1)
        val d = unirCuadro(12,8, 4, 0)
        return a || b || c || d
    }

    private fun fusionDerecha(): Boolean {
        val a = unirCuadro(3,2, 1, 0)
        val b = unirCuadro(7,6, 5, 4)
        val c = unirCuadro(11,10, 9, 8)
        val d = unirCuadro(15, 14, 13, 12)
        return a || b || c || d
    }

    private fun fusionIzquierda(): Boolean {
        val a = unirCuadro(0, 1, 2, 3)
        val b = unirCuadro(4, 5, 6, 7)
        val c = unirCuadro(8, 9, 10,11)
        val d = unirCuadro(12, 13, 14,15)
        return a || b || c || d
    }

    private fun moverArriba(): Boolean {
        val a = deslizarArriba()
        val b = fusionArriba()
        val c = deslizarArriba()
        return a || b || c
    }

    private fun moverAbajo(): Boolean {
        val a = deslizarAbajo()
        val b = fusionAbajo()
        val c = deslizarAbajo()
        return a || b || c
    }

    private fun moverDerecha(): Boolean {
        val a = deslizarDerecha()
        val b = fusionDerecha()
        val c = deslizarDerecha()
        return a || b || c
    }

    private fun moverIzquierda(): Boolean {
        val a = deslizarIzquierda()
        val b = fusionIzquierda()
        val c = deslizarIzquierda()
        return a || b || c
    }

    override fun toString(): String {
        return "NewTiles:class\n" +
                "--------------------\n" +
                "|" + tiles[0] + "|" + tiles[1] + "|" + tiles[2] + "|" +  tiles[3] + "|" + "\n" +
                "|" + tiles[4] + "|" + tiles[5] + "|" + tiles[6] + "|" +  tiles[7] + "|" + "\n" +
                "|" + tiles[8] + "|" + tiles[9] + "|" + tiles[10] + "|" +  tiles[11] + "|" +  "\n" +
                "|" + tiles[12] + "|" + tiles[13] + "|" + tiles[14] + "|" +  tiles[15] + "|" + "\n" +
                "--------------------\n"
    }
}
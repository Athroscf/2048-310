package com.example.a2048310

class Transition {
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
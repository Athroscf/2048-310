package com.example.a2048310

import android.provider.ContactsContract

interface InterfaceHighScoresinterface {
    fun onClick(note: HighScoresLoad, position: Int)
    fun onLongClick(note: ContactsContract.CommonDataKinds.Note, position: Int)
}
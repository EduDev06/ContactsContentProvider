package com.example.contactscontentprovider

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var contacts by mutableStateOf(emptyList<Contacts>())
        private set

    fun updateContacts(contacts: List<Contacts>) {
        this.contacts = contacts
    }
}
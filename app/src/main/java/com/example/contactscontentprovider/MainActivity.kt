package com.example.contactscontentprovider

import android.Manifest
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.database.getStringOrNull
import com.example.contactscontentprovider.ui.theme.ContactsContentProviderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            0
        )
        val viewModel by viewModels<MainViewModel>()
        viewModel.updateContacts(getContacts())
        setContent {
            ContactsContentProviderTheme {
                MyContent(contacts = viewModel.contacts)
            }
        }
    }

    private fun getContacts(): List<Contacts> {
        val contacts = arrayListOf<Contacts>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
        )
        val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf("Ana")
        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} DESC"
        contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val name = cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone = cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(Contacts(phone.orEmpty(), name.orEmpty()))
            }
        }
        return contacts
    }
}

@Composable
fun MyContent(
    modifier: Modifier = Modifier,
    contacts: List<Contacts>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(contacts) {contact ->
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = contact.name)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = contact.phone)
                }
            }
        }
    }
}
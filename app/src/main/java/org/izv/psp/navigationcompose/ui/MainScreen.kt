package org.izv.psp.navigationcompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.izv.psp.navigationcompose.data.Contact
import org.izv.psp.navigationcompose.ui.theme.NavigationComposeTheme
import kotlin.collections.remove

//https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#0
//https://developer.android.com/develop/ui/compose/navigation

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
        Navigation()
    }
}

@Composable
fun Navigation() {
    val contactos = remember {
        mutableStateListOf(
            Contact("Pepe Pérez", "123"),
            Contact("Juan López", "321"),
            Contact("Beatriz Gómez", "213")
        )
    }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "menu-principal"
    ) {
        composable("menu-principal") {
            MainMenu(navController)
        }
        composable("agregar-contacto") {
            AddContactScreen(navController, contactos)
        }
        composable("buscar-contacto") {
            SearchContactScreen(navController, contactos)
        }
        composable("eliminar-contacto") {
            DeleteContactScreen(navController, contactos)
        }
        composable("listar-contactos") {
            ListContactScreen(navController, contactos)
        }
    }
}

@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("agregar-contacto") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar contacto")
        }
        Button(
            onClick = { navController.navigate("buscar-contacto") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar contacto")
        }
        Button(
            onClick = { navController.navigate("eliminar-contacto") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar contacto")
        }
        Button(
            onClick = { navController.navigate("listar-contactos") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lista de contactos")
        }
    }
}

@Composable
fun AddContactScreen(navController: NavHostController, contactos: MutableList<Contact>) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Agregar contacto",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val contacto = Contact(nombre, telefono)
            contactos.add(contacto)
            nombre = ""
            telefono = ""
        }) {
            Text("Guardar contacto")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}

@Composable
fun SearchContactScreen(navController: NavHostController, contactos: MutableList<Contact>) {
    var consulta by remember { mutableStateOf("") }
    val contactosFiltrados = remember(consulta, contactos) {
        if (consulta.isBlank()) {
            contactos
        } else contactos.filter {
            it.name.contains(consulta, ignoreCase = true)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = consulta,
            onValueChange = { consulta = it },
            label = { Text("Buscar contacto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (contactosFiltrados.isEmpty()) {
            Text("No se encontraron contactos.")
        } else {
            contactosFiltrados.forEach { contact ->
                ShowItem(contact = contact)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}

@Composable
fun ShowItem(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = contact.name + " " + contact.phone)
    }
}

@Composable
fun DeleteContactScreen(navController: NavHostController, contactos: MutableList<Contact>) {
    var consulta by remember { mutableStateOf("") }
    val contactosFiltrados = remember(consulta, contactos) {
        if (consulta.isBlank()) {
            contactos
        } else contactos.filter {
            it.name.contains(consulta, ignoreCase = true)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = consulta,
            onValueChange = { consulta = it },
            label = { Text("Buscar contacto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (contactosFiltrados.isEmpty()) {
            Text("No se encontraron contactos.")
        } else {
            contactosFiltrados.forEach { contact ->
                ShowDeleteItem(
                    contact = contact,
                    onDelete = { contactos.remove(it) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}

@Composable
fun ShowDeleteItem(contact: Contact, onDelete: (Contact) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = contact.name + " " + contact.phone)
        Button(
            onClick = { onDelete(contact) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Eliminar")
        }
    }
}

@Composable
fun ListContactScreen(navController: NavHostController, contactos: MutableList<Contact>) {
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if(contactos.isEmpty()) {
            Text("No se encontraron contactos.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()) {
                items(items = contactos) { contacto ->
                    ContactItemCard(
                        contact = contacto,
                        onDeleteRequest = { contactToDelete = it })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
        if (contactToDelete != null) {
            AlertDialog(
                onDismissRequest = { contactToDelete = null },
                confirmButton = {
                    TextButton(onClick = {
                        contactos.remove(contactToDelete)
                        contactToDelete = null
                    }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { contactToDelete = null }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Seguro que deseas eliminar a ${contactToDelete?.name}?") }
            )
        }
    }
}

@Composable
fun ContactItemCard(contact: Contact, onDeleteRequest: (Contact) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = contact.name + " " + contact.phone)
            Button(
                onClick = { onDeleteRequest(contact) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NavigationComposeTheme {
        Navigation()
    }
}
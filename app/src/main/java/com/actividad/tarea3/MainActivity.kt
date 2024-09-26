package com.actividad.tarea3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.actividad.tarea3.ui.theme.Tarea3Theme
import androidx.compose.ui.platform.LocalContext

data class Food(val name: String, val origin: String, val imageResId: Int, val description: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FoodList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FoodList(modifier: Modifier = Modifier) {
    val foodList = listOf(
        Food("Ramen", "Japón", R.drawable.ramen, "El ramen es una sopa de fideos japonesa muy popular..."),
        Food("Churrasco", "Brasil", R.drawable.churrasco, "El churrasco es un tipo de carne asada típico en Brasil..."),
        Food("Poutine", "Canadá", R.drawable.poutin, "La poutine es un platillo canadiense que consiste en papas fritas con queso y salsa..."),
        Food("Shawarma", "Medio Oriente", R.drawable.shawarma, "El shawarma es un platillo de carne asada en un pan plano..."),
        Food("Goulash", "Hungría", R.drawable.goulash, "El goulash es un estofado húngaro de carne y vegetales..."),
        Food("Ceviche", "Perú", R.drawable.ceviche, "El ceviche es un plato de pescado marinado típico de Perú..."),
        Food("Baklava", "Turquía", R.drawable.baklava, "La baklava es un postre turco hecho con nueces y jarabe..."),
        Food("Samosa", "India", R.drawable.samosa, "La samosa es un aperitivo indio relleno de especias y verduras..."),
        Food("Pavlova", "Australia", R.drawable.pavlova, "La pavlova es un postre de merengue con frutas frescas..."),
        Food("Borscht", "Rusia", R.drawable.borscht, "El borscht es una sopa de remolacha típica de Rusia...")
    )

    var selectedFood by remember { mutableStateOf<Food?>(null) }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(foodList) { food ->
            FoodItem(food) { selectedFood = food }
        }
    }

    // Mostrar diálogo cuando se selecciona un ítem
    selectedFood?.let {
        FoodDetailDialog(food = it, onDismiss = { selectedFood = null })
    }
}

@Composable
fun FoodItem(food: Food, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = food.imageResId),
            contentDescription = food.name,
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = food.name, style = MaterialTheme.typography.titleMedium)
            Text(text = food.origin, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun FoodDetailDialog(food: Food, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.White,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = food.imageResId),
                    contentDescription = food.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = food.name, style = MaterialTheme.typography.titleLarge)
                Text(text = food.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                // Asegúrate de usar LocalContext aquí
                val context = LocalContext.current
                Button(onClick = { shareFoodInfo(context, food) }) {
                    Text("Compartir")
                }
            }
        }
    }
}

// Cambié la declaración para que no sea un Composable
fun shareFoodInfo(context: Context, food: Food) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "${food.name} de ${food.origin}: ${food.description}")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartir con"))
}

@Preview(showBackground = true)
@Composable
fun FoodListPreview() {
    Tarea3Theme {
        FoodList()
    }
}

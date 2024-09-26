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
        Food("Tacos", "México", R.drawable.tacos, "Los tacos son una comida tradicional mexicana..."),
        Food("Sushi", "Japón", R.drawable.sushi, "El sushi es un platillo japonés..."),
        Food("Paella", "España", R.drawable.paella, "La paella es una comida española..."),
        Food("Pasta", "Italia", R.drawable.pasta, "La pasta es un alimento básico en la cocina italiana..."),
        Food("Curry", "India", R.drawable.curry, "El curry es un plato característico de la cocina india..."),
        Food("Biryani", "Pakistán", R.drawable.biryani, "El biryani es un plato de arroz especiado típico de Pakistán..."),
        Food("Dim Sum", "China", R.drawable.dismun, "El dim sum es una variedad de platillos chinos en porciones pequeñas..."),
        Food("Falafel", "Medio Oriente", R.drawable.falafel, "El falafel es un plato vegetariano popular en la cocina del Medio Oriente..."),
        Food("Kimchi", "Corea del Sur", R.drawable.kimchi, "El kimchi es un plato fermentado hecho principalmente de repollo..."),
        Food("Moussaka", "Grecia", R.drawable.moussaka, "La moussaka es un plato tradicional griego que incluye carne y berenjenas...")
        // Añade más platillos según sea necesario
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

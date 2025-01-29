import java.util.*

// CLASE CLIENTE
data class Cliente(
    val nombre: String,
    val direccion: String,
    val email: String,
    val telefono: String,
    val pedidos: MutableList<Pedido> = mutableListOf()
)

// CLASE PEDIDO
class Pedido(
    val fecha: Date = Date(), // Valor por defecto
    var estado: String = "pdte", // Estado inicial
    val productos: MutableList<Producto> = mutableListOf(),
    val pagos: MutableList<Pago> = mutableListOf()
) {
    fun calcularCostoTotal(): Float {
        return productos.sumOf {
            (it.precio * it.impuestos).toDouble()
        }.toFloat() // Conversión explícita
    }

    fun actualizarEstado(nuevoEstado: String) {
        require(nuevoEstado in listOf("pdte", "pgdo", "pcdo", "envdo", "entgdo")) {
            "Estado no válido"
        }
        estado = nuevoEstado
    }
}

// CLASE PRODUCTO
data class Producto(
    val nombre: String,
    val descripcion: String,
    val precio: Float,
    val impuestos: Float,
    var stock: Int
) {
    fun actualizarStock(cantidad: Int) {
        require(cantidad > 0) { "La cantidad debe ser positiva" }
        require(stock >= cantidad) { "Stock insuficiente" }
        stock -= cantidad
    }
}

// CLASE PAGO (ABSTRACT)
abstract class Pago(
    val fecha: Date,
    val importe: Float
)

// SUBCLASES DE PAGO
class Efectivo(
    importe: Float,
    val tipoMoneda: String
) : Pago(Date(), importe) // Fecha automática

class Cheque(
    importe: Float,
    val nombre: String,
    val banco: String
) : Pago(Date(), importe)

class Card(
    importe: Float,
    val numero: String,
    val fechaCaducidad: Date,
    val tipoTarjeta: String
) : Pago(Date(), importe)

// EJEMPLO DE USO CORRECTO
fun main() {
    val producto1 = Producto(
        "Laptop",
        "i7 16GB RAM",
        999.99f,
        1.16f, // Impuesto 16%
        10
    )

    val pedido = Pedido().apply {
        productos.add(producto1)
        pagos.add(Efectivo(1200f, "USD"))
    }

    println("Costo total: ${pedido.calcularCostoTotal()}")
    producto1.actualizarStock(1)
}

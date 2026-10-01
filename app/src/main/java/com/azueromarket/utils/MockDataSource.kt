package com.azueromarket.utils

import com.azueromarket.model.*

/**
 * MockDataSource – datos locales para demo sin backend real.
 * Cuando conectes Supabase, este objeto se reemplaza por los
 * repositorios que llaman a la base de datos.
 */
object MockDataSource {

    // ─── Usuarios ────────────────────────────────────────────
    val usuarios = mutableMapOf(
        "emprendedor@test.com" to Pair("123456",
            Usuario("emp_001","María Rodríguez","emprendedor@test.com",
                TipoUsuario.EMPRENDEDOR,"+507 6123-4567","Las Tablas, Los Santos",
                "Artesana con 15 años de experiencia en sombreros pintados y polleras")),
        "emprendedor2@test.com" to Pair("123456",
            Usuario("emp_002","Don Pedro Castillo","emprendedor2@test.com",
                TipoUsuario.EMPRENDEDOR,"+507 6234-5678","Macaracas, Los Santos",
                "Apicultor familiar desde hace 20 años. Miel 100% natural de Azuero")),
        "emprendedor3@test.com" to Pair("123456",
            Usuario("emp_003","Familia Herrera","emprendedor3@test.com",
                TipoUsuario.EMPRENDEDOR,"+507 6345-6789","Ocú, Herrera",
                "Productores de queso y embutidos artesanales desde 1975")),
        "cliente@test.com" to Pair("123456",
            Usuario("cli_001","Carlos Méndez","cliente@test.com",
                TipoUsuario.CLIENTE,"+507 6987-6543","Ciudad de Panamá"))
    )

    // ─── Productos ───────────────────────────────────────────
    val productos = mutableListOf(
        Producto("prod_001","Sombrero Pintado de Azuero",
            "Sombrero pintado artesanal, hecho a mano con fibra de bellota. Diseño tradicional de Los Santos. Proceso de elaboración de más de 3 semanas.",
            85.00, CategoriaProducto.ARTESANIAS,
            "emp_001","María Rodríguez","+507 6123-4567","Las Tablas, Los Santos","",stock=5),

        Producto("prod_002","Miel de Abeja Pura 500ml",
            "Miel artesanal 100% natural, sin conservantes ni aditivos. Cosechada en las montañas de Macaracas a más de 600m de altura.",
            12.50, CategoriaProducto.MIEL_DULCES,
            "emp_002","Don Pedro Castillo","+507 6234-5678","Macaracas, Los Santos","",stock=20),

        Producto("prod_003","Queso Blanco de Ocú",
            "Queso fresco artesanal elaborado con leche de vaca pasteurizada. Producto típico de la región de Ocú, ideal para el desayuno.",
            8.00, CategoriaProducto.QUESOS_EMBUTIDOS,
            "emp_003","Familia Herrera","+507 6345-6789","Ocú, Herrera","",stock=15),

        Producto("prod_004","Pollera Bordada Miniatura",
            "Réplica artesanal de pollera panameña, bordada completamente a mano. Perfecta como souvenir o pieza decorativa. Colores tradicionales de Azuero.",
            45.00, CategoriaProducto.TEXTILES,
            "emp_001","María Rodríguez","+507 6123-4567","Las Tablas, Los Santos","",stock=3),

        Producto("prod_005","Dulce de Nance Casero",
            "Dulce tradicional de nance en almíbar, receta familiar transmitida por generaciones. Sin preservantes. Frasco de 250g.",
            5.00, CategoriaProducto.MIEL_DULCES,
            "emp_002","Don Pedro Castillo","+507 6234-5678","Chitré, Herrera","",stock=0),

        Producto("prod_006","Cerámica Pintada Típica",
            "Jarrón de cerámica pintado a mano con motivos folclóricos de la región de Azuero. Pieza única, firmada por el artesano.",
            30.00, CategoriaProducto.CERAMICA,
            "emp_003","Familia Herrera","+507 6345-6789","Parita, Herrera","",stock=2),

        Producto("prod_007","Miel de Abeja con Jengibre",
            "Combinación artesanal de miel pura con jengibre natural. Excelente para la salud y el sistema inmune. Frasco 350ml.",
            15.00, CategoriaProducto.MIEL_DULCES,
            "emp_002","Don Pedro Castillo","+507 6234-5678","Macaracas, Los Santos","",stock=10),

        Producto("prod_008","Chorizos Artesanales Herrera",
            "Chorizos elaborados con especias tradicionales de Azuero. Curados naturalmente. Paquete de 500g.",
            14.00, CategoriaProducto.QUESOS_EMBUTIDOS,
            "emp_003","Familia Herrera","+507 6345-6789","Ocú, Herrera","",stock=8),

        Producto("prod_009","Tejido a Crochet Flor",
            "Manteles y caminos de mesa tejidos a crochet con diseños de flores típicas de Azuero. Hecho a mano, 100% algodón.",
            22.00, CategoriaProducto.TEXTILES,
            "emp_001","María Rodríguez","+507 6123-4567","Las Tablas, Los Santos","",stock=7)
    )

    // ─── Conversaciones demo ─────────────────────────────────
    val conversaciones = mutableListOf(
        Conversacion("conv_001","emp_001","María Rodríguez",
            "El sombrero está disponible, ¿cuántos necesita?",
            System.currentTimeMillis() - 3600000, 2,
            "prod_001","Sombrero Pintado de Azuero"),
        Conversacion("conv_002","emp_002","Don Pedro Castillo",
            "Claro, puede pedir hasta 10 frascos con envío.",
            System.currentTimeMillis() - 86400000, 0,
            "prod_002","Miel de Abeja Pura")
    )

    // ─── Mensajes demo ───────────────────────────────────────
    val mensajesPorConversacion = mutableMapOf(
        "conv_001" to mutableListOf(
            Mensaje("m1","conv_001","cli_001","Hola, estoy interesado en el sombrero pintado",
                System.currentTimeMillis()-7200000,true),
            Mensaje("m2","conv_001","emp_001","¡Hola! Claro que sí, tengo 5 disponibles. ¿Cuántos necesita?",
                System.currentTimeMillis()-3600000,false),
            Mensaje("m3","conv_001","cli_001","Necesito uno solo, ¿tiene envío a Panamá ciudad?",
                System.currentTimeMillis()-1800000,true),
            Mensaje("m4","conv_001","emp_001","Sí, enviamos a todo el país por $5.00 adicionales.",
                System.currentTimeMillis()-900000,false)
        ),
        "conv_002" to mutableListOf(
            Mensaje("m5","conv_002","cli_001","Buenos días, ¿la miel es 100% natural?",
                System.currentTimeMillis()-90000000,true),
            Mensaje("m6","conv_002","emp_002","¡Buenos días! Sí, completamente natural, sin preservantes.",
                System.currentTimeMillis()-86400000,false)
        )
    )

    // ─── Métodos de acceso ───────────────────────────────────
    fun autenticar(email: String, password: String): Usuario? {
        val entry = usuarios[email.lowercase().trim()] ?: return null
        return if (entry.first == password) entry.second else null
    }

    fun registrarUsuario(nombre: String, email: String, password: String,
        tipoUsuario: TipoUsuario, telefono: String, ubicacion: String): Result<Usuario> {
        if (usuarios.containsKey(email.lowercase().trim()))
            return Result.failure(Exception("Este correo ya está registrado"))
        val nuevo = Usuario("usr_${System.currentTimeMillis()}", nombre, email,
            tipoUsuario, telefono, ubicacion)
        usuarios[email.lowercase().trim()] = Pair(password, nuevo)
        return Result.success(nuevo)
    }

    fun getTodosProductos(): List<Producto> = productos.toList()

    fun getProductosByEmprendedor(id: String) = productos.filter { it.emprendedorId == id }

    fun getProductosByCategoria(cat: CategoriaProducto) = productos.filter { it.categoria == cat }

    fun getProductosRelacionados(producto: Producto): List<Producto> =
        productos.filter { it.emprendedorId == producto.emprendedorId && it.id != producto.id }.take(4)

    fun getUsuario(id: String): Usuario? =
        usuarios.values.firstOrNull { it.second.id == id }?.second

    fun getConversacionesCliente(clienteId: String) = conversaciones.toList()

    fun getMensajes(convId: String) = mensajesPorConversacion[convId]?.toList() ?: emptyList()

    fun enviarMensaje(convId: String, remitenteId: String, contenido: String) {
        val msg = Mensaje(
            id = "msg_${System.currentTimeMillis()}",
            conversacionId = convId,
            remitenteId = remitenteId,
            contenido = contenido,
            timestamp = System.currentTimeMillis(),
            esMio = true
        )
        mensajesPorConversacion.getOrPut(convId) { mutableListOf() }.add(msg)
        conversaciones.find { it.id == convId }?.let { conv ->
            val idx = conversaciones.indexOf(conv)
            conversaciones[idx] = conv.copy(ultimoMensaje = contenido, timestamp = msg.timestamp)
        }
    }
}

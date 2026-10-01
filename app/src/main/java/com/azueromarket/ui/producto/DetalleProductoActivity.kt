package com.azueromarket.ui.producto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azueromarket.AzueroMarketApp
import com.azueromarket.databinding.ActivityDetalleProductoBinding
import com.azueromarket.model.Producto
import com.azueromarket.ui.chat.ChatActivity
import com.azueromarket.ui.cliente.ProductoRelacionadoAdapter
import com.azueromarket.utils.CarritoManager
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class DetalleProductoActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUCTO = "extra_producto"
    }

    private lateinit var binding: ActivityDetalleProductoBinding
    private lateinit var producto: Producto
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        producto = intent.getParcelableExtra(EXTRA_PRODUCTO)!!

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupProductoInfo()
        setupBotones()
        setupProductosRelacionados()
    }

    private fun setupProductoInfo() {
        binding.tvNombreProducto.text   = producto.nombre
        binding.tvCategoriaChip.text    = "${producto.categoria.emoji} ${producto.categoria.nombreMostrar}"
        binding.tvDescripcion.text      = producto.descripcion
        binding.tvEmprendedor.text      = "Vendido por ${producto.emprendedorNombre}"
        binding.tvUbicacion.text        = "📍 ${producto.ubicacion}"

        // Precio
        binding.tvPrecioProducto.text = "$ %.2f".format(producto.precio)

        // Desglose de cargos
        val cargoServ = producto.precio * AzueroMarketApp.CARGO_SERVICIO_PORCENT
        val baseItbms = producto.precio + cargoServ
        val itbms     = baseItbms * AzueroMarketApp.ITBMS_PORCENT
        val totalUnit = producto.precio + cargoServ + itbms

        binding.tvCargoServicio.text = "+ $ %.2f cargo de servicio (5%%)".format(cargoServ)
        binding.tvItbms.text         = "+ $ %.2f ITBMS (7%%)".format(itbms)
        binding.tvTotalConCargos.text = "Total: $ %.2f".format(totalUnit)

        // Emoji placeholder imagen
        binding.tvImagenProducto.text = producto.categoria.emoji

        // Stock
        setupStockIndicator()
    }

    private fun setupStockIndicator() {
        when {
            producto.stock > 5 -> {
                binding.tvStock.text = "✅ En stock (${producto.stock} disponibles)"
                binding.tvStock.setTextColor(getColor(android.R.color.holo_green_dark))
                binding.btnSolicitarProducto.isEnabled = true
                binding.btnSolicitarProducto.text = "Agregar al Carrito"
            }
            producto.stock in 1..5 -> {
                binding.tvStock.text = "⚠️ Últimas ${producto.stock} unidades"
                binding.tvStock.setTextColor(getColor(android.R.color.holo_orange_dark))
                binding.btnSolicitarProducto.isEnabled = true
                binding.btnSolicitarProducto.text = "Agregar al Carrito"
            }
            else -> {
                binding.tvStock.text = "❌ Sin stock — Consulta al productor"
                binding.tvStock.setTextColor(getColor(android.R.color.holo_red_dark))
                binding.btnSolicitarProducto.isEnabled = false
                binding.btnSolicitarProducto.text = "Sin Stock"
                binding.btnContactarProductor.text = "💬 Preguntar al Productor"
            }
        }
    }

    private fun setupBotones() {
        // Botón principal: agregar al carrito (solo si hay stock)
        binding.btnSolicitarProducto.setOnClickListener {
            CarritoManager.agregarProducto(producto)
            Snackbar.make(binding.root, "¡${producto.nombre} agregado al carrito!", Snackbar.LENGTH_LONG)
                .setAction("Ver Carrito") {
                    finish() // regresa al home que tiene la nav
                }.show()
        }

        // Botón chat con productor
        binding.btnContactarProductor.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra(ChatActivity.EXTRA_CONV_ID,          "conv_${producto.emprendedorId}_${session.getUserId()}")
                putExtra(ChatActivity.EXTRA_OTRO_USUARIO_ID,   producto.emprendedorId)
                putExtra(ChatActivity.EXTRA_OTRO_USUARIO_NOMBRE, producto.emprendedorNombre)
                putExtra(ChatActivity.EXTRA_PRODUCTO_ID,       producto.id)
                putExtra(ChatActivity.EXTRA_PRODUCTO_NOMBRE,   producto.nombre)
            }
            startActivity(intent)
        }
    }

    private fun setupProductosRelacionados() {
        val relacionados = MockDataSource.getProductosRelacionados(producto)
        if (relacionados.isEmpty()) {
            binding.tvRelacionadosTitle.visibility = View.GONE
            return
        }
        binding.tvRelacionadosTitle.text = "Más de ${producto.emprendedorNombre.split(" ").first()}"
        val adapterRel = ProductoRelacionadoAdapter { prod ->
            // Navegar a otro producto
            val intent = Intent(this, DetalleProductoActivity::class.java)
            intent.putExtra(EXTRA_PRODUCTO, prod)
            startActivity(intent)
            finish()
        }
        binding.rvProductosRelacionados.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProductosRelacionados.adapter = adapterRel
        adapterRel.submitList(relacionados)
    }
}

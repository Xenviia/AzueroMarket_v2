package com.azueromarket.ui.carrito

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azueromarket.databinding.ActivityCarritoBinding
import com.azueromarket.utils.CarritoManager
import com.azueromarket.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true); title = "Mi Carrito" }
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        setupBotones()
        refreshCarrito()
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(
            onCantidadChange = { item, delta ->
                CarritoManager.actualizarCantidad(item.producto.id, item.cantidad + delta)
                refreshCarrito()
            },
            onEliminar = { item ->
                CarritoManager.quitarProducto(item.producto.id)
                refreshCarrito()
                Snackbar.make(binding.root, "Producto eliminado", Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = adapter
    }

    private fun setupBotones() {
        binding.btnConfirmarPedido.setOnClickListener {
            showConfirmarPedidoDialog()
        }
    }

    private fun refreshCarrito() {
        if (CarritoManager.isEmpty()) {
            binding.layoutCarritoVacio.visibility = View.VISIBLE
            binding.layoutCarritoContenido.visibility = View.GONE
            return
        }
        binding.layoutCarritoVacio.visibility = View.GONE
        binding.layoutCarritoContenido.visibility = View.VISIBLE

        val resumen = CarritoManager.calcularResumen()
        adapter.submitList(resumen.items)

        binding.tvSubtotal.text      = "$ %.2f".format(resumen.subtotal)
        binding.tvCargoServicio.text = "+ $ %.2f".format(resumen.cargoServicio)
        binding.tvItbms.text         = "+ $ %.2f".format(resumen.itbms)
        binding.tvTotal.text         = "$ %.2f".format(resumen.total)
    }

    private fun showConfirmarPedidoDialog() {
        val resumen = CarritoManager.calcularResumen()
        val productosTexto = resumen.items.joinToString("\n") {
            "  • ${it.producto.nombre} x${it.cantidad} — ${"$ %.2f".format(it.subtotal)}"
        }
        AlertDialog.Builder(this)
            .setTitle("Confirmar Pedido")
            .setMessage(
                "$productosTexto\n\n" +
                "Subtotal:         ${"$ %.2f".format(resumen.subtotal)}\n" +
                "Cargo de servicio: ${"$ %.2f".format(resumen.cargoServicio)}\n" +
                "ITBMS (7%%):       ${"$ %.2f".format(resumen.itbms)}\n" +
                "─────────────────────\n" +
                "TOTAL:            ${"$ %.2f".format(resumen.total)}\n\n" +
                "Los productores serán notificados de tu pedido."
            )
            .setPositiveButton("Confirmar") { _, _ ->
                // Aquí irá la llamada a Supabase para crear el pedido
                CarritoManager.limpiarCarrito()
                Snackbar.make(binding.root, "✅ ¡Pedido enviado! Los productores te contactarán pronto.", Snackbar.LENGTH_LONG).show()
                refreshCarrito()
            }
            .setNegativeButton("Revisar", null)
            .show()
    }
}

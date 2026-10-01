package com.azueromarket.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.azueromarket.R
import com.azueromarket.databinding.ActivityHomeClienteBinding
import com.azueromarket.model.CategoriaProducto
import com.azueromarket.ui.carrito.CarritoActivity
import com.azueromarket.ui.chat.ConversacionesActivity
import com.azueromarket.ui.cliente.ProductoClienteAdapter
import com.azueromarket.ui.login.LoginActivity
import com.azueromarket.ui.producto.DetalleProductoActivity
import com.azueromarket.utils.CarritoManager
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class HomeClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeClienteBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: ProductoClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "AzueroMarket"

        setupWelcome()
        setupCategoriaChips()
        setupRecyclerView()
        setupBottomNav()
        loadAllProductos()
    }

    override fun onResume() {
        super.onResume()
        // Actualizar badge del carrito
        updateCartBadge()
    }

    private fun setupWelcome() {
        val nombre = session.getUserName().split(" ").firstOrNull() ?: "Cliente"
        binding.tvWelcome.text  = "Hola, $nombre 👋"
        binding.tvSubtitle.text = "Descubre lo mejor de Azuero"
    }

    private fun setupCategoriaChips() {
        val chipTodos = Chip(this).apply {
            text = "Todos"; isCheckable = true; isChecked = true
        }
        chipTodos.setOnCheckedChangeListener { _, isChecked -> if (isChecked) loadAllProductos() }
        binding.chipGroupCategorias.addView(chipTodos)

        CategoriaProducto.values().forEach { cat ->
            val chip = Chip(this).apply {
                text = "${cat.emoji} ${cat.nombreMostrar}"; isCheckable = true
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val filtered = MockDataSource.getProductosByCategoria(cat)
                    adapter.submitList(filtered)
                    binding.tvResultCount.text = "${filtered.size} productos"
                }
            }
            binding.chipGroupCategorias.addView(chip)
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductoClienteAdapter { producto ->
            val intent = Intent(this, DetalleProductoActivity::class.java)
            intent.putExtra(DetalleProductoActivity.EXTRA_PRODUCTO, producto)
            startActivity(intent)
        }
        binding.rvProductos.layoutManager = GridLayoutManager(this, 2)
        binding.rvProductos.adapter = adapter
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tienda -> true  // ya estamos aquí
                R.id.nav_chat   -> {
                    startActivity(Intent(this, ConversacionesActivity::class.java))
                    false
                }
                R.id.nav_carrito -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    private fun loadAllProductos() {
        val productos = MockDataSource.getTodosProductos()
        adapter.submitList(productos)
        binding.tvResultCount.text = "${productos.size} productos disponibles"
    }

    private fun updateCartBadge() {
        val count = CarritoManager.getConteo()
        val badgeItem = binding.bottomNav.getOrCreateBadge(R.id.nav_carrito)
        if (count > 0) {
            badgeItem.isVisible = true
            badgeItem.number   = count
        } else {
            badgeItem.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logout -> { showLogoutConfirm(); true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showLogoutConfirm() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Deseas cerrar tu sesión?")
            .setPositiveButton("Sí") { _, _ ->
                session.clearSession()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }); finish()
            }
            .setNegativeButton("No", null).show()
    }
}

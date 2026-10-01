package com.azueromarket.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azueromarket.R
import com.azueromarket.databinding.ActivityHomeEmprendedorBinding
import com.azueromarket.model.Producto
import com.azueromarket.ui.chat.ConversacionesActivity
import com.azueromarket.ui.emprendedor.AgregarProductoDialog
import com.azueromarket.ui.emprendedor.ProductoEmprendedorAdapter
import com.azueromarket.ui.login.LoginActivity
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class HomeEmprendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeEmprendedorBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: ProductoEmprendedorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeEmprendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Mi Tienda"

        setupWelcome()
        setupRecyclerView()
        setupBottomNav()
        binding.fabAgregar.setOnClickListener { showAgregarProducto() }
        loadProductos()
    }

    private fun setupWelcome() {
        val nombre = session.getUserName().split(" ").firstOrNull() ?: "Emprendedor"
        binding.tvWelcome.text    = "Hola, $nombre 👋"
        binding.tvUbicacion.text  = "📍 ${session.getUserLocation()}"
    }

    private fun setupRecyclerView() {
        adapter = ProductoEmprendedorAdapter(
            onEditClick   = { showEditProducto(it) },
            onDeleteClick = { showDeleteConfirm(it) }
        )
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter
    }

    private fun setupBottomNav() {
        binding.bottomNavEmp.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tienda_emp -> true
                R.id.nav_chat_emp   -> {
                    startActivity(Intent(this, ConversacionesActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    private fun showAgregarProducto() {
        AgregarProductoDialog.show(this,
            emprendedorId     = session.getUserId(),
            emprendedorNombre = session.getUserName(),
            emprendedorTel    = session.getUserPhone()
        ) {
            loadProductos()
            Snackbar.make(binding.root, "¡Producto publicado con éxito!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun loadProductos() {
        val productos = MockDataSource.getProductosByEmprendedor(session.getUserId())
        adapter.submitList(productos)
        binding.tvProductCount.text = "${productos.size} productos publicados"
        binding.tvTotalVentas.text  = "$ %.2f este mes".format(productos.size * 18.0)
    }

    private fun showEditProducto(producto: Producto) {
        Snackbar.make(binding.root, "Editar: ${producto.nombre} (próximamente)", Snackbar.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirm(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Producto")
            .setMessage("¿Seguro que deseas eliminar '${producto.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                MockDataSource.productos.removeIf { it.id == producto.id }
                loadProductos()
                Snackbar.make(binding.root, "Producto eliminado", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu); return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logout -> { showLogoutConfirm(); true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showLogoutConfirm() {
        AlertDialog.Builder(this).setTitle("Cerrar Sesión")
            .setMessage("¿Deseas cerrar tu sesión?")
            .setPositiveButton("Sí") { _, _ ->
                session.clearSession()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }); finish()
            }.setNegativeButton("No", null).show()
    }
}

package br.com.marchiro.supertrainingapp.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.marchiro.supertrainingapp.data.local.AppDatabase
import br.com.marchiro.supertrainingapp.databinding.ActivityMainBinding
import br.com.marchiro.supertrainingapp.ui.adapters.MainRvAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dao by lazy {
        AppDatabase.instance(this).trainDao()
    }

    private val adapter = MainRvAdapter(this) { id ->
        Intent(this, TrainDetailsActivity::class.java).apply {
            putExtra(TRAIN_ID_KEY, id)
            startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainRv.adapter = adapter
        configFab()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            adapter.update(dao.getAllTrain())
        }
    }

    private fun configFab() {
        binding.mainFab.setOnClickListener {
            startActivity(Intent(this, TrainFormActivity::class.java))
        }
    }
}
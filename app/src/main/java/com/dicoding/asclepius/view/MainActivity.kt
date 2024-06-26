package com.dicoding.asclepius.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.view.history.HistoryFragment
import com.dicoding.asclepius.view.main.MainFragment
import com.dicoding.asclepius.view.news.NewsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    replaceFragment(MainFragment())
                    true
                }
                R.id.page_2 -> {
                    replaceFragment(HistoryFragment())
                    showToast("History Page")
                    true
                }
                R.id.page_3 -> {
                    replaceFragment(NewsFragment())
                    showToast("News Page")
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.page_1
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frContainer.id, fragment)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RESULT_KEY = "result"
    }
}
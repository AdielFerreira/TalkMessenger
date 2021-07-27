package br.com.talkmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.talkmessenger.adapters.ViewPagerAdapter
import br.com.talkmessenger.databinding.ActivityIntroBinding
import br.com.talkmessenger.databinding.ActivityMainBinding
import br.com.talkmessenger.ui.VerifyPhoneActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabs,binding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy{ tabs, pos->
                when(pos){
                    0 -> tabs.text = "Chats"
                    1 -> tabs.text = "Users"
                }
        }).attach()
    }
}
package uz.iskandarbek.shifrlovchi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import uz.iskandarbek.shifrlovchi.activitys.DeCryptoActivit
import uz.iskandarbek.shifrlovchi.activitys.EncryptoActivit
import uz.iskandarbek.shifrlovchi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            encrypto.setOnClickListener {
                val intent = Intent(this@MainActivity, EncryptoActivit::class.java)
                startActivity(intent)
            }
            decrypto.setOnClickListener {
                val intent = Intent(this@MainActivity, DeCryptoActivit::class.java)
                startActivity(intent)
            }
        }
    }
}
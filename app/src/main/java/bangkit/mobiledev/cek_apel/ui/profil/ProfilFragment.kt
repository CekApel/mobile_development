package bangkit.mobiledev.cek_apel.ui.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.databinding.FragmentProfilBinding
import bangkit.mobiledev.cek_apel.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profilViewModel =
            ViewModelProvider(this).get(ProfilViewModel::class.java)

        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Tampilkan teks profil
        val textView: TextView = binding.textProfil
        profilViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Menampilkan email pengguna yang sedang login
        val emailTextView: TextView = binding.userEmail // Sesuaikan dengan ID TextView di XML
        val currentUser = auth.currentUser
        if (currentUser != null) {
            emailTextView.text = "Email: ${currentUser.email}"
        } else {
            emailTextView.text = "Tidak ada pengguna yang login"
        }

        // Setup button logout
        val logoutButton: Button = binding.logoutButton
        logoutButton.setOnClickListener {
            // Logout user
            auth.signOut()
            // Arahkan pengguna ke LoginActivity setelah logout
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish() // Menutup aktivitas ProfilFragment agar tidak bisa kembali
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

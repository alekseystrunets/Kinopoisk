import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentLoginBinding
import com.example.kinopoisk.presentation.ui.fragment.HomeFragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        viewModel.publicLiveDataForFields.observe(viewLifecycleOwner) { errors ->
            if (errors != null) {
                binding.regEmailEditText.error = errors.getString("emailError")
                binding.regPasswordEditText.error = errors.getString("passwordError")
            } else {
                binding.regEmailEditText.error = null
                binding.regPasswordEditText.error = null
            }
        }

        binding.buttonEnter.setOnClickListener {
            val email = binding.regEmailEditText.text.toString()
            val password = binding.regPasswordEditText.text.toString()

            viewModel.validateInputs(email, password)
            if (viewModel.publicLiveDataForFields.value == null) {
                viewModel.loginUser(email, password,
                    onSuccess = { userEmail ->
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                        toHomeScreen(userEmail)
                    },
                    onError = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        binding.buttonReg.setOnClickListener {
            toRegistrationScreen()
        }
    }

    private fun toHomeScreen(userEmail: String) {
        // Сохраняем email в SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_email", userEmail).apply()
        Log.d("LoginFragment", "Email saved to SharedPreferences: $userEmail")

        // Переход на HomeFragment
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun toRegistrationScreen() {
        val registrationFragment = RegistrationFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, registrationFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
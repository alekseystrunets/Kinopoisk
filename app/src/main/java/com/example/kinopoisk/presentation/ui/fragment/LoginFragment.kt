package com.example.kinopoisk.presentation.ui.fragment

import HomeFragment
import LoginFragmentViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoisk.R
import com.example.kinopoisk.data.db.SharedPreferences
import com.example.kinopoisk.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private  var viewModel: LoginFragmentViewModel? = null
    private var sharedPreferences : SharedPreferences? = null


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

         val sharedPreferences = SharedPreferences(requireContext())
        // Наблюдаем за ошибками валидации
        viewModel?.publicLiveDataForFields?.observe(viewLifecycleOwner) { errors ->
            if (errors != null) {
                binding.regEmailEditText.error = errors.getString("emailError")
                binding.regLoginEditText.error = errors.getString("loginError")
                binding.regPasswordEditText.error = errors.getString("passwordError")
            } else {
                binding.regEmailEditText.error = null
                binding.regLoginEditText.error = null
                binding.regPasswordEditText.error = null
            }
        }

        // Регистрация
        binding.buttonReg.setOnClickListener {
            val email = binding.regEmailEditText.text.toString()
            val login = binding.regLoginEditText.text.toString()
            val password = binding.regPasswordEditText.text.toString()

            // Валидируем поля перед регистрацией (логин проверяем)
            viewModel?.validateInputs(email, login, password, validateLogin = true)
            if (viewModel?.publicLiveDataForFields?.value == null) {
                viewModel?.registerUser(email, login, password,
                    onSuccess = { isFirstLogin ->
                        val message = if (isFirstLogin) {
                            "Registration successful!"
                        } else {
                            "Welcome back!"
                        }
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        sharedPreferences.saveUserEmail(email)
                        toNextScreen()
                    },
                    onError = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Вход
        binding.buttonEnter.setOnClickListener {
            val email = binding.regEmailEditText.text.toString()
            val password = binding.regPasswordEditText.text.toString()

            // Валидируем поля перед входом (логин не проверяем)
            viewModel?.validateInputs(email, "", password, validateLogin = false)
            if (viewModel?.publicLiveDataForFields?.value == null) {
                viewModel?.loginUser(email, password,
                    onSuccess = { isFirstLogin ->
                        val message = if (isFirstLogin) {
                            "Registration successful!"
                        } else {
                            "Welcome back!"
                        }
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        sharedPreferences.saveUserEmail(email)
                        toNextScreen()
                    },
                    onError = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun toNextScreen() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
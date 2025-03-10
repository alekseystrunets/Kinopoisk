package com.example.kinopoisk.presentation.ui.fragment

import HomeFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentLoginBinding
import com.example.kinopoisk.presentation.view_model.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подписываемся на ошибки валидации
        viewModel.publicLiveDataForFields.observe(viewLifecycleOwner) { errors ->
            if (errors != null) {
                binding.regEmailEditText.error = errors.getString("emailError")
                binding.regPasswordEditText.error = errors.getString("passwordError")
            } else {
                binding.regEmailEditText.error = null
                binding.regPasswordEditText.error = null
            }
        }

        // Подписываемся на сообщения Toast
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()  // Сбрасываем сообщение после показа
            }
        }

        // Обработчик кнопки "Войти"
        binding.buttonEnter.setOnClickListener {
            val email = binding.regEmailEditText.text.toString()
            val password = binding.regPasswordEditText.text.toString()

            if (viewModel.validateInputs(email, password)) {
                viewModel.loginUser(
                    email,
                    password,
                    onSuccess = { userEmail ->
                        toHomeScreen(userEmail)
                    },
                    onError = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Обработчик кнопки "Регистрация"
        binding.buttonReg.setOnClickListener {
            toRegistrationScreen()
        }
    }

    // Переход на HomeFragment
    private fun toHomeScreen(userEmail: String) {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }

    // Переход на RegistrationFragment
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
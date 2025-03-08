import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentUserAccountBinding
import com.example.kinopoisk.presentation.fragments.FavoritesFragment

class UserAccountFragment : Fragment() {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserAccountViewModel::class.java)

        // Получаем email из SharedPreferences через ViewModel
        val userEmail = viewModel.getUserEmail()
        Log.d("UserAccountFragment", "Email retrieved from SharedPreferences: $userEmail")

        if (userEmail != null) {
            viewModel.loadUserData(userEmail)
        } else {
            binding.userAccountLogin.text = getString(R.string.user_not_found)
            binding.emailAccount.text = getString(R.string.email_not_found)
        }

        // Подписываемся на данные пользователя
        viewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.userAccountLogin.text = user.login
                binding.emailAccount.text = user.email
            } else {
                binding.userAccountLogin.text = getString(R.string.login_not_found)
                binding.emailAccount.text = getString(R.string.email_not_found)
            }
        }

        // Обработчики нажатий на кнопки
        binding.buttonHome.setOnClickListener {
            toHomeScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            toFavoritesScreen()
        }
    }

    // Переход на HomeFragment
    private fun toHomeScreen() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }

    // Переход на FavoritesFragment
    private fun toFavoritesScreen() {
        val favoritesFragment = FavoritesFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, favoritesFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
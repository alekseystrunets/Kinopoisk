import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentAdditionalPageFilmBinding
import com.example.kinopoisk.presentation.view_model.AdditionalPageFilmViewModel

class AdditionalPageFilmFragment : Fragment() {

    private var _binding: FragmentAdditionalPageFilmBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AdditionalPageFilmViewModel

    companion object {
        private const val ARG_DESCRIPTION = "description"

        fun newInstance(description: String): AdditionalPageFilmFragment {
            val args = Bundle().apply {
                putString(ARG_DESCRIPTION, description)
            }
            val fragment = AdditionalPageFilmFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdditionalPageFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdditionalPageFilmViewModel::class.java)

        // Устанавливаем описание в ViewModel
        val description = arguments?.getString(ARG_DESCRIPTION) ?: "No description available"
        viewModel.setDescription(description)

        // Подписываемся на изменения данных в ViewModel
        viewModel.description.observe(viewLifecycleOwner) { description ->
            binding.textForAdditionalPage.text = description
        }

        // Обработчики нажатий на кнопки
        binding.arrowBack.setOnClickListener {
            viewModel.navigateBack()
            back()
        }

        binding.buttonHome.setOnClickListener {
            viewModel.navigateToHome()
            toHomeScreen()
        }

        binding.buttonAccount.setOnClickListener {
            viewModel.navigateToAccount()
            toAccountScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            viewModel.navigateToFavorites()
            toFavoritesScreen()
        }
    }

    // Методы навигации
    private fun toHomeScreen() {
        val homeScreen = HomeFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, homeScreen)
            .addToBackStack(null).commit()
    }

    private fun toAccountScreen() {
        val accountFragment = UserAccountFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment)
            .addToBackStack(null).commit()
    }

    private fun toFavoritesScreen() {
        val favoritesFragment = FavoritesFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, favoritesFragment)
            .addToBackStack(null).commit()
    }

    private fun back() {
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
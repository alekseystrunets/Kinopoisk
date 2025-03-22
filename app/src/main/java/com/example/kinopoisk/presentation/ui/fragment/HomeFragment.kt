import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentHomeBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.adapter.CategoriesAdapter
import com.example.kinopoisk.presentation.fragments.FavoritesFragment
import com.example.kinopoisk.presentation.fragments.UserAccountFragment
import com.example.kinopoisk.presentation.interfaices.OnFilmClickListener
import com.example.kinopoisk.presentation.ui.fragment.FilmPageFragment

class HomeFragment : Fragment(), OnFilmClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Настройка RecyclerView
        binding.firstRecycleView.layoutManager = LinearLayoutManager(context)
        val adapter = CategoriesAdapter(mutableMapOf(), this@HomeFragment) // Используем Map
        binding.firstRecycleView.adapter = adapter

// Подписываемся на данные
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.updateData(categories)
            Log.d("HomeFragment", "Адаптер обновлён с ${categories.size} категориями")
        }

        // Подписываемся на ошибки
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Log.e("HomeFragment", errorMessage)
            }
        }

        // Загрузка данных
        viewModel.loadData()

        // Обработчики нажатий на кнопки
        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            toFavoritesScreen()
        }
    }

    override fun onFilmClick(film: Film) {
        val filmPageFragment = FilmPageFragment.newInstance(film)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, filmPageFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun toAccountScreen() {
        val accountFragment = UserAccountFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, accountFragment)
            .addToBackStack(null)
            .commit()
    }

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
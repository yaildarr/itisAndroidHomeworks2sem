package ru.practice.homeworks.presentation.cats


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.practice.homeworks.MainActivity
import ru.practice.homeworks.R
import ru.practice.homeworks.databinding.FragmentMainBinding
import ru.practice.homeworks.util.NavigationAction

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews(){
        viewBinding.buttonRandomCat.setOnClickListener {
            (requireActivity() as? MainActivity)?.navigate(
                destination = CatFragment(),
                action = NavigationAction.REPLACE,
                isAddToBackStack = true
            )
        }
        viewBinding.buttonCatId.setOnClickListener {
            val catIdFragment = CatIdFragment.newInstance(viewBinding.textInputIdCat.editText?.text.toString())

            (requireActivity() as? MainActivity)?.navigate(
                destination = catIdFragment,
                action = NavigationAction.REPLACE,
                isAddToBackStack = true
            )
        }
    }
}


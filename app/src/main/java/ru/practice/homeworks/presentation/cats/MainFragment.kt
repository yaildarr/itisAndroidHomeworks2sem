package ru.practice.homeworks.presentation.cats


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(R.id.action_mainFragment_to_catFragment)
        }
        viewBinding.buttonCatId.setOnClickListener {


            val id = viewBinding.textInputIdCat.editText?.text.toString()

            findNavController().navigate(
                R.id.action_mainFragment_to_catIdFragment,
                bundleOf(CatIdFragment.catId to id)
            )
        }
    }
}


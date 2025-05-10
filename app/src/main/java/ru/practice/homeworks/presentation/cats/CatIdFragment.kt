package ru.practice.homeworks.presentation.cats

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practice.homeworks.R
import ru.practice.homeworks.databinding.FragmentCatBinding
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.wrapper.CatWrapper
import ru.practice.homeworks.domain.wrapper.DataSource

@AndroidEntryPoint
class CatIdFragment : Fragment(R.layout.fragment_cat_id) {

    private val binding: FragmentCatBinding by viewBinding(FragmentCatBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    private val catId: String by lazy {
        arguments?.getString("cat_id") ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyLog",catId)
        lifecycleScope.launch {
            viewModel.uiStateId.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> {
                        showData(state.data.catDataModel)
                        showToast(requireContext(),state.data.source)
                    }
                    is UiState.Error -> showError(state.message)
                }
            }
        }
        viewModel.loadListCatById(catId)
    }

    private fun showToast(context: Context, source: DataSource){
        val toast = Toast.makeText(context,source.name, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showLoading(){
        binding.catImageView.visibility = View.GONE
        binding.catIdText.visibility = View.GONE
        binding.shimmerImageView.visibility = View.VISIBLE
        binding.shimmerTextView.visibility = View.VISIBLE
        startShimmerAnimation()
    }

    private fun showData(cat: CatDataModel) {
        stopShimmerAnimation()

        Glide.with(this)
            .load(cat.url)
            .placeholder(R.drawable.ic_placeholder_cat)
            .error(R.drawable.ic_error_cat)
            .override(1000, 1000)
            .into(binding.catImageView)


        binding.catImageView.visibility = View.VISIBLE

        binding.catIdText.run {
            text = "ID кота: ${cat.id}"
            visibility = View.VISIBLE
        }


    }

    private fun showError(message: String) {
        stopShimmerAnimation()
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
    private fun startShimmerAnimation() {
        val animation = AlphaAnimation(0.5f, 1.0f).apply {
            duration = 500
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }

        binding.shimmerImageView.startAnimation(animation)
        binding.shimmerTextView.startAnimation(animation)
    }


    private fun stopShimmerAnimation() {
        binding.shimmerImageView.clearAnimation()
        binding.shimmerTextView.clearAnimation()

        binding.shimmerImageView.visibility = View.GONE
        binding.shimmerTextView.visibility = View.GONE
    }

    companion object {
        const val catId = "cat_id"
    }
}

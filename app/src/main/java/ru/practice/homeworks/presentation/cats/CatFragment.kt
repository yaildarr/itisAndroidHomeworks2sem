package ru.practice.homeworks.presentation.cats

import android.R.attr.data
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
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

@AndroidEntryPoint
class CatFragment : Fragment(R.layout.fragment_cat) {

    private val binding: FragmentCatBinding by viewBinding(FragmentCatBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> showData(state.data)
                    is UiState.Error -> showError(state.message)
                }
            }
        }
        viewModel.loadRandomCat()
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
        fun newInstance(): CatFragment = CatFragment()
    }
}
package ru.bashcony.currencyconverter.presentation.selector

import android.R
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Selection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.bashcony.currencyconverter.databinding.FragmentSelectorBinding
import ru.bashcony.currencyconverter.presentation.selector.adapter.SearchAdapter


@AndroidEntryPoint
class SelectorFragment : Fragment() {

    private lateinit var binding: FragmentSelectorBinding
    private val selectorViewModel: SelectorViewModel by activityViewModels()

    private val currencyRegex = "^0+(?=\\d)|[^0-9.,]".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSelectorBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onResume() {
        super.onResume()
        binding.fromIcon.loadCurrencyIcon(binding.currencyFromText.text.toString().substringBefore(' '))
        binding.toIcon.loadCurrencyIcon(binding.currencyToText.text.toString().substringBefore(' '))
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var suffix = ""

        selectorViewModel.currentCurrency.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty())
                return@observe

            suffix = " $it"
            val text = binding.amount.text.toString()
            binding.amount.setText(
                text.replace(currencyRegex, "")
                    .ifBlank { "0" }
                    .plus(suffix).let {
                        println(it)
                        it
                    }
            )
        }

        binding.amount.doOnTextChanged { text, start, before, count ->
            if (!text.toString().endsWith(suffix) || text.toString()
                    .contains("^0+(?=\\d)|^\\s".toRegex())
            ) {
                binding.amount.setText(
                    text.toString()
                        .replace(currencyRegex, "")
                        .let {
                            println(it)
                            it
                        }
                        .ifBlank { "0" }
                        .plus(suffix).let {
                            println(it)
                            it
                        }
                )

                Selection.setSelection(
                    binding.amount.text,
                    (binding.amount.text?.length ?: (suffix.length + 1)) - suffix.length
                )
            }
        }

        binding.amount.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun sendAccessibilityEvent(host: View, eventType: Int) {
                super.sendAccessibilityEvent(host, eventType)
                if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {

                    val idealPosition =
                        (binding.amount.text?.length ?: (suffix.length + 1)) - suffix.length

                    val endDifferentFromStart =
                        binding.amount.selectionEnd != binding.amount.selectionStart

                    if (binding.amount.selectionEnd > idealPosition)
                        if (endDifferentFromStart)
                            binding.amount.setSelection(
                                binding.amount.selectionStart,
                                idealPosition
                            )
                        else
                            binding.amount.setSelection(
                                idealPosition
                            )
                }
            }
        }

        selectorViewModel.currentState.observe(viewLifecycleOwner) {
            when (it) {
                SelectorState.Initial -> {
                    binding.selectorShimmer.root.visibility = View.VISIBLE
                    binding.selectorScroll.visibility = View.GONE
                    binding.errorLayout.root.visibility = View.GONE
                    selectorViewModel.loadCurrencies()
                }
                SelectorState.Loading -> onLoading()
                is SelectorState.LoadedCurrencies -> {
                    val list = listOf("").plus(it.currencies.map { "${it.code} (${it.localizedName})" })

                    val fromAdapter = SearchAdapter(
                        binding.currencyFromText.context,
                        R.layout.simple_dropdown_item_1line,
                        ArrayList(list)
                    )

                    val toAdapter = SearchAdapter(
                        binding.currencyFromText.context,
                        R.layout.simple_dropdown_item_1line,
                        ArrayList(list)
                    )

                    binding.currencyFromText.setAdapter(fromAdapter)
                    binding.currencyToText.setAdapter(toAdapter)

                    binding.currencyFromText.setOnItemClickListener { parent, view, position, id ->
                        selectorViewModel.setFromCurrency(fromAdapter[position].substringBefore(' '))
                        binding.fromIcon.loadCurrencyIcon(fromAdapter[position].substringBefore(' '))
                    }

                    binding.currencyToText.setOnItemClickListener { parent, view, position, id ->
                        binding.toIcon.loadCurrencyIcon(toAdapter[position].substringBefore(' '))
                    }

                    binding.convertButton.setOnClickListener { _ ->
                        findNavController().navigate(
                            SelectorFragmentDirections.actionSelectorFragmentToConvertedFragment(
                                from = binding.currencyFromText.text.toString().substringBefore(' '),
                                to = binding.currencyToText.text.toString().substringBefore(' '),
                                amount = binding.amount.text.toString().substringBefore(' ')
                            )
                        )
                    }

                    binding.swap.setOnClickListener {
                        val newFromText = binding.currencyToText.text
                        val newToText = binding.currencyFromText.text

                        binding.currencyToText.text = newToText
                        binding.currencyFromText.text = newFromText

                        selectorViewModel.setFromCurrency(
                            newFromText.toString().substringBefore(' ')
                        )
                        binding.fromIcon.loadCurrencyIcon(
                            newFromText.toString().substringBefore(' ')
                        )
                        binding.toIcon.loadCurrencyIcon(newToText.toString().substringBefore(' '))
                    }

                    binding.selectorShimmer.shimmer.stopShimmer()
                    binding.selectorShimmer.root.visibility = View.GONE
                    binding.selectorScroll.visibility = View.VISIBLE
                    binding.errorLayout.root.visibility = View.GONE
                }

                is SelectorState.Error -> onError(it.e)
            }
        }

        binding.errorLayout.errorButton.setOnClickListener {
            selectorViewModel.loadCurrencies()
        }
    }

    private fun ImageView.loadCurrencyIcon(currencyCode: String) {
        Glide.with(context.applicationContext)
            .load(Uri.parse("https://www.thecurrencyclub.co.uk/images/flags-currency/$currencyCode.png"))
            .placeholder(drawable)
            .error(ru.bashcony.currencyconverter.R.drawable.resource_default)
            .centerCrop()
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    private fun onLoading() {
        binding.selectorShimmer.shimmer.startShimmer()
        binding.selectorShimmer.root.visibility = View.VISIBLE
        binding.selectorScroll.visibility = View.GONE
        binding.errorLayout.root.visibility = View.GONE
    }

    private fun onError(e: Throwable) {

        binding.errorLayout.errorDescription.setText(e.message)

        binding.selectorShimmer.shimmer.stopShimmer()
        binding.selectorShimmer.root.visibility = View.GONE
        binding.selectorScroll.visibility = View.GONE
        binding.errorLayout.root.visibility = View.VISIBLE

        e.printStackTrace()
    }
}
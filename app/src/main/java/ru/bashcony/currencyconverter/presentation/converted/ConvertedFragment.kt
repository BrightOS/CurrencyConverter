package ru.bashcony.currencyconverter.presentation.converted

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.icu.util.GregorianCalendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.bashcony.currencyconverter.databinding.FragmentConvertedBinding
import ru.bashcony.currencyconverter.infra.utils.format
import ru.bashcony.currencyconverter.presentation.selector.SelectorState
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ConvertedFragment : Fragment() {

    private lateinit var binding: FragmentConvertedBinding
    private val convertedViewModel: ConvertedViewModel by viewModels()
    private val args: ConvertedFragmentArgs by navArgs()

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
    ) = FragmentConvertedBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.appbar.setStartButtonOnClickListener {
            findNavController().popBackStack()
        }

        convertedViewModel.currentState.observe(viewLifecycleOwner) {
            when (it) {
                ConvertedState.Initial -> {
                    binding.convertedShimmer.root.visibility = View.VISIBLE
                    binding.errorLayout.root.visibility = View.GONE
                    convertedViewModel.loadConversion(
                        args.from,
                        args.to,
                        args.amount
                    )
                }

                ConvertedState.Loading -> onLoading()

                is ConvertedState.LoadedConversion -> {

                    binding.timeUpdated.setText(
                        "С учётом курса валют от ${
                            SimpleDateFormat(
                                "dd MMM yyyy",
                                Locale.getDefault()
                            ).format(Date((it.conversion.timeLastUpdateUnix ?: 0L) * 1000))
                        } года:"
                    )

                    binding.from.text =
                        "${args.amount.format()} ${it.conversion.baseCode}"

                    binding.to.text =
                        "${it.conversion.conversionResult.format()} ${it.conversion.targetCode}"

                    binding.fromRate.text =
                        "1 ${it.conversion.baseCode} = ${it.conversion.conversionRate.format()} ${it.conversion.targetCode}"

                    binding.toRate.text =
                        "1 ${it.conversion.targetCode} = ${(1 / (it.conversion.conversionRate ?: .0)).format()} ${it.conversion.baseCode}"

                    binding.from.loadCurrencyIcon(args.from)
                    binding.to.loadCurrencyIcon(args.to)

                    binding.convertedShimmer.root.visibility = View.GONE
                    binding.convertedScroll.visibility = View.VISIBLE
                    binding.errorLayout.root.visibility = View.GONE
                }

                is ConvertedState.Error -> onError(it.e)
            }
        }

        binding.errorLayout.errorButton.setOnClickListener {
            convertedViewModel.loadConversion(args.from, args.to, args.amount)
        }
    }

    private fun TextView.loadCurrencyIcon(currencyCode: String) {
        Glide.with(context.applicationContext)
            .asDrawable()
            .load(Uri.parse("https://www.thecurrencyclub.co.uk/images/flags-currency/$currencyCode.png"))
            .placeholder(this.compoundDrawables[2])
            .error(ru.bashcony.currencyconverter.R.drawable.resource_default)
            .centerCrop()
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, placeholder, null)
                }
            })
    }

    private fun onLoading() {
        binding.convertedShimmer.shimmer.startShimmer()
        binding.convertedShimmer.root.visibility = View.VISIBLE
        binding.errorLayout.root.visibility = View.GONE
    }

    private fun onError(e: Throwable) {
        binding.errorLayout.errorDescription.setText(e.message)

        binding.convertedShimmer.root.visibility = View.GONE
        binding.convertedScroll.visibility = View.GONE
        binding.errorLayout.root.visibility = View.VISIBLE

        e.printStackTrace()
    }
}
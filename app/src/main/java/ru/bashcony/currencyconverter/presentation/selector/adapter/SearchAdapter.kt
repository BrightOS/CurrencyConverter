package ru.bashcony.currencyconverter.presentation.selector.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes


class SearchAdapter<String>(
    context: Context,
    @LayoutRes resource: Int,
    val items: ArrayList<String>
) : ArrayAdapter<String>(context, resource, items) {

    val originalItems = ArrayList(items)

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    operator fun get(position: Int) = items[position]

    private inner class SearchFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val result = FilterResults()

            if (!constraint.isNullOrBlank()) {
                val filteredList = originalItems.filter { it.toString().contains(constraint, true) }

                result.values = filteredList
                result.count = filteredList.size
            } else {
                result.values = originalItems
                result.count = originalItems.size
            }
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            items.clear()
            items.addAll(results?.values as List<String>)
            notifyDataSetChanged()
        }
    }

}
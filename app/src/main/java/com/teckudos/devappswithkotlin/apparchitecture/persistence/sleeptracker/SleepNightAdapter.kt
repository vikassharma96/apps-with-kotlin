package com.teckudos.devappswithkotlin.apparchitecture.persistence.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teckudos.devappswithkotlin.apparchitecture.persistence.database.SleepNight
import com.teckudos.devappswithkotlin.databinding.ListItemSleepNightBinding

// recycler view never interacts directly with view instead all its operations are done on
// view holder. adapter exposes three methods for recyclerview to us GetItemCount,
// OnCreateViewHolder and onBindViewHolder. to meet recyclerview interface requirements our
// adapter exposes ViewHolder.
// Adapter, adopts a list of data into an interface that the recyclerview can use.It does
// that by defining three methods.
// Adapter interface - 1.GetItemCount 2.OnCreateViewHolder 3.onBindViewHolder
// ListAdapter helps to build a recyclerview adapter that's backed by a list. ListAdapter
// will take care of keeping tracks of the list for us and notifying the adapter when list updated.
// DiffUtil and ListAdapter figure out what need to be change and recyclerview how to animate change

// To bind different or complex types we use binding adapter. binding adapter is just like
// recyclerview adapter they take our data and adapt into something that databinding can use
// to bind the view like text or an image.
class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    // no need to define data in case of listadapter
    /*var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
            // notifyDataSetChanged() tells recyclerview that the entire list is potentially
            // invalid as a result recyclerview has to re-bind and re-draw every item in the
            // list it true even it not current display on the scree it to expensive
            // recyclerview has rich api for updating a single element
            // 1.Add 2.Remove 3.Move 4.Update we can manually figure out what change in list
            // and use this method but it tedious for this recyclerview has class called
            // DiffUtil it calculate changes in list and minimize modifications. use
            // algorithm mysers diff to figure out minimum number of changes to make from
            // old list to produce new list. DiffUtil only draw changed item and animate
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // val layoutInflater = LayoutInflater.from(parent.context)
        // val view = layoutInflater
        //    .inflate(R.layout.list_item_sleep_night, parent, false)
        // return ViewHolder(view)
        return ViewHolder.from(parent)
        // this gives us a clean way to create a new viewholder and encapsulate the details
        // of inflation and what layout to the viewholder class.
    }

    // no need to override it too while using ListAdapter
    /*override fun getItemCount(): Int = data.size*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // val item = data[position] // no need with ListAdapter it provide getItem
        // val res = holder.itemView.context.resources so as this is method of viewholder
        // class so we encapsulate and move logic of class into class
        holder.bind(item, clickListener)
    }

    /*private fun ViewHolder.bind( this is extension function of our class viewholder
        item: SleepNight,
        res: Resources
    ) {
        sleepLength.text =
            convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        quality.text = convertNumericQualityToString(item.sleepQuality, res)

        qualityImage.setImageResource(
            when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            }
        )
    }*/

    // RecyclerView doesn't know anything about binding objects or data binding but we can
    // expose recyclerview API that's power of recyclerview adapter api we design code that
    // makes sense to us as long as we meet requirements of adapter interface.
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SleepNight,
            clickListener: SleepNightListener
        ) {
            /*val res = itemView.context.resources
            binding.sleepLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)

            binding.qualityImage.setImageResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_active
                }
            )*/
            // we use data binding to do this binding for us
            binding.sleep = item
            binding.clickListener = clickListener
            // as an optimization we can ask databinding to executer our pending binding rights away.
            // it's always a good idea to executePendingBindings() when using binding adapters in the
            // recyclerview since it can be slightly faster to size the views
            binding.executePendingBindings()
        }

        // now the details of which layout to inflate are part of viewholder class instead of
        // buried in our adapter
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                /*val view = layoutInflater
                    .inflate(R.layout.list_item_sleep_night, parent, false)*/
                // use data binding instead
                val binding = ListItemSleepNightBinding.inflate(
                    layoutInflater,
                    parent, false
                )
                return ViewHolder(binding)
            }
        }
    }
}

/*
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
*/
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    // DiffUtil gives callback in case we want to do a custom check for equality
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // this method called if we move one item to other position so it checks it's
        // new position from old position basically we check if item have same id or not
        return oldItem.nightId == newItem.nightId
        // it's important we only check id in this callback so diffutil will know difference
        // between an item to edit, remove or moved and an item being changed.
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // to determine if an item has changed we override this
        // call to check whether two item have same data used to detect if contents of an item
        // have changed this methods calls equality instead of object equals
        return oldItem == newItem
    }

}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    // when user select an item the onclick method in the listener is triggered with the selected item.
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

// simple implementation of recyclerview

/*class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>() {

    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        if (item.sleepQuality <= 1) {
            holder.textView.setTextColor(Color.RED)
        } else {
            // as recyclerview use offscreen viewholder and replace it with new data
            // so we need to reset it otherwise large sleep quality also show red
            holder.textView.setTextColor(Color.BLACK)
        }
        holder.textView.text = item.sleepQuality.toString()
    }

}*/



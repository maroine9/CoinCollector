package com.example.coincollector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CoinAdapter(private var coins: List<Coin>) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val yearTextView: TextView = itemView.findViewById(R.id.yearTextView)
        val rarityTextView: TextView = itemView.findViewById(R.id.rarityTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
        val coinImageView: ImageView = itemView.findViewById(R.id.coinImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_coin, parent, false)
        return CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]
        holder.yearTextView.text = coin.year
        holder.rarityTextView.text = coin.rarity
        holder.quantityTextView.text = coin.quantity.toString()
        holder.valueTextView.text = coin.value.toString()

        if (!coin.imagePath.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(coin.imagePath)
                .into(holder.coinImageView)
        } else {
            holder.coinImageView.setImageResource(R.drawable.placeholder_image)
        }
    }

    override fun getItemCount() = coins.size

    fun updateCoins(newCoins: List<Coin>) {
        coins = newCoins
        notifyDataSetChanged()
    }
}
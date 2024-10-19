/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.will.androidlighttest.recyclersample.flowerList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.will.androidlighttest.R
import com.will.androidlighttest.recyclersample.data.Flower

class FlowersAdapter(private val onClick: (Flower) -> Unit) :
    ListAdapter<Flower, FlowersAdapter.FlowerViewHolder>(FlowerDiffCallback) {
        init {
            setHasStableIds(true)
        }

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class FlowerViewHolder(itemView: View, val onClick: (Flower) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
         val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)
         val flowerImageView: ImageView = itemView.findViewById(R.id.flower_image)
         var currentFlower: Flower? = null

        init {
            itemView.setOnClickListener {
                currentFlower?.let {
                    onClick(it)
                }
            }
        }

        /* Bind flower name and image. */
        fun bind(flower: Flower) {
            currentFlower = flower


            flowerTextView.text = flower.name
            if (flower.image != null) {

                flowerImageView.setImageResource(flower.image)
            } else {

                flowerImageView.setImageResource(R.drawable.rose)
            }
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flower_item, parent, false)
        return FlowerViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        val flower = getItem(position)
        holder.bind(flower)

    }

    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val bundle: Bundle = payloads[0] as Bundle
            bundle.keySet().forEach {
                if (it == "name") {
                    holder.flowerTextView.text = bundle.getString("name")
                } else if (it == "image") {
                    holder.flowerImageView.setImageResource(bundle.getInt("image"))
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }
}

object FlowerDiffCallback : DiffUtil.ItemCallback<Flower>() {
    override fun areItemsTheSame(oldItem: Flower, newItem: Flower): Boolean {
        Log.e("WillWolf", "areItemsTheSame-->" + (oldItem.id == newItem.id))

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Flower, newItem: Flower): Boolean {
        Log.e("WillWolf", "areItemsTheSame-->" + (oldItem.description == newItem.description) + ", " + newItem.image)
        return oldItem.name == newItem.name
                && oldItem.image == newItem.image
                && oldItem.description == newItem.description
    }

    // 这里加这个方法，实现局部刷新
    override fun getChangePayload(oldItem: Flower, newItem: Flower): Any? {
        val bundle = Bundle()
        if (oldItem.name != newItem.name) {
            Log.e("WillWolf", "getChangePayload name")
            bundle.putString("name", newItem.name)
        }
        if (oldItem.image != newItem.image) {
            Log.e("WillWolf", "getChangePayload image")
            bundle.putInt("image", newItem.image!!)
        }

        return bundle
    }
}
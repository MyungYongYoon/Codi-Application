package com.example.golladreamclient.ui.main.image

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.golladreamclient.data.model.MediaStoreImage
import com.example.golladreamclient.databinding.ItemImagePickerBinding


class ImagePickerAdapter(
    private val imageClickListener: (position: Int, image: MediaStoreImage) -> Unit
) : ListAdapter<MediaStoreImage, ImagePickerAdapter.ImagePickerViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<MediaStoreImage>() {
        override fun areItemsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) = oldItem == newItem
    }

    inner class ImagePickerViewHolder(val binding: ItemImagePickerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaStoreImage) {
            if (item.contentUri != "CAMERA".toUri()) setImage(item.contentUri)
            else setEmptyImage()

            itemView.setOnClickListener {
                imageClickListener(absoluteAdapterPosition, item)
            }
        }

        private fun setEmptyImage() {
            binding.listItemImage.visibility = View.GONE
            binding.listItemCamera.visibility = View.VISIBLE
        }

        private fun setImage(uri: Uri) {
            binding.listItemImage.load(uri)
            binding.listItemImage.visibility = View.VISIBLE
            binding.listItemCamera.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerViewHolder {
        return ImagePickerViewHolder(
            ItemImagePickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagePickerViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

}
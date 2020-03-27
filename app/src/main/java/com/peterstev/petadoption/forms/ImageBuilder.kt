@file:Suppress("ReplacePutWithAssignment")
@file:SuppressLint("SetTextI18n", "InflateParams")

package com.peterstev.petadoption.forms

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.peterstev.petadoption.R
import com.peterstev.petadoption.models.adoption.Element
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ImageBuilder(private val element: Element, private val context: Context) {

    fun build(): MutableMap<String, View> {
        val mapList: MutableMap<String, View> = mutableMapOf()
        val imageLayout =
            LayoutInflater.from(context).inflate(R.layout.view_image, null, false)
        val image = imageLayout.findViewById<CircleImageView>(R.id.view_image_view)
        //enable handler to allow testing
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Picasso.get()
                .load(element.file ?: "")
                .placeholder(R.mipmap.ic_launcher_round)
                .resize(200, 200)
                .into(image)
        }

        mapList.put(element.uniqueId!!, imageLayout)

        return mapList
    }
}
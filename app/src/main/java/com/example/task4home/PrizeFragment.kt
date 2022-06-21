package com.example.task4home

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.example.task4home.databinding.FragmentPrizeBinding


class PrizeFragment : Fragment() {

    private var _binding:FragmentPrizeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrizeBinding.inflate(inflater)
        var view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //LANZA LA ANIMACION
        binding.prizeAnimation.setAnimation(R.raw.ape)
        binding.prizeAnimation.repeatCount = Animation.INFINITE
        binding.prizeAnimation.playAnimation()

        reproducirSonido()

        binding.prizeAnimation.setOnClickListener {
            reproducirSonido()
        }

    }

    fun reproducirSonido(){
        val mediaPlayer = MediaPlayer.create(activity as Context, R.raw.monkeysound)
        mediaPlayer.start()
    }



}
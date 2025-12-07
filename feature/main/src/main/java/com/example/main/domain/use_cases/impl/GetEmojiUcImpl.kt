package com.example.main.domain.use_cases.impl

import com.example.main.R
import com.example.main.domain.use_cases.GetEmojiUc
import javax.inject.Inject

class GetEmojiUcImpl @Inject constructor() : GetEmojiUc {
    override fun invoke(percent: Double): Int {
        return when (percent) {
            in 0.0..0.2 -> R.drawable.ic_angry_emotional
            in 0.21..0.4 -> R.drawable.ic_sad_emotional
            in 0.41..0.6 -> R.drawable.ic_medium_emotional
            in 0.61..0.8 -> R.drawable.ic_good_emotional
            else -> R.drawable.ic_max_emotional
        }
    }

}
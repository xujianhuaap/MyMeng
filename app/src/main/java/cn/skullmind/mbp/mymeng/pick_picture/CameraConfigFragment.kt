package cn.skullmind.mbp.mymeng.pick_picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import cn.skullmind.mbp.mymeng.R

class CameraConfigFragment : Fragment() {
     var changeListener: CameraConfigChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(activity, R.layout.fragment_camera_config, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seekBar = view.findViewById<SeekBar>(R.id.seek_bar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                changeListener?.onChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                this@CameraConfigFragment.fragmentManager?.also {
                    it.beginTransaction().hide(this@CameraConfigFragment).commit()
                }
            }
        })
    }

    companion object{
        const val ARGUMENTS_KEY_CHANGE_LISTENER = "changeListener"
    }
}

interface CameraConfigChangeListener{
    fun onChange(value: Int)
}
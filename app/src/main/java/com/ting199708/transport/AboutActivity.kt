package com.ting199708.transport

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AboutActivity : Fragment() {
    private var mListener: MainActivity.OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_about, container, false)
        val textview = view.findViewById<TextView>(R.id.textview)
        textview.text = "資料庫版本："+(fragmentManager!!.findFragmentByTag(MainActivity().javaClass.name) as MainActivity).returnVersion().toString()
        return view
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MainActivity.OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}

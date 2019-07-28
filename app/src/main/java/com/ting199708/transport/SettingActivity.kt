package com.ting199708.transport

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup

class SettingActivity : Fragment() {

    private var mListener: MainActivity.OnFragmentInteractionListener? = null
    private lateinit var settings: SharedPreferences
    var autoUpdate : Boolean = true
    var studentNumber : String = ""
    var password : String = ""
    var studentNumber_edittext : EditText? = null
    var password_edittext : EditText? = null
    var radioGroup : RadioGroup? = null
    var radioButton1 : RadioButton? = null
    var radioButton2 : RadioButton? = null
    var updateNow : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = activity!!.getSharedPreferences("DATA", 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_setting, container, false)
        studentNumber_edittext = view.findViewById(R.id.studentNumber)
        password_edittext = view.findViewById(R.id.password)
        radioGroup = view.findViewById(R.id.radioGroup)
        radioButton1 = view.findViewById(R.id.radioButton1)
        radioButton2 = view.findViewById(R.id.radioButton2)
        updateNow = view.findViewById(R.id.updateNow)
        updateNow!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val fragment : MainActivity = fragmentManager!!.findFragmentByTag(MainActivity().javaClass.name) as MainActivity
                fragment.update(true)
            }
        })
        studentNumber_edittext!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                write()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        password_edittext!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                write()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        radioGroup!!.setOnCheckedChangeListener { radioGroup, checkId -> write() }
        load()
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

    fun load() {
        autoUpdate = settings.getBoolean("AUTOUPDATE",true)
        studentNumber = settings.getString("STUDENT","")
        password = settings.getString("PASSWORD","")
        if (autoUpdate) radioButton1!!.isChecked = true
        else radioButton2!!.isChecked = true
        studentNumber_edittext!!.setText(studentNumber)
        password_edittext!!.setText(password)
    }
    fun write() {
        settings.edit()
                .putBoolean("AUTOUPDATE", radioButton1!!.isChecked)
                .apply()
        settings.edit()
                .putString("STUDENT", studentNumber_edittext!!.text.toString())
                .apply()
        settings.edit()
                .putString("PASSWORD", password_edittext!!.text.toString())
                .apply()
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            //Fragment隐藏时调用
            write()
        } else {
            //Fragment显示时调用
            load()
        }

    }

}

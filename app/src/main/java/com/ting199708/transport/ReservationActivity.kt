package com.ting199708.transport

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

class ReservationActivity : Fragment() {
    private var mListener: MainActivity.OnFragmentInteractionListener? = null
    private lateinit var settings: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_reservation, container, false)
        val webview = view.findViewById<WebView>(R.id.webview)
        settings = activity!!.getSharedPreferences("DATA", 0)
        loadWeb(webview)
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

    private fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {

        return inflater!!.inflate(R.layout.activity_reservation, container, false)
    }

    fun loadWeb(webview : WebView) {
        val url = "https://sso.nknu.edu.tw/userLogin/login.aspx?cUrl=/StudentProfile/Day/LastBusReserve.aspx"
        webview.loadUrl(url)
        webview.settings.setJavaScriptEnabled(true)
        val user = settings.getString("STUDENT","")
        val pwd = settings.getString("PASSWORD","")
        val js = "javascript:document.getElementById('ctl00_phMain_uLoginID').value = '" + user + "';document.getElementById('ctl00_phMain_uPassword').value='" + pwd + "';";
        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (Build.VERSION.SDK_INT >= 19) {
                    view.evaluateJavascript(js) { }
                } else {
                    view.loadUrl(js)
                }
            }
        })
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}

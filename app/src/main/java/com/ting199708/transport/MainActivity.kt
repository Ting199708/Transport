package com.ting199708.transport

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.format.Time
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recent.view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : Fragment() {

    private var eda = false
    private var seven = false
    private var school = false
    private var picture1 = false
    private var picture2 = false
    private var picture3 = false
    private var full_version = false
    private var autoupdate = true
    private var weekday: Int = 0
    private var online_version: Int = 0
    private var version: Int = 0
    private var newfunction: String = ""
    private var updating = false
    private var useable = false
    private var settings: SharedPreferences? = null
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var recentButton: Button
    private lateinit var edaButton: Button
    private lateinit var sevenButton: Button
    private lateinit var seven_one: Button
    private lateinit var seven_two: Button
    private lateinit var schoolButton: Button
    private lateinit var school_one: Button
    private lateinit var school_two: Button
    private lateinit var progess : RelativeLayout
    internal lateinit var animation1: Animation
    internal lateinit var animation2: Animation
    internal lateinit var animation3: Animation
    internal lateinit var animation4: Animation
    internal lateinit var animation5: Animation
    internal lateinit var animation6: Animation
    internal var edatothsr1: IntArray? = null
    internal var edatothsr2: IntArray? = null
    internal var edatonknu1: IntArray? = null
    internal var edatonknu2: IntArray? = null
    internal var seventocity1: IntArray? = null
    internal var seventocity2: IntArray? = null
    internal var seventonknu1: IntArray? = null
    internal var seventonknu2: IntArray? = null
    internal var schooltocity1: IntArray? = null
    internal var schooltocity2: IntArray? = null
    internal var schooltocountry1: IntArray? = null
    internal var schooltocountry2: IntArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_main, container, false)
        edaButton = view.findViewById(R.id.button)
        sevenButton = view.findViewById(R.id.button2)
        seven_one = view.findViewById(R.id.button3)
        seven_two = view.findViewById(R.id.button4)
        schoolButton = view.findViewById(R.id.button5)
        school_one = view.findViewById(R.id.button6)
        school_two = view.findViewById(R.id.button7)
        recentButton = view.findViewById(R.id.button8)
        progess = view.findViewById(R.id.progessLayout)
        onClick_setting()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animation1 = AnimationUtils.loadAnimation(activity, R.anim.anim1)
        animation2 = AnimationUtils.loadAnimation(activity, R.anim.anim2)
        animation3 = AnimationUtils.loadAnimation(activity, R.anim.anim1)
        animation4 = AnimationUtils.loadAnimation(activity, R.anim.anim2)
        animation5 = AnimationUtils.loadAnimation(activity, R.anim.anim3)
        animation6 = AnimationUtils.loadAnimation(activity, R.anim.anim4)
        val c = Calendar.getInstance()
        weekday = c.get(Calendar.DAY_OF_WEEK)
        settings = activity!!.getSharedPreferences("DATA", 0)
        //更新公告
        if (!settings!!.getBoolean("UPDATE2", false)) {
            /*AlertDialog.Builder(this)
                    .setTitle("更新公告")
                    .setMessage("更改最近班次顯示方式\n更改觀賞影片優惠內容，將只開啟最近班次功能，請見諒")
                    .setPositiveButton("確定",DialogInterface.OnClickListener({dialog, which ->  }))
                    .setCancelable(false)
                    .show()
            */
            settings!!.edit()
                    .putInt("VERSION", 0)
                    .apply()
            settings!!.edit()
                    .putBoolean("UPDATE2", true)
                    .apply()
        }

        if (full_version) button8.visibility = View.VISIBLE
        //取得版本
        readData()
        useable = true
        allRecentUpdate()
        if (!updating && autoupdate) update(false)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            //Fragment隐藏时调用
        } else {
            //Fragment显示时调用
        }

    }

    fun onClick_setting() {
        edaButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                eda_click(v!!)
            }
        })
        sevenButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                seven_click(v!!)
            }
        })
        seven_one.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                seven1_click(v!!)
            }
        })
        seven_two.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                seven2_click(v!!)
            }
        })
        schoolButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                school_click(v!!)
            }
        })
        school_one.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                school1_click(v!!)
            }
        })
        school_two.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                school2_click(v!!)
            }
        })
        recentButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                recent_click(v!!)
            }
        })
    }

    fun eda_click(view: View) {
        if (!updating) {
            if (eda) {
                eda = false
                if (picture1) {
                    imageView.startAnimation(animation6)
                    picture1 = false
                }
            } else {
                val bitmap: Bitmap? = getBitmapFromSDCard(".eda.png")
                if (bitmap != null) {
                    eda = true
                    imageView.startAnimation(animation5)
                    picture1 = true
                    picture2 = false
                    picture3 = false
                    imageView.setImageBitmap(bitmap)
                } else
                    losedata()
            }
        }

    }

    fun seven_click(view: View) {
        if (!updating) {
            if (seven) {
                seven = false
                linearLayout1!!.startAnimation(animation1)
                if (picture2) {
                    imageView!!.startAnimation(animation6)
                    picture2 = false
                }
            } else {
                seven = true
                linearLayout1!!.startAnimation(animation2)
            }
        }

    }

    fun school_click(view: View) {
        if (!updating) {
            if (school) {
                school = false
                linearLayout2!!.startAnimation(animation3)
                if (picture3) {
                    imageView!!.startAnimation(animation6)
                    picture3 = false
                }
            } else {
                school = true
                linearLayout2!!.startAnimation(animation4)
            }
        }

    }

    fun seven1_click(view: View) {
        if (!updating) {
            val bitmap: Bitmap? = getBitmapFromSDCard(".seven_two.png")
            if (bitmap != null) {
                if (!picture2) imageView!!.startAnimation(animation5)
                picture2 = true
                picture1 = false
                picture3 = false
                eda = false
                imageView!!.setImageBitmap(bitmap)
            } else
                losedata()
        }

    }

    fun seven2_click(view: View) {
        if (!updating) {
            val bitmap: Bitmap? = getBitmapFromSDCard(".seven_one.png")
            if (bitmap != null) {
                if (!picture2) imageView!!.startAnimation(animation5)
                picture2 = true
                picture1 = false
                picture3 = false
                eda = false
                imageView!!.setImageBitmap(bitmap)
            } else
                losedata()
        }

    }

    fun school1_click(view: View) {
        if (!updating) {
            val bitmap: Bitmap? = getBitmapFromSDCard(".school_one.png")
            if (bitmap != null) {
                if (!picture3) imageView!!.startAnimation(animation5)
                picture3 = true
                picture1 = false
                picture2 = false
                eda = false
                imageView!!.setImageBitmap(bitmap)
            } else
                losedata()
        }

    }

    fun school2_click(view: View) {
        if (!updating) {
            val bitmap: Bitmap? = getBitmapFromSDCard(".school_two.png")
            if (bitmap != null) {
                if (!picture3) imageView!!.startAnimation(animation5)
                picture3 = true
                picture1 = false
                picture2 = false
                eda = false
                imageView!!.setImageBitmap(bitmap)
            } else
                losedata()
        }

    }

    fun recent_click(view: View) {
        val recentLayout = LayoutInflater.from(activity).inflate(R.layout.activity_recent, null)
        if (useable) {
            val t = Time()
            t.setToNow()
            val hour = t.hour
            val minute = t.minute
            val currenttime = hour * 60 + minute
            if (weekday > 1 && weekday < 7) {
                //E04往高鐵
                run {
                    var i = 0
                    while (i <= 19) {
                        if (edatothsr1!![i] > currenttime) {
                            if (edatothsr1!![i] % 60 < 10)
                                recentLayout.textView4.text = (edatothsr1!![i] / 60).toString() + ":0" + edatothsr1!![i] % 60
                            else
                                recentLayout.textView4.text = (edatothsr1!![i] / 60).toString() + ":" + edatothsr1!![i] % 60
                            i = 19
                        } else
                            recentLayout.textView4.text = "末班駛離"
                        i++
                    }
                }
                //E04往高師大
                run {
                    var i = 0
                    while (i <= 19) {
                        if (edatonknu1!![i] > currenttime) {
                            if (edatonknu1!![i] % 60 < 10)
                                recentLayout.textView5.text = (edatonknu1!![i] / 60).toString() + ":0" + edatonknu1!![i] % 60
                            else
                                recentLayout.textView5.text = (edatonknu1!![i] / 60).toString() + ":" + edatonknu1!![i] % 60
                            i = 19
                        } else
                            recentLayout.textView5.text = "末班駛離"
                        i++
                    }
                }
                //7A往加昌站
                run {
                    var i = 0
                    while (i <= 24) {
                        if (seventocity1!![i] > currenttime) {
                            if (seventocity1!![i] % 60 < 10)
                                recentLayout.textView10.text = (seventocity1!![i] / 60).toString() + ":0" + seventocity1!![i] % 60
                            else
                                recentLayout.textView10.text = (seventocity1!![i] / 60).toString() + ":" + seventocity1!![i] % 60
                            i = 24
                        } else
                            recentLayout.textView10!!.text = "末班駛離"
                        i++
                    }
                }
                //7A往高師大
                var i = 0
                while (i <= 24) {
                    if (seventonknu1!![i] > currenttime) {
                        if (seventonknu1!![i] % 60 < 10)
                            recentLayout.textView11!!.text = (seventonknu1!![i] / 60).toString() + ":0" + seventonknu1!![i] % 60
                        else
                            recentLayout.textView11!!.text = (seventonknu1!![i] / 60).toString() + ":" + seventonknu1!![i] % 60
                        i = 24
                    } else
                        recentLayout.textView11!!.text = "末班駛離"
                    i++
                }
            } else {
                //E04往高鐵(假日)
                run {
                    var i = 0
                    while (i <= 19) {
                        if (edatothsr2!![i] > currenttime) {
                            if (edatothsr2!![i] % 60 < 10)
                                recentLayout.textView4.text = (edatothsr2!![i] / 60).toString() + ":0" + edatothsr2!![i] % 60
                            else
                                recentLayout.textView4.text = (edatothsr2!![i] / 60).toString() + ":" + edatothsr2!![i] % 60
                            i = 19
                        } else
                            recentLayout.textView4.text = "末班駛離"
                        i++
                    }
                }
                //E04往高師大(假日)
                run {
                    var i = 0
                    while (i <= 19) {
                        if (edatonknu2!![i] > currenttime) {
                            if (edatonknu2!![i] % 60 < 10)
                                recentLayout.textView5.text = (edatonknu2!![i] / 60).toString() + ":0" + edatonknu2!![i] % 60
                            else
                                recentLayout.textView5.text = (edatonknu2!![i] / 60).toString() + ":" + edatonknu2!![i] % 60
                            i = 19
                        } else
                            recentLayout.textView5!!.text = "末班駛離"
                        i++
                    }
                }
                //7A往加昌站(假日)
                run {
                    var i = 0
                    while (i <= 24) {
                        if (seventocity2!![i] > currenttime) {
                            if (seventocity2!![i] % 60 < 10)
                                recentLayout.textView10.text = (seventocity2!![i] / 60).toString() + ":0" + seventocity2!![i] % 60
                            else
                                recentLayout.textView10.text = (seventocity2!![i] / 60).toString() + ":" + seventocity2!![i] % 60
                            i = 24
                        } else
                            recentLayout.textView10.text = "末班駛離"
                        i++
                    }
                }
                //7A往高師大(假日)
                var i = 0
                while (i <= 24) {
                    if (seventonknu2!![i] > currenttime) {
                        if (seventonknu2!![i] % 60 < 10)
                            recentLayout.textView11.text = (seventonknu2!![i] / 60).toString() + ":0" + seventonknu2!![i] % 60
                        else
                            recentLayout.textView11.text = (seventonknu2!![i] / 60).toString() + ":" + seventonknu2!![i] % 60
                        i = 24
                    } else
                        recentLayout.textView11.text = "末班駛離"
                    i++
                }
            }
            if (weekday > 1 && weekday < 6) {
                //往和平(周一~四)
                run {
                    var i = 0
                    while (i <= 19) {
                        if (schooltocity1!![i] > currenttime) {
                            if (schooltocity1!![i] % 60 < 10)
                                recentLayout.textView12.text = (schooltocity1!![i] / 60).toString() + ":0" + schooltocity1!![i] % 60
                            else
                                recentLayout.textView12.text = (schooltocity1!![i] / 60).toString() + ":" + schooltocity1!![i] % 60
                            i = 19
                        } else
                            recentLayout.textView12.text = "末班駛離"
                        i++
                    }
                }
                //往燕巢(周一~四)
                var i = 0
                while (i <= 19) {
                    if (schooltocountry1!![i] > currenttime) {
                        if (schooltocountry1!![i] % 60 < 10)
                            recentLayout.textView13.text = (schooltocountry1!![i] / 60).toString() + ":0" + schooltocountry1!![i] % 60
                        else
                            recentLayout.textView13.text = (schooltocountry1!![i] / 60).toString() + ":" + schooltocountry1!![i] % 60
                        i = 19
                    } else
                        recentLayout.textView13.text = "末班駛離"
                    i++
                }
            } else if (weekday == 6) {
                //往和平(週五)
                run {
                    var i = 0
                    while (i <= 19) {
                        if (schooltocity2!![i] > currenttime) {
                            if (schooltocity2!![i] % 60 < 10)
                                recentLayout.textView12.text = (schooltocity2!![i] / 60).toString() + ":0" + schooltocity2!![i] % 60
                            else
                                recentLayout.textView12.text = (schooltocity2!![i] / 60).toString() + ":" + schooltocity2!![i] % 60
                            i = 19
                        } else
                            recentLayout.textView12.text = "末班駛離"
                        i++
                    }
                }
                //往燕巢(周五)
                var i = 0
                while (i <= 19) {
                    if (schooltocountry2!![i] > currenttime) {
                        if (schooltocountry2!![i] % 60 < 10)
                            recentLayout.textView13.text = (schooltocountry2!![i] / 60).toString() + ":0" + schooltocountry2!![i] % 60
                        else
                            recentLayout.textView13.text = (schooltocountry2!![i] / 60).toString() + ":" + schooltocountry2!![i] % 60
                        i = 19
                    } else
                        recentLayout.textView13.text = "末班駛離"
                    i++
                }
            } else {
                //校車假日
                recentLayout.textView12!!.text = "末班駛離"
                if (currenttime < 1340)
                    recentLayout.textView13!!.text = "22:20"
                else
                    recentLayout.textView13!!.text = "末班駛離"
            }
            AlertDialog.Builder(activity!!)
                    .setView(recentLayout)
                    .show()
        } else {
            if (!updating) losedata()
        }
    }

    fun save(bitmap: Bitmap, filename: String) {
        try {
            // 取得外部儲存裝置路徑
            val path = Environment.getExternalStorageDirectory().path + "/.Transport"
            val file_path = File(path)
            if (!file_path.exists()) file_path.mkdir()
            // 開啟檔案
            val file = File(path, filename)
            // 開啟檔案串流
            val out = FileOutputStream(file)
            // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            // 刷新並關閉檔案串流
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    /*fun downloader(url: String, filename: String) {
        val downloadManager : DownloadManager =  (DownloadManager) Context.getSystemService(Context.DOWNLOAD_SERVICE)
        val request : DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().path + "/.Transport", filename)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        myDownloadReference = downloadManager.enqueue(request);
    }*/

    fun update(necessary: Boolean) {  //necessary必要更新

        //網路偵測
        if (isConnected) {
            //版本檢查
            Thread(Runnable {
                val mURL = URL("https://sites.google.com/site/ting199708/home/download/transport/version.txt?attredirects=0&d=1")
                val connection = mURL.openConnection() as HttpURLConnection
                connection.connect()
                val bufferedInputStream = BufferedInputStream(connection.inputStream)
                val inputStreamReader = InputStreamReader(bufferedInputStream, "Unicode")
                val bufferedReader = BufferedReader(inputStreamReader)
                online_version = bufferedReader.readLine().toInt()
                newfunction = bufferedReader.readLine()

                //釋放資源
                bufferedReader.close()
                bufferedInputStream.close()
                connection.disconnect()
                activity!!.runOnUiThread {
                    if (online_version > version) {
                        var success = true
                        val alertDialog = AlertDialog.Builder(this.activity!!)
                                .setTitle("更新")
                                .setMessage("偵測到新版資料庫，是否立即更新?\n目前版本：" + version + "\n更新版本：" + online_version + "\n更新內容：" + newfunction)
                                .setPositiveButton("是", DialogInterface.OnClickListener { dialog, which ->
                                    progess.visibility = View.VISIBLE
                                    //Update
                                    Thread(Runnable {
                                        //取得圖檔
                                        try {
                                            val bitmap = getBitmapFromURL("https://sites.google.com/site/ting199708/home/download/transport/eda.png?attredirects=0&d=1")
                                            save(bitmap!!, ".eda.png")
                                            val bitmap2 = getBitmapFromURL("https://sites.google.com/site/ting199708/home/download/transport/school_one.png?attredirects=0&d=1")
                                            save(bitmap2!!, ".school_one.png")
                                            val bitmap3 = getBitmapFromURL("https://sites.google.com/site/ting199708/home/download/transport/school_two.png?attredirects=0&d=1")
                                            save(bitmap3!!, ".school_two.png")
                                            val bitmap4 = getBitmapFromURL("https://sites.google.com/site/ting199708/home/download/transport/seven_one.png?attredirects=0&d=1")
                                            save(bitmap4!!, ".seven_one.png")
                                            val bitmap5 = getBitmapFromURL("https://sites.google.com/site/ting199708/home/download/transport/seven_two.png?attredirects=0&d=1")
                                            save(bitmap5!!, ".seven_two.png")
                                            //取得最近班次用資料
                                            getrecentdata(".edatothsr1", "https://sites.google.com/site/ting199708/home/download/transport/edatothsr1?attredirects=0&d=1")
                                            getrecentdata(".edatothsr2", "https://sites.google.com/site/ting199708/home/download/transport/edatothsr2?attredirects=0&d=1")
                                            getrecentdata(".edatonknu1", "https://sites.google.com/site/ting199708/home/download/transport/edatonknu1?attredirects=0&d=1")
                                            getrecentdata(".edatonknu2", "https://sites.google.com/site/ting199708/home/download/transport/edatonknu2?attredirects=0&d=1")
                                            getrecentdata(".seventocity1", "https://sites.google.com/site/ting199708/home/download/transport/seventocity1?attredirects=0&d=1")
                                            getrecentdata(".seventocity2", "https://sites.google.com/site/ting199708/home/download/transport/seventocity2?attredirects=0&d=1")
                                            getrecentdata(".seventonknu1", "https://sites.google.com/site/ting199708/home/download/transport/seventonknu1?attredirects=0&d=1")
                                            getrecentdata(".seventonknu2", "https://sites.google.com/site/ting199708/home/download/transport/seventonknu2?attredirects=0&d=1")
                                            getrecentdata(".schooltocity1", "https://sites.google.com/site/ting199708/home/download/transport/schooltocity1?attredirects=0&d=1")
                                            getrecentdata(".schooltocity2", "https://sites.google.com/site/ting199708/home/download/transport/schooltocity2?attredirects=0&d=1")
                                            getrecentdata(".schooltocountry1", "https://sites.google.com/site/ting199708/home/download/transport/schooltocountry1?attredirects=0&d=1")
                                            getrecentdata(".schooltocountry2", "https://sites.google.com/site/ting199708/home/download/transport/schooltocountry2?attredirects=0&d=1")
                                            allRecentUpdate()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            success = false
                                        }

                                        activity!!.runOnUiThread {
                                            updating = false
                                            version = online_version
                                            useable = true
                                            progess.visibility = View.GONE
                                            if (success) Toast.makeText(activity, "已更新至版本" + version, Toast.LENGTH_SHORT).show()
                                            else Toast.makeText(activity, "更新失敗", Toast.LENGTH_SHORT).show()
                                            saveData()
                                        }
                                    }).start()
                                })
                                .setNegativeButton("否", DialogInterface.OnClickListener { dialog, which -> })
                                .show()
                    } else if (online_version == version && necessary) {
                        Toast.makeText(activity, "已更新至最新版本", Toast.LENGTH_SHORT).show()
                    }
                }
            }).start()


        } else {
            updating = false
            if (necessary)
                Toast.makeText(activity, "請開啟網路進行更新", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun allRecentUpdate() {
        edatothsr1 = recentupdate(".edatothsr1"); edatothsr2 = recentupdate(".edatothsr2"); edatonknu1 = recentupdate(".edatonknu1")
        edatonknu2 = recentupdate(".edatonknu2"); seventocity1 = recentupdate(".seventocity1"); seventocity2 = recentupdate(".seventocity2")
        seventonknu1 = recentupdate(".seventonknu1"); seventonknu2 = recentupdate(".seventonknu2"); schooltocity1 = recentupdate(".schooltocity1")
        schooltocity2 = recentupdate(".schooltocity2"); schooltocountry1 = recentupdate(".schooltocountry1"); schooltocountry2 = recentupdate(".schooltocountry2")
        val lose = (edatothsr1 == null || edatothsr2 == null || edatonknu1 == null || edatonknu2 == null
                || seventocity1 == null || seventocity2 == null || seventonknu1 == null
                || seventonknu2 == null || schooltocity1 == null || schooltocity2 == null
                || schooltocountry1 == null || schooltocountry2 == null)
        if (lose) losedata()
    }

    //取得資料
    fun getrecentdata(name: String, url: String) {
        val path = Environment.getExternalStorageDirectory().path + "/.Transport"
        val file = File(path, name)
        try {
            val mURL = URL(url)
            val connection = mURL.openConnection() as HttpURLConnection
            connection.connect()
            val bufferedInputStream = BufferedInputStream(connection.inputStream)
            val fileOutputStream = FileOutputStream(file)
            val b = ByteArray(8192)
            var i: Int
            do {
                i = bufferedInputStream.read(b)
                if (i == -1) break
                fileOutputStream.write(b, 0, i)
            } while (true)
            //釋放資源
            bufferedInputStream.close()
            fileOutputStream.close()
            connection.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun losedata() {
        version = 0
        useable = false
        updating = true
        val alertDialog = AlertDialog.Builder(activity!!)
                .setTitle("注意")
                .setMessage("尚未下載資料庫，即將開始下載")
                .setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                    saveData()
                    update(true)
                })
                .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                    updating = false
                })
                .setCancelable(false)
                .show()
    }

    //網路偵測
    private val isConnected: Boolean
        get() {
            val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return if (networkInfo != null && networkInfo.isAvailable) {
                true
            } else false
        }

    //儲存版本
    fun saveData() {
        settings = activity!!.getSharedPreferences("DATA", 0)
        settings!!.edit()
                .putInt("VERSION", version)
                .apply()
    }

    //讀取版本
    fun readData() {
        settings = activity!!.getSharedPreferences("DATA", 0)
        version = settings!!.getInt("VERSION", 0)
        autoupdate = settings!!.getBoolean("AUTOUPDATE", true)
    }

    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream = connection.inputStream
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun getBitmapFromSDCard(file: String): Bitmap? {
        try {
            val path = Environment.getExternalStorageDirectory().path + "/.Transport"
            return BitmapFactory.decodeFile(path + "/" + file)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    //更新矩陣資料
    fun recentupdate(route: String): IntArray? {
        try {
            val filename = Environment.getExternalStorageDirectory().path + "/.Transport/" + route
            val fileInputStream = FileInputStream(filename)
            val bufferedInputStream = BufferedInputStream(fileInputStream)
            val inputStreamReader = InputStreamReader(bufferedInputStream, "UTF-8")
            val bufferedReader = BufferedReader(inputStreamReader)
            var data: String
            var i = 0
            val j = IntArray(30)
            while (true) {
                data = bufferedReader.readLine() ?: break
                //更新矩陣
                j[i] = Integer.parseInt(data)
                i += 1
            }
            bufferedReader.close()
            return j
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    //get the device serial
    fun getSerial(): String? {
        Log.d("Serial", Build.SERIAL)
        return Build.SERIAL
    }

    fun returnVersion(): Int {
        return version
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}

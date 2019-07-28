package com.ting199708.transport

import android.Manifest
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.View

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class Permission : AppCompatActivity() {
    private var permission = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permission == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this,DrawerActivity::class.java))
            finish()
        }
    }
    fun give(v : View) {
        PermissionPermissionsDispatcher.needsPermissionWithCheck(this)
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun needsPermission() {
        startActivity(Intent(this,DrawerActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied() {
        AlertDialog.Builder(this)
                .setTitle("權限取得失敗")
                .setMessage("您已拒絕授予權限，程式無法運作")
                .setPositiveButton("離開",DialogInterface.OnClickListener({dialog, which -> finish() }))
                .setCancelable(false)
                .show()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        AlertDialog.Builder(this)
                .setTitle("權限取得失敗")
                .setMessage("您已永久拒絕授予權限，請至設定中開啟權限或重新安裝")
                .setPositiveButton("離開",DialogInterface.OnClickListener({dialog, which -> finish() }))
                .setNegativeButton("前往設定",DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent()
                    intent.action = "android.intent.action.MAIN"
                    intent.component = ComponentName("com.android.settings", "com.android.settings.Settings")
                    startActivity(intent)
                })
                .setCancelable(false)
                .show()
    }
}


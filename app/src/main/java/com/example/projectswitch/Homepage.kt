package com.example.projectswitch

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_homepage.*


class Homepage : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.projectswitch"
    private val description = "Test notification"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        auth = FirebaseAuth.getInstance()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val fragmentManager: FragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.lightMode)!!)
            .commit()

        val powerButton: ImageButton = findViewById(R.id.powerButtonFragmentDark)
        val powerButton2: ImageButton = findViewById(R.id.powerButtonFragmentLight)


        powerButton.setOnClickListener {

            fragmentManager.beginTransaction()
                .hide(fragmentManager.findFragmentById(R.id.darkMode)!!).commit()
            fragmentManager.beginTransaction()
                .show(fragmentManager.findFragmentById(R.id.lightMode)!!).commit()

            val intent = Intent(this, LauncherActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.sound
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setContentTitle("IoT Switch")
                    .setContentText("Power On")
                    .setSmallIcon(R.drawable.asseteight)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.asseteight
                        )
                    )
                    .setContentIntent(pendingIntent)
            } else {
                builder = Notification.Builder(this)
                    .setContentTitle("IoT Switch")
                    .setContentText("Power On")
                    .setSmallIcon(R.drawable.asseteight)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.asseteight
                        )
                    )
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())


        }
        powerButton2.setOnClickListener {

            fragmentManager.beginTransaction()
                .hide(fragmentManager.findFragmentById(R.id.lightMode)!!).commit()
            fragmentManager.beginTransaction()
                .show(fragmentManager.findFragmentById(R.id.darkMode)!!).commit()

            val intent = Intent(this, LauncherActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.sound
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setContentTitle("IoT Switch")
                    .setContentText("Power Off")
                    .setSmallIcon(R.drawable.assetseven)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.assetseven
                        )
                    )
                    .setContentIntent(pendingIntent)
            } else {
                builder = Notification.Builder(this)
                    .setContentTitle("IoT Switch")
                    .setContentText("Power Off")
                    .setSmallIcon(R.drawable.assetseven)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.assetseven
                        )
                    )
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.SigningOut) {
            val mAlertDialogBuilder = AlertDialog.Builder(this)
            mAlertDialogBuilder.setTitle("Confirm this")
            mAlertDialogBuilder.setIcon(R.drawable.ic_warning)
            mAlertDialogBuilder.setMessage("Are you sure you want to sign out")
            mAlertDialogBuilder.setCancelable(true)

            mAlertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                auth.signOut()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
                finish()
            }
            mAlertDialogBuilder.setNegativeButton("No") { _, _ ->
                Toast.makeText(this, "Continue", Toast.LENGTH_SHORT).show()
            }
            mAlertDialogBuilder.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            val mAlertDialog = mAlertDialogBuilder.create()
            mAlertDialog.show()

            return true


        }
        return super.onOptionsItemSelected(item)
    }
}
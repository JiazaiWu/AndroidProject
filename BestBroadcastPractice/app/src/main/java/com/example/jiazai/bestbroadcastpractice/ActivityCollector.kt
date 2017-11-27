package com.example.jiazai.bestbroadcastpractice

import android.app.Activity

/**
 * Created by jiazai on 17-11-24.
 */
class ActivityCollector {

    companion object {

        private val activities = ArrayList<Activity>()

        fun addActivity(activity: Activity) {
            activities.add(activity)
        }

        fun removeActivity(activity: Activity) {
            activities.remove(activity)
        }

        fun finishAll() {
            for (activity in activities) {
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }
        }
    }
}
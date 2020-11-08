package com.example.hatewait

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_loading.*
import java.lang.ref.WeakReference

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }
}

class MyAsyncTask (activity: Activity) : AsyncTask<String, Int, Int>(){

    private val activityReference: WeakReference<Activity> = WeakReference(activity)
    //Override the doInBackground method
    override fun doInBackground(vararg params: String?): Int {
        val count = 100
        var index=0
        while (index<count){
            // Publish the progress
            publishProgress(index+1)
            //Sleeping for 1/10 seconds
            Thread.sleep(100)
            index++
        }
        return count;
    }

    // Override the onProgressUpdate method to post the update on main thread
    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)

        if(values[0]!=null) {
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            activity.loading_view.progress = (values[0] as Int).toFloat() / 100
            activity.loading_view.setMaxFrame(values[0] as Int)
        }
    }

    // Setup the intial UI before execution of the background task
    override fun onPreExecute() {
        super.onPreExecute()

        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        activity.loading_view.progress = 0.0f
        Log.d("Kotlin","On PreExecute Method")
    }

    // Update the final status by overriding the OnPostExecute method.
    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        Log.d("Kotlin","On Post Execute Method")
    }
}
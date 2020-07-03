package com.example.hatewait

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.daimajia.swipe.util.Attributes
import com.example.hatewait.model.newClient
import com.example.hatewait.socket.*
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import hatewait.vo.QueueListSerializable
import kotlinx.android.synthetic.main.activity_store_waiting_list.*
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap



lateinit var mRecyclerView:RecyclerView
var clientList = ArrayList<ClientData>()
lateinit var mAdapter: SwipeRecyclerViewAdapter
lateinit var listContext: Context
lateinit var totalWaitingNumView:TextView
lateinit var autoCallSwitchView:Switch

class StoreWaitingList : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_waiting_list)

        listContext = this.applicationContext
        mRecyclerView = findViewById<View>(R.id.myRecyclerView) as RecyclerView
        totalWaitingNumView = findViewById<View>(R.id.totalWaitingNum) as TextView
        autoCallSwitchView = findViewById<View>(R.id.autoCallSwitch) as Switch

//        // 임시로 여기에
//        //Get Firebase FCM token
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnSuccessListener(this,
//                { instanceIdResult ->
//                    Log.i("현재 토큰: ", instanceIdResult.token)
//                })

        // 자기 폰번호 subscribe하면 됨!!!
        FirebaseMessaging.getInstance().subscribeToTopic("01093097866")

        init()
    }

    override fun onResume() {
        super.onResume()
        Log.i("로그", "Resume")
        try {
            storeWaitingListAsyncTask().execute()
        } catch (e: Exception) {
            Log.i("로그", e.toString())
        }
    }

    fun init() {
        setRecyclerView()
        makeAddDialog()
        readFile()


        autoCallSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //자동호출 on
            } else {
                //자동호출 off
            }
        })

    }


    private fun readFile() {

        val scan = Scanner(resources.openRawResource(R.raw.client))
        while (scan.hasNextLine()) {
            val client = scan.nextLine()
            var id_tmp = client.split(",")[0]
            var phone_tmp = client.split(",")[1]
            var name_tmp = client.split(",")[2]
            var people_num_tmp = client.split(",")[3]
            var is_member_tmp = client.split(",")[4]
            var data_tmp =
                ClientData(id_tmp, phone_tmp, name_tmp, people_num_tmp, is_member_tmp)
            clientList?.add(data_tmp)
        }
    }



    class storeWaitingListAsyncTask : AsyncTask<Unit, Unit, QueueListSerializable?>() {

        var qls: QueueListSerializable? = null

        override fun doInBackground(vararg params: Unit?): QueueListSerializable? { // 소켓 연결

            try {
                clientSocket = Socket(SERVERIP, PORT)
                Log.i("로그", "storeWaitingListAsyncTask:: ok")
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                writer!!.println("STRQUE;${STOREID}")

                val ois = ObjectInputStream(clientSocket!!.getInputStream()) //객체 받는 스트림
                qls = ois.readObject() as QueueListSerializable

                if (ois != null) {
                    try {
                        ois!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (writer != null) {
                    writer!!.close()
                }
                if (clientSocket != null) {
                    try {
                        clientSocket!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                Log.i("로그", e.toString())
                println(e)
            }

            return qls
        }

        override fun onPostExecute(result: QueueListSerializable?) { // UI에 보이기
            super.onPostExecute(result)
            Log.i("로그", "storeWaitingListAsyncTask - onPostExecute :: ok")
            Log.i("로그", "result ::" + result.toString() ?: "전달된 리스트가 없습니다.")

            autoCallSwitchView.text = "${result?.autonum}번째 팀까지 자동호출"

            clientList.clear()

            for (i in 0..(result?.qivo?.size?.minus(1) ?: 0)) {
                var data_tmp =
                    ClientData(
                        result?.qivo?.get(i)?.id.toString(),
                        result?.qivo?.get(i)?.phone.toString(),
                        result?.qivo?.get(i)?.name.toString(),
                        result?.qivo?.get(i)?.peopleNum.toString(),
                        result?.qivo?.get(i)?.turn.toString()
                    )
                clientList?.add(data_tmp)
            }
            setRecyclerView()
        }
    }


    class addCustomerTask : AsyncTask<newClient, Unit, Unit>() {

        override fun doInBackground(vararg params: newClient) { // 소켓 연결
            try {
                clientSocket = Socket(SERVERIP, PORT)
                Log.i("로그", "addCustomerTask:: ok")
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                reader = BufferedReader(
                    InputStreamReader(
                        clientSocket!!.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )

                //TODO INSQUE;NONMEM;가게id;이름;전화번호;대기인원
                writer!!.println("INSQUE;NONMEM;${STOREID};" + "${params[0].name};${params[0].phoneNum};${params[0].peopleNum}")
                Log.i("로그: 대기 추가 서버로 보냄", "INSQUE;NONMEM;${STOREID};" + "${params[0].name};${params[0].phoneNum};${params[0].peopleNum}")
                val addCustomerResponse: String = reader!!.readLine()
                Log.i("로그: add서버응답", addCustomerResponse)

                var data_tmp =
                    ClientData(
                        "",
                        params[0].phoneNum,
                        params[0].name,
                        params[0].peopleNum,
                        ""
                    )
                clientList?.add(data_tmp)

                if (reader != null) {
                    try {
                        reader!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (writer != null) {
                    writer!!.close()
                }
                if (clientSocket != null) {
                    try {
                        clientSocket!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            Toasty.success(listContext, "추가되었습니다", Toast.LENGTH_SHORT, true).show()
            setRecyclerView()
        }

    }

    fun makeAddDialog() {
        val fab: View = findViewById(R.id.addFab)
        fab.setOnClickListener { view ->

            MaterialDialog(this).show {
                noAutoDismiss()
                title(text = "대기손님 추가")
                val customView = customView(R.layout.activity_add_waiting).getCustomView()
                val addWaitingName =
                    customView.findViewById(R.id.addWaitingName) as TextView
                var addWaitingPerson =
                    customView.findViewById(R.id.addWaitingPerson) as TextView
                var addWaitingPhonenum =
                    customView.findViewById(R.id.addWaitingPhonenum) as TextView
                positiveButton { dialog ->

                    if (addWaitingName.text.toString()
                            .equals("") || addWaitingPerson.text.toString()
                            .equals("") || addWaitingPhonenum.text.toString().equals("")
                    ) {

                    } else {
                        var newclient = newClient(
                            name = addWaitingName.text.toString(),
                            peopleNum = addWaitingPerson.text.toString(),
                            phoneNum = addWaitingPhonenum.text.toString()
                        )

                        //TODO asynctask 실행하기
                        addCustomerTask().execute(newclient)
                        dismiss()
                    }
                }
                negativeButton() { dialog ->
                    dismiss()
                }
            }
        }
    }
}


// RecyclerView와 Adapter 연결
fun setRecyclerView() {
    totalWaitingNumView.text = "현재 ${clientList.size}팀 대기중"

    mRecyclerView!!.layoutManager =
        LinearLayoutManager(listContext, LinearLayoutManager.VERTICAL, false)

    val pref = listContext.getSharedPreferences("myPref", Context.MODE_PRIVATE)


    var called = HashMap<String, Boolean>()
    val prefKeys: MutableSet<String> = pref.all.keys
    for (pref_key in prefKeys) {
        if (getShared(pref, pref_key)) {
            called[pref_key] = true
        } else {
            called[pref_key] = false
        }
    }

    val clicked = HashMap<String, Boolean>()
    for (a in clientList) {
        clicked[a.phone] = false
    }

    mAdapter = SwipeRecyclerViewAdapter(clientList!!, called, clicked, pref, listContext)
    mAdapter.mode = Attributes.Mode.Single

//        mAdapter.itemClickListener = object : SwipeRecyclerViewAdapter.onItemClickListener {
//            override fun onItemClick(
//                holder: SwipeRecyclerViewAdapter.SimpleViewHolder,
//                view: View,
//                position: Int
//            ) {
//                Log.d("position", position?.toString())
//                if (holder.detailView.visibility == GONE) {
//                    holder.detailView.visibility = VISIBLE
//                } else {
//                    holder.detailView.visibility = GONE
//                }
//            }
//        }
    mRecyclerView.adapter = mAdapter
}


package com.example.hatewait

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.daimajia.swipe.util.Attributes
import com.example.hatewait.serialize.QueueInfoVo
import com.example.hatewait.serialize.QueueListSerializable
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_store_waiting_list.*
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StoreWaitingList : AppCompatActivity() {

    var clientList = ArrayList<ClientData>()
    lateinit var mAdapter: SwipeRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_waiting_list)


        init()

        // 자기 폰번호 subscribe하면 됨!!!
        FirebaseMessaging.getInstance().subscribeToTopic("weather")

        // 임시로 여기에
        //Get Firebase FCM token
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(this,
                { instanceIdResult ->
                    Log.i("현재 토큰: ", instanceIdResult.token)
                })
    }

    override fun onResume() {
        super.onResume()
//        storeWaitingListAsyncTask(this).execute()
    }


    @SuppressLint("WrongViewCast")
    private fun init() {

        readFile()
        setRecyclerView()
        makeAddDialog()



        autoCallSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //자동호출 on
            } else {
                //자동호출 off
            }
        })

    }


    private fun makeAddDialog() {
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
                positiveButton() { dialog ->
                    Log.d("이름", addWaitingName.text.toString())
                    Log.d("인원", addWaitingPerson.text.toString())
                    Log.d("전화번호", addWaitingPhonenum.text.toString())
                    //db에 addClient()
                    addCustomerTask().execute()
                    dismiss()
                    Toasty.success(view.context, "추가되었습니다", Toast.LENGTH_SHORT, true).show()
                    setRecyclerView()
                }
                negativeButton() { dialog ->
                    dismiss()
                }
            }
        }
    }


    // RecyclerView와 Adapter 연결
    private fun setRecyclerView() {
        // 나중에 디자인부분 DividerItemDecoration.java 참고
        // http://helloandroidexample.blogspot.com/2015/07/swipe-recyclerview-using.html

        val mRecyclerView = findViewById<View>(R.id.myRecyclerView) as RecyclerView
        // Layout Managers:
        mRecyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Item Decorator:
        // mRecyclerView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.divider)))
        // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        val pref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)


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


        // Creating Adapter object
        mAdapter = SwipeRecyclerViewAdapter(clientList!!, called, clicked, pref, baseContext)

        //Single or Multiple
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

    // 나중에 db에서 불러오기
    // getList()
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


    class storeWaitingListAsyncTask(context: StoreWaitingList) :
        AsyncTask<Unit, Unit, QueueListSerializable>() {

        private var clientSocket: Socket? = null
        private var reader: BufferedReader? = null // 서버 < 앱
        private var writer: PrintWriter? = null // 앱 > 서버
        private val port = 3000// port num
        private val ip: String = "192.168.1.166"// 서버 ip적기
        var qls: QueueListSerializable? = null

        override fun doInBackground(vararg params: Unit?): QueueListSerializable { // 소켓 연결

            try {
                clientSocket = Socket(ip, port)
                Log.i("로그", "storeWaitingListAsyncTask:: ok")
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                writer!!.println("STRQUE;s1111")


                val ois = ObjectInputStream(clientSocket!!.getInputStream()) //객체 받는 스트림

                // ois 왜 못받아오지...
                qls = ois.readObject() as QueueListSerializable
                Log.i("로그", "qls객체 잘 받아왔나" + qls!!.autonum.toString())
                Log.i("로그", "qls객체 잘 받아왔나" + qls!!.qivo.toString())
                Log.i("로그", "qls객체 잘 받아왔나" + qls!!.toString())

                // 임시 객체
                var qiv = QueueInfoVo()
                qiv.id = "s1234"
                qiv.phone = 1093097866
                qiv.name = "choyerin"
                qiv.peopleNum = 22
                qiv.turn = 4

                var list = MutableList<QueueInfoVo>(1, { qiv })

                // 임시 객체
                qls = QueueListSerializable()
                qls!!.autonum = 10
                qls!!.qivo = list


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
                println(e)
            }

            return qls!!
        }

        override fun onPostExecute(result: QueueListSerializable) { // UI에 보이기
            super.onPostExecute(result)
            Log.i("로그", "storeWaitingListAsyncTask - onPostExecute :: ok")
            Log.i("로그", "result ::" + result.toString())
        }
    }


}

class addCustomerTask : AsyncTask<Unit, Unit, Unit>() {

    private var clientSocket: Socket? = null
    private var reader: BufferedReader? = null // 서버 < 앱
    private var writer: PrintWriter? = null // 앱 > 서버

    private val port = 3000// port num
    private val ip: String = "192.168.1.166"// 서버 ip적기

    var StoreMenuArray: Array<String>? = null

    override fun doInBackground(vararg params: Unit) { // 소켓 연결
        try {
            clientSocket = Socket(ip, port)
            Log.i("로그", "addCustomerTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )

            //TODO INSQUE;NONMEM;가게id;이름;전화번호;대기인원
            writer!!.println("INSQUE;NONMEM;s0000;chocho;12345678;3")
            Log.i("로그: 대기 추가 서버로 보냄", "INSQUE;NONMEM;s0000;chocho;12345678;3")
            val addCustomerResponse: String = reader!!.readLine()
            Log.i("로그: add서버응답", addCustomerResponse)
            //서버>앱: 서버 > 앱: INSQUE;NONMEM;대기열 몇번째인지(turn)
//                StoreMenuArray = storeMenuResponse.split(";").toTypedArray()


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

    override fun onPostExecute(result: Unit) { // UI에 보이기
        try {
            super.onPostExecute(result)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}


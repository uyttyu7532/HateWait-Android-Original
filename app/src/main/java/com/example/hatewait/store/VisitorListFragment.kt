package com.example.hatewait.store

import VisitorRecyclerViewAdapter
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import kotlinx.android.synthetic.main.fragment_visitor_list.*
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller
import java.util.*
import kotlin.collections.ArrayList


class VisitorListFragment : Fragment() {

//    var words = mutableMapOf<String, String>()
    var visitorList = ArrayList<String>()
    lateinit var adapter: VisitorRecyclerViewAdapter
    lateinit var tts: TextToSpeech
    var isTtsReady = false
    var switchOn2 = false

    //    lateinit var basicPref: SharedPreferences
    //    lateinit var myPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_visitor_list, container, false)
        val visitorListRecyclerView = view.findViewById<RecyclerView>(R.id.visitor_list_recycler_view)

        val fastScroller = view.findViewById(R.id.visitor_list_fast_scroller) as VerticalRecyclerViewFastScroller
        fastScroller.setRecyclerView(visitorListRecyclerView)
        visitorListRecyclerView.addOnScrollListener(fastScroller.getOnScrollListener())


        visitorListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = VisitorRecyclerViewAdapter(visitorList, switchOn2)
        visitorListRecyclerView.adapter = adapter


        init(visitorListRecyclerView)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//        // 전체보이기/숨기기
//        meaningSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                switchOn = true
//                makeList(recyclerView, array)
//            } else {
//                switchOn = false
//                makeList(recyclerView, array)
//            }
//        }

//        sortByABC.setOnClickListener {
//            sortByABC.setTextColor(Color.BLACK)
//            sortByRecent.setTextColor(Color.GRAY)
//            sortArray()
//        }
//        sortByRecent.setOnClickListener {
//            sortByRecent.setTextColor(Color.BLACK)
//            sortByABC.setTextColor(Color.GRAY)
//            makeList(recyclerView, array)
//        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })


    }

    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    @SuppressLint("WrongConstant")
    private fun init(visitorRecyclerView: RecyclerView) {

//        basicPref = context!!.getSharedPreferences("basicPref", Context.MODE_PRIVATE)
//        myPref = context!!.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            isTtsReady = true
            tts.language = Locale.US
        })


        visitorList.add("조예린")
        visitorList.add("조예린2")
        visitorList.add("조예린3")
        visitorList.add("조예린4")
        visitorList.add("조예린5")
        visitorList.add("조예린6")
        visitorList.add("조예린7")
        visitorList.add("조예린8")
        visitorList.add("조예린9")
        visitorList.add("조예린10")
        visitorList.add("조예린11")



//        loadAllData(myPref)
//        basicPref.edit().clear().apply()
//        readBasicFile(recyclerView)
        makeList(visitorRecyclerView, visitorList)


        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP and ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                visitorRecyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

//                val delWord = adapter.removeItem(viewHolder.adapterPosition)
//                delData(myPref, delWord)
//                Toasty.error(
//                    context!!,
//                    "\"" + delWord + "\" 단어가 삭제되었습니다.",
//                    Toast.LENGTH_SHORT,
//                    true
//                ).show();
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(visitorRecyclerView)

    }


    private fun makeList(visitorRecyclerView: RecyclerView, array: ArrayList<String>) {
        visitorRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = VisitorRecyclerViewAdapter(visitorList, switchOn2)
        adapter.itemClickListener = object : VisitorRecyclerViewAdapter.onItemClickListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onItemClick(
                holder: VisitorRecyclerViewAdapter.MyViewHolder,
                view: View,
                data: String,
                position: Int
            ) {

                if (holder.visitorListDetailView.visibility == View.GONE) {
                    holder.visitorListDetailView.visibility = View.VISIBLE
                } else {
                    holder.visitorListDetailView.visibility = View.GONE
                }
            }
        }
        visitorRecyclerView.adapter = adapter

        try {
            if (searchView.query != null) {
                adapter.filter.filter(searchView.query.toString())
            }
        } catch (_: Exception) {

        }


    }

//    private fun saveData(pref: SharedPreferences, word: String, meaning: String) {
//        val editor = pref.edit()
//        editor.putString(word, meaning)
//            .apply()
//
//        words[word] = meaning
//        array.add(word)
//    }


//    private fun loadAllData(pref: SharedPreferences) {
//        val prefKeys: MutableSet<String> = pref.all.keys
//        for (pref_key in prefKeys) {
//            words[pref_key] = pref.getString(pref_key, "null")
//            array.add(pref_key)
//        }
//    }

//    private fun delData(pref: SharedPreferences, word: String) {
//
//        val editor = pref.edit()
//        editor.remove(word).commit()
//
//        words.remove(word)
//        array.remove(word)
//        //https://www.it-swarm.dev/ko/java/%ED%8C%8C%EC%9D%BC%EC%97%90%EC%84%9C-%EC%A4%84%EC%9D%84-%EC%B0%BE%EC%95%84%EC%84%9C-%EC%A0%9C%EA%B1%B0%ED%95%98%EC%8B%AD%EC%8B%9C%EC%98%A4/967010671/
//        //https://stackoverrun.com/ko/q/11565281
//        //https://mantdu.tistory.com/731
//
//    }


//    fun sortArray() {
//        var sortedArray: ArrayList<String> = ArrayList(visitorList)
//        Collections.sort(sortedArray)
//        makeList(visitorListRecyclerView, sortedArray)
//    }




}

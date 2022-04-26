package com.masterandroid.thecatapi_app.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.masterandroid.thecatapi_app.R
import com.masterandroid.thecatapi_app.adapter.HomeAdapter
import com.masterandroid.thecatapi_app.model.ResponseItem
import com.masterandroid.thecatapi_app.networking.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var photos: ArrayList<ResponseItem>
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(view)
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        recyclerView = view.findViewById(R.id.recyclerView_home)
        photos = ArrayList()

        apiPosterListRetrofit()
        refreshAdapter(photos)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false

            photos.clear()
            apiPosterListRetrofit()
            homeAdapter.notifyDataSetChanged()

        }


    }

    private fun apiPosterListRetrofit() {

        RetrofitHttp.posterService.listPhotos().enqueue(object : Callback<ArrayList<ResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseItem>>,
                response: Response<ArrayList<ResponseItem>>
            ) {

                photos.clear()
                Log.d("response", response.body().toString())
                photos.addAll(response.body()!!)
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                refreshAdapter(photos)
                Log.d("@@@@", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<ResponseItem>>, t: Throwable) {
                Log.d("@@@", t.message.toString())
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun refreshAdapter(photos:ArrayList<ResponseItem>) {
        homeAdapter = HomeAdapter(requireContext(),photos) {

        }
        recyclerView.adapter = homeAdapter

    }


}
package com.perumdajepara.jlajah.allreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.model.data.Review
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.hilang
import com.perumdajepara.jlajah.util.showAlert
import com.perumdajepara.jlajah.util.terlihat
import kotlinx.android.synthetic.main.activity_all_review.*

class AllReviewActivity : AppCompatActivity(), AllReviewView {

    private val allReviewPresenter = AllReviewPresenter()
    private val reviewData: MutableList<Review> = mutableListOf()
    private lateinit var reviewAdapter: ReviewAdapter
    private var startPage = 1
    private var pageCount = 0
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_review)

        onAttachView()
    }

    override fun onAttachView() {
        allReviewPresenter.onAttach(this)

        val idLocation = intent.getIntExtra(ConstantVariable.id, 0)
        val nameLocation = intent.getStringExtra(ConstantVariable.fullName)

        setSupportActionBar(toolbar_all_review)
        supportActionBar?.apply {
            title = nameLocation
            setDisplayHomeAsUpEnabled(true)
        }

        allReviewPresenter.getAllReview(this, idLocation, startPage, perPage)

        swipe_all_review.setOnRefreshListener {
            startPage = 1
            reviewData.clear()
            allReviewPresenter.getAllReview(this, idLocation, startPage, perPage)
        }

        val linearLayout = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(reviewData)

        rv_all_review.apply {
            layoutManager = linearLayout
            adapter = reviewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && startPage <= pageCount && pageCount != 1) {
                        startPage = startPage.plus(1)
                        allReviewPresenter.getAllReview(this@AllReviewActivity, idLocation, startPage, perPage)
                    }
                }
            })
        }
    }

    override fun onDetachView() {
        allReviewPresenter.onDetach()
        allReviewPresenter.disposable()
    }

    override fun totalPage(count: Int) {
        pageCount = count
    }

    override fun showData(review: List<Review>) {
        if (review.isEmpty()) {
            tv_tidak_ada_ulasan.terlihat()
        } else {
            tv_tidak_ada_ulasan.hilang()
        }

        reviewData.addAll(review)
        reviewAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun showLoading() {
        swipe_all_review.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_all_review.isRefreshing = false
    }

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}


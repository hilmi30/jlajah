package com.perumdajepara.jlajah.home

import android.content.Context
import com.perumdajepara.jlajah.BuildConfig
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.basecontract.BasePresenter
import com.perumdajepara.jlajah.network.ApiRepository
import com.perumdajepara.jlajah.network.RetrofitBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class HomePresenter: BasePresenter<HomeView> {

    private var mView: HomeView? = null
    private var services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: HomeView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun getAllCategory(codeLang: String, context: Context) {
        mView?.showCategoryLoading()
        disposable = services.getAllCategory(codeLang)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.showData(it.data)
                    mView?.hideCategoryLoading()
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))

                    mView?.hideCategoryLoading()
                },
                onComplete = {
                    mView?.hideCategoryLoading()
                }
            )
    }
}
package com.perumdajepara.jlajah.bookmark

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

class BookmarkPresenter: BasePresenter<BookmarkView> {

    private var mView: BookmarkView? = null
    private val services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: BookmarkView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun getBookmarkByUser(context: Context, token: String, codeLang: String) {
        mView?.showLoading()
        disposable = services.getBookmarkByUser(accessToken = token, codeLanguage = codeLang)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.items.isNullOrEmpty()) mView?.showDataKosong() else mView?.hideDataKosong()
                    mView?.showData(it.items)
                    mView?.hideLoading()
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))

                    mView?.hideLoading()
                },
                onComplete = {
                    mView?.hideLoading()
                }
            )
    }

    fun deleteBookmark(context: Context, idLocation: Int, token: String) {
        mView?.showLoading()
        disposable = services.deleteBookmark(accessToken = token, locationId = idLocation)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.statusCode == 200) {
                        mView?.suksesDelete(context.getString(R.string.hapus_berhasil))
                    } else {
                        mView?.error(context.getString(R.string.terjadi_kesalahan))
                    }
                    mView?.hideLoading()
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))

                    mView?.hideLoading()
                },
                onComplete = {
                    mView?.hideLoading()
                }
            )
    }

}
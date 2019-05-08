package com.perumdajepara.jlajah.detaillokasi

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

class DetailLokasiPresenter: BasePresenter<DetailLokasiView> {

    private var mView: DetailLokasiView? = null
    private var services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: DetailLokasiView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun getDetailLokasi(context: Context, codeLang: String, idLokasi: Int) {
        mView?.showLoading()
        disposable = services.getDetailLokasiById(codeLang, idLokasi)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.showData(it)
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

    fun getReviewByUser(context: Context, idLocation: Int, accessToken: String) {
        mView?.showReviewLoading()
        disposable = services.getReviewByUser(idLocation, accessToken)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it.status) {
                        1 -> mView?.showReview(it.data)
                        0 -> mView?.hideReview()
                        else -> mView?.error(context.getString(R.string.terjadi_kesalahan))
                    }
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))
                    mView?.hideReviewLoading()
                },
                onComplete = {
                    mView?.hideReviewLoading()
                }
            )
    }

    fun cekBookmark(context: Context, accessToken: String, idLocation: Int) {
        disposable = services.cekBookmark(accessToken = accessToken, locationId = idLocation)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.cekBookmark(it.count)
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))
                },
                onComplete = {
                    //
                }
            )
    }

    fun addBookmark(context: Context, accessToken: String, idLocation: Int) {
        mView?.showLoading()
        disposable = services.addBookmark(accessToken = accessToken, locationId = idLocation)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.suksesBookmark()
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
                        mView?.suksesDelete()
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
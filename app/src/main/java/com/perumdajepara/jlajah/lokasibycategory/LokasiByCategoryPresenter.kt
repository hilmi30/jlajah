package com.perumdajepara.jlajah.lokasibycategory

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

class LokasiByCategoryPresenter: BasePresenter<LokasiByCategoryView> {

    private var mView: LokasiByCategoryView? = null
    private val services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: LokasiByCategoryView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun getLokasiByCategory(context: Context, codeLang: String, idCategory: Int, page: Int, perPage: Int) {
        mView?.showLokasiLoading()

        disposable = services.getLokasiByCategory(codeLang, idCategory, page, perPage)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.showData(it.items)
                    mView?.totalPage(it.pageCount)
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.cekKoneksi(context.getString(R.string.cek_koneksi))
                    mView?.hideLokasiLoading()
                },
                onComplete = {
                    mView?.hideLokasiLoading()
                }
            )
    }

    fun cariLokasi(context: Context, codeLanguage: String, key: String, page: Int, perPage: Int, sort: String, category: Int) {
        mView?.showLokasiLoading()
        disposable = services.cariLokasi(
            category = category,
            codeLanguage = codeLanguage,
            key = key,
            page = page,
            perPage = perPage,
            sort = sort
        )
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.page == 1) {
                        mView?.resetData(it.items)
                    } else {
                        mView?.showData(it.items)
                    }
                    mView?.totalPage(it.pageCount)
                },
                onError = {
                    if (it is HttpException) {
                        when (it.code()) {
                            422 -> mView?.error(context.getString(R.string.username_email_ada))
                            else -> mView?.error(context.getString(R.string.terjadi_kesalahan))
                        }
                    }

                    if (it is UnknownHostException || it is TimeoutException) mView?.cekKoneksi(context.getString(R.string.cek_koneksi))
                    mView?.showLokasiLoading()
                },
                onComplete = {
                    mView?.showLokasiLoading()
                }
            )
    }

}
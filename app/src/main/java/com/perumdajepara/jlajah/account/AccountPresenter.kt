package com.perumdajepara.jlajah.account

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

class AccountPresenter: BasePresenter<AccountView> {

    private var mView: AccountView? = null
    private var services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: AccountView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun resetPassword(
        userToken: String,
        passLama: String,
        passBaru: String,
        context: Context?
    ) {
        mView?.showLoading()
        mView?.submitResetBtnIsEnabled(false)
        disposable = services.resetPassword(userToken, passLama, passBaru)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.suksesResetPass()
                    mView?.hideLoading()
                    mView?.submitResetBtnIsEnabled(true)
                },
                onError = {
                    if (it is HttpException) {
                        val errorCode = it.code()
                        when (errorCode) {
                            422 -> mView?.error(context?.getString(R.string.password_lama_salah))
                            else -> mView?.error(context?.getString(R.string.terjadi_kesalahan))
                        }
                    }
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context?.getString(R.string.cek_koneksi))
                    mView?.hideLoading()
                    mView?.submitResetBtnIsEnabled(true)
                },
                onComplete = {
                    mView?.hideLoading()
                    mView?.submitResetBtnIsEnabled(true)
                }
            )
    }

    fun editProfile(accessToken: String, id: Int, nama: String, genderId: Int, notelp: String, context: Context) {
        mView?.showProfilLoading()
        disposable = services.editProfile(accessToken, id, nama, genderId, notelp)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.suksesGantiProfil(it)
                },
                onError = {
                    if (it is HttpException) mView?.error(context.getString(R.string.terjadi_kesalahan))
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))
                    mView?.hideProfilLoading()
                },
                onComplete = {
                    mView?.hideProfilLoading()
                }
            )
    }

}
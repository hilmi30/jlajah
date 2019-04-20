package com.perumdajepara.jlajah.login

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

class LoginPresenter: BasePresenter<LoginView> {

    private var mView: LoginView? = null
    private var services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: LoginView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun login(email: String, password: String, context: Context) {
        mView?.showLoading()

        disposable = services.login(email, password)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.suksesLogin(it)
                },
                onError = {
                    if (it is HttpException) {
                        when (it.code()) {
                            422 -> mView?.error(context.getString(R.string.username_password_salah))
                            else -> mView?.error(context.getString(R.string.terjadi_kesalahan))
                        }
                    }
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))
                    mView?.hideLoading()
                },
                onComplete = {
                    mView?.hideLoading()
                }
            )
    }

    fun forgetPassword(email: String, password: String, context: Context) {
        mView?.showLupaPassLoading()
        mView?.submitBtnIsEnabled(false)

        disposable = services.forgetPassword(email, password)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mView?.suksesForgetPass()
                    mView?.submitBtnIsEnabled(true)
                },
                onError = {
                    if (it is HttpException) {
                        val errorCode = it.code()
                        when (errorCode) {
                            404 -> mView?.error(context.getString(R.string.email_tidak_ditemukan))
                            else -> mView?.error(context.getString(R.string.terjadi_kesalahan))
                        }
                    }
                    if (it is UnknownHostException || it is TimeoutException) mView?.error(context.getString(R.string.cek_koneksi))
                    mView?.hideLupaPassLoading()
                    mView?.submitBtnIsEnabled(true)
                },
                onComplete = {
                    mView?.hideLupaPassLoading()
                    mView?.submitBtnIsEnabled(true)
                }
            )
    }
}
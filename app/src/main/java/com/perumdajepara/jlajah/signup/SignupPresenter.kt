package com.perumdajepara.jlajah.signup

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

class SignupPresenter: BasePresenter<SignupView> {

    private var mView: SignupView? = null
    private val services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepository::class.java)
    private var disposable: Disposable? = null

    override fun onAttach(view: SignupView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun disposable() {
        disposable?.dispose()
    }

    fun signup(
        username: String,
        email: String,
        password: String,
        nama: String,
        noTelp: String,
        nilaiGender: Int,
        context: Context
    ) {
        mView?.showLoading()
        disposable = services.signup(username, email, password, nama, noTelp, nilaiGender)
            .debounce(100, TimeUnit.MILLISECONDS)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.statusCode == 200) {
                        mView?.suksesRegister()
                        mView?.hideLoading()
                    }
                },
                onError = {
                    if (it is HttpException) {
                        val errorCode = it.code()
                        when (errorCode) {
                            422 -> mView?.error(context.getString(R.string.username_email_ada))
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


}
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class NetworkManager(private val context: Context) {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback
    private var networkReceiver: BroadcastReceiver? = null
    lateinit var networkStatusObserver: Observer<Boolean>


    private fun connectionCallback(): ConnectivityManager.NetworkCallback {
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                networkStatusObserver.onNext(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                networkStatusObserver.onNext(false)
            }
        }
        return networkConnectionCallback
    }

    fun updateNetworkConnection() {
        val networkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (networkConnection != null) {
            networkStatusObserver.onNext(networkConnection.isConnected)
        }
    }

    fun observeNetworkStatus(): Observable<Boolean> {

        return Observable.create { emitter ->
            networkStatusObserver = object : Observer<Boolean> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(isConnected: Boolean) {
                    emitter.onNext(isConnected)
                }

                override fun onError(e: Throwable) {
                    emitter.onError(e)
                }

                override fun onComplete() {
                    emitter.onComplete()
                }
            }

            emitter.setCancellable {
                // Dispose the observer when the observable is unsubscribed
                networkReceiver?.let { context.unregisterReceiver(it) }
                connectivityManager.unregisterNetworkCallback(connectionCallback())
            }

            // Register network callback
            val callback = connectionCallback()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(callback)
            } else {
                networkReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        updateNetworkConnection()
                    }
                }
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
            updateNetworkConnection()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

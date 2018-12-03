package n.com.myapplication.extension

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import n.com.myapplication.rxAndroid.BaseSchedulerProvider

/**
 * Use SchedulerProvider configuration for Observable
 */
fun Completable.with(scheduler: BaseSchedulerProvider): Completable =
    this.observeOn(scheduler.ui()).subscribeOn(scheduler.io())

/**
 * Use SchedulerProvider configuration for Single
 */
fun <T> Single<T>.with(scheduler: BaseSchedulerProvider): Single<T> =
    this.observeOn(scheduler.ui()).subscribeOn(scheduler.io())

/**
 * Use SchedulerProvider configuration for Observable
 */
fun <T> Observable<T>.with(scheduler: BaseSchedulerProvider): Observable<T> =
    this.observeOn(scheduler.ui()).subscribeOn(scheduler.io())
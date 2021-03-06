package com.example.zhang.mvp.presenter;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.TimeUtils;
import com.example.zhang.base.BasePresenter;
import com.example.zhang.http.AbstractCustomerSubscribe;
import com.example.zhang.http.ResponseBean;
import com.example.zhang.mvp.contract.RxJavaContract;
import com.example.zhang.mvp.model.RxJavaModel;
import com.example.zhang.mvp.model.bean.BannerBean;
import com.example.zhang.mvp.model.bean.LoginResponseBean;
import com.example.zhang.mvp.model.bean.MainDataBean;
import com.example.zhang.mvp.model.bean.RegisterParamBean;
import com.example.zhang.mvp.model.bean.RegisterResponseBean;
import com.example.zhang.mvp.model.bean.UrlRequestBean;
import com.example.zhang.utils.LogUtils;
import com.example.zhang.utils.RxLifeCycleUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * @author zzh
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class RxJavaPresenter extends BasePresenter<RxJavaContract.IRxJavaView, RxJavaModel> {
    private static final String TAG = RxJavaPresenter.class.getSimpleName();

    public RxJavaPresenter(RxJavaContract.IRxJavaView view) {
        super(view);
        model = new RxJavaModel();
        rxJavaMapExample();
    }

    /**
     * 练习RxJava创建过程
     */
    @SuppressLint("CheckResult")
    public void rxJavaCreateExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Observer<Integer>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        LogUtils.error(TAG, "rxJavaCreateExample---:onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.error(TAG, "rxJavaCreateExample---:" + integer);
//                        if (integer == 2) {
//                            disposable.dispose();
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(TAG, "rxJavaCreateExample---:onError");
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.error(TAG, "rxJavaCreateExample---:onComplete");
                        disposable.dispose();
                    }
                });
    }

    /**
     * RxJava -Map语法练习
     */
    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void rxJavaMapExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        LogUtils.error(TAG, "rxJavaMapExample--Function--:" + Thread.currentThread().getName() + "--:" + integer);
                        return "this is from map " + integer;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxLifeCycleUtils.<String>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        LogUtils.error(TAG, "rxJavaMapExample--Consumer--:" + Thread.currentThread().getName() + "--:" + s);
                    }
                });

    }

    /**
     * RxJava-zip语法练习
     */
    @SuppressLint("CheckResult")
    public void rxJavaZipExample() {
        Observable.zip(model.getRxJavaCreateExampleData().subscribeOn(Schedulers.io()), model.getRxJavaStringData().subscribeOn(Schedulers.io())
                , new BiFunction<Integer, String, String>() {
                    @Override
                    public String apply(Integer integer, String s) {
                        LogUtils.error(TAG, "rxJavaZipExample--zip--:" + Thread.currentThread().getName() + "--:" + integer + "--:" + s);
                        return "this is zip method --:" + integer + "--:" + s;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<String>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        LogUtils.error(TAG, "rxJavaZipExample--Consumer--:" + Thread.currentThread().getName() + "--:" + s);
                    }
                });


    }

    /**
     * RxJava语法-Concat练习
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void rxJavaConcatExample() {
        Observable.concat(model.getRxJavaCreateExampleData().subscribeOn(Schedulers.io()), model.getRxJavaStringData().subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Serializable>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(Serializable serializable) {
                        LogUtils.error(TAG, "rxJavaConcatExample--Consumer--:" + Thread.currentThread().getName() + "--:" + serializable);
                    }
                });

    }

    /**
     * RxJava语法-FlatMap练习
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void rxJavaFlatMapExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFlatMapExample--flatMap--:" + Thread.currentThread().getName() + "--:" + integer);
                        List<String> list = new ArrayList<>();
                        int count = 3;
                        for (int i = 0; i < count; i++) {
                            list.add("this is flatMap--" + integer + "---i--:" + i);
                        }
                        long delayTime = 1 + new Random().nextInt(10);
                        return Observable.fromIterable(list).delay(delayTime, TimeUnit.SECONDS);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<String>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        LogUtils.error(TAG, "rxJavaFlatMapExample--Consumer--:" + Thread.currentThread().getName() + "--:" + s);
                    }
                });

    }

    /**
     * RxJava语法-ConcatMap练习
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void rxJavaConcatMapExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) {
                        LogUtils.error(TAG, "rxJavaConcatMapExample--concatMap--:" + Thread.currentThread().getName() + "--:" + integer);
                        List<String> list = new ArrayList<>();
                        int count = 3;
                        for (int i = 0; i < count; i++) {
                            list.add("this is ConcatMap--" + integer + "---i--:" + i);
                        }
                        long delayTime = 1 + new Random().nextInt(10);
                        //delay方法内部切换了线程
                        return Observable.fromIterable(list).delay(delayTime, TimeUnit.SECONDS);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<String>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        LogUtils.error(TAG, "rxJavaConcatMapExample--Consumer--:" + Thread.currentThread().getName() + "--:" + s);
                    }
                });

    }

    /**
     * RxJava语法-distinct练习
     */
    @SuppressLint("CheckResult")
    public void rxJavaDistinctExample() {
        model.getRxJavaDistinctData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaDistinctExample--Consumer--:" + Thread.currentThread().getName() + "--:" + integer);
                    }
                });

    }

    /**
     * RxJava语法-filter练习
     */
    @SuppressLint("CheckResult")
    public void rxJavaFilterExample() {
        model.getRxJavaDistinctData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFilterExample--filter--:" + Thread.currentThread().getName() + "--:" + integer);
                        return integer > 1;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFilterExample--Consumer--:" + Thread.currentThread().getName() + "--:" + integer);
                    }
                });

    }

    /**
     * RxJava语法-buffer
     */
    @SuppressLint("CheckResult")
    public void rxJavaBufferExample() {
        model.getRxJavaDistinctData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .buffer(3, 2)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<List<Integer>>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) {
                        LogUtils.error(TAG, "rxJavaBufferExample--Consumer--:" + Thread.currentThread().getName() + "--size:" + integers.size());
                        for (int integer : integers) {
                            LogUtils.error(TAG, "rxJavaBufferExample--Consumer--:" + Thread.currentThread().getName() + "--value:" + integer + "\n");
                        }
                    }
                });

    }

    /**
     * RxJava语法-time练习
     */
    @SuppressLint("CheckResult")
    public void rxJavaTimeExample() {
        LogUtils.error(TAG, "rxJavaTimeExample--start--:" + Thread.currentThread().getName() + "--:" + TimeUtils.getNowString());
        Observable.timer(4, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Long>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        LogUtils.error(TAG, "rxJavaTimeExample--end--:" + Thread.currentThread().getName() + "--:" + TimeUtils.getNowString() + "---long:" + aLong);
                    }
                });

    }

    /**
     * rxJava-interval
     */
    @SuppressLint("CheckResult")
    public void rxJavaIntervalExample() {
        LogUtils.error(TAG, "rxJavaIntervalExample--start--:" + Thread.currentThread().getName() + "--:" + TimeUtils.getNowString());
        Observable.interval(3, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Long>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        LogUtils.error(TAG, "rxJavaIntervalExample--end--:" + Thread.currentThread().getName() + "--:" + TimeUtils.getNowString() + "---long:" + aLong);
                    }
                });

    }

    /**
     * RxJava -doOnNext
     */
    @SuppressLint("CheckResult")
    public void rxJavaDoOnNextExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaDoOnNextExample--:" + Thread.currentThread().getName() + "-doOnNext-:" + integer);
                    }
                })
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaDoOnNextExample--:" + Thread.currentThread().getName() + "-doAfterNext-:" + integer);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        LogUtils.error(TAG, "rxJavaDoOnNextExample--:" + Thread.currentThread().getName() + "-doOnComplete-:");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() {
                        LogUtils.error(TAG, "rxJavaDoOnNextExample--:" + Thread.currentThread().getName() + "-doFinally-:");
                    }
                })
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaDoOnNextExample--:" + Thread.currentThread().getName() + "-Consumer-:" + integer);
                    }
                });

    }

    /**
     * skip(long count) count 跳过count个
     */
    @SuppressLint("CheckResult")
    public void rxJavaSkipExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .skip(2)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaSkipExample--:" + Thread.currentThread().getName() + "-Consumer-:" + integer);
                    }
                });

    }

    /**
     * take(long count) count 能接收的最多count个数据
     */
    @SuppressLint("CheckResult")
    public void rxJavaTakeExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .take(2)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaTakeExample--:" + Thread.currentThread().getName() + "-Consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaSingleExample() {
        Single.just(1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new SingleObserver<Integer>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LogUtils.error(TAG, "rxJavaSingleExample--:" + Thread.currentThread().getName() + "-onSuccess-:" + integer);
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(TAG, "rxJavaSingleExample--:" + Thread.currentThread().getName() + "-onError-:" + e.toString());
                        disposable.dispose();
                    }
                });

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void rxJavaDebounceExample() {
        model.getRxJavaDebounceData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .debounce(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaDebounceExample--:" + Thread.currentThread().getName() + "-consumer-:" + integer);
                    }
                });

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void rxJavaDeferExample() {
        Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() {
                return model.getRxJavaCreateExampleData();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        LogUtils.error(TAG, "rxJavaDeferExample--:" + Thread.currentThread().getName() + "-consumer-:" + o.toString());
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaLastExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .last(4)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaDeferExample--:" + Thread.currentThread().getName() + "-consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaMergeExample() {
        Observable.merge(model.getRxJavaCreateExampleData().subscribeOn(Schedulers.io()), model.getRxJavaStringData().subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Serializable>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(Serializable serializable) {
                        LogUtils.error(TAG, "rxJavaMergeExample--:" + Thread.currentThread().getName() + "-consumer-:" + serializable);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaReduceExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) {
                        LogUtils.error(TAG, "rxJavaReduceExample--:" + Thread.currentThread().getName() + "-reduce-:" + integer + "---" + integer2);
                        return integer + integer2;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaReduceExample--:" + Thread.currentThread().getName() + "-consumer-:" + integer);
                    }
                });

    }

    /**
     * range(final int start, final int count) start 初始值，count发送个数
     */
    @SuppressLint("CheckResult")
    public void rxJavaRangeExample() {
        Observable.range(-1, 10)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaRangeExample--:" + Thread.currentThread().getName() + "-consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaRepeatExample() {
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaRepeatExample--:" + Thread.currentThread().getName() + "-consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaScanExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) {
                        LogUtils.error(TAG, "rxJavaScanExample--:" + Thread.currentThread().getName() + "-scan-:" + integer + "---" + integer2);
                        return integer + integer2;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaReduceExample--:" + Thread.currentThread().getName() + "-consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaWindowExample() {
        model.getRxJavaDistinctData()
                .window(3)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Observable<Integer>>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Observable<Integer>>() {
                    @Override
                    public void accept(Observable<Integer> longObservable) {
                        LogUtils.error(TAG, "rxJavaReduceExample--:" + Thread.currentThread().getName() + "-consumer1-:");
                        longObservable.subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) {
                                        LogUtils.error(TAG, "rxJavaReduceExample--:" + Thread.currentThread().getName() + "-consumer2-:" + integer);
                                    }
                                });

                    }
                });

    }

    @SuppressLint("CheckResult")
    public void rxJavaSchedulersExample() {
        model.getRxJavaCreateExampleData()
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaSchedulersExample--:" + Thread.currentThread().getName() + "-doOnNext-:" + integer);
                    }
                })
                .observeOn(Schedulers.newThread())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        LogUtils.error(TAG, "rxJavaSchedulersExample--:" + Thread.currentThread().getName() + "-filter-:" + integer);
                        return integer > 2;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaSchedulersExample--:" + Thread.currentThread().getName() + "-Consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("all")
    public void rxJavaFlowableCreateExample() {
        model.getRxJavaFlowableData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new FlowableSubscriber<Integer>() {
                    // Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
//                        subscription= s;
//                        subscription.request(3);
                        LogUtils.error(TAG, "rxJavaFlowableCreateExample--:" + Thread.currentThread().getName() + "-onSubscribe-:" + "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFlowableCreateExample--:" + Thread.currentThread().getName() + "-onNext-:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.error(TAG, "rxJavaFlowableCreateExample--:" + Thread.currentThread().getName() + "-onError-:" + "onError--:0" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.error(TAG, "rxJavaFlowableCreateExample--:" + Thread.currentThread().getName() + "-onComplete-:" + "onComplete");
                    }
                });
    }

    @SuppressLint("all")
    public void rxJavaFlowableSizeExample() {
        model.getRxJavaFlowableSizeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new FlowableSubscriber<Integer>() {
                    Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(95);
                        LogUtils.error(TAG, "rxJavaFlowableSizeExample--:" + Thread.currentThread().getName() + "-onSubscribe-:");

                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFlowableSizeExample--:" + Thread.currentThread().getName() + "-onNext-:" + integer);

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.error(TAG, "rxJavaFlowableSizeExample--:" + Thread.currentThread().getName() + "-onError-:" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.error(TAG, "rxJavaFlowableSizeExample--:" + Thread.currentThread().getName() + "-onComplete-:");
                    }
                });
    }

    @SuppressLint("all")
    public void rxJavaFlowableRealExample() {
        model.getRxJavaFlowableRealExample()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(96);
                        LogUtils.error(TAG, "rxJavaFlowableRealExample--:" + Thread.currentThread().getName() + "-onSubscribe-:");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFlowableRealExample--:" + Thread.currentThread().getName() + "-onNext-:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.error(TAG, "rxJavaFlowableRealExample--:" + Thread.currentThread().getName() + "-onError-:" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.error(TAG, "rxJavaFlowableRealExample--:" + Thread.currentThread().getName() + "-onComplete-:");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void rxJavaFlowableConsumeExample() {
        model.getRxJavaFlowableRealExample()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Integer>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtils.error(TAG, "rxJavaFlowableConsumeExample--:" + Thread.currentThread().getName() + "-Consumer-:" + integer);
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void getMainData(String headerTimestamp, int id) {
        Map<String, String> requestParams = new HashMap<>(2);
        requestParams.put("platform", "100");
        requestParams.put("platform2", "200");
        model.getMainData(headerTimestamp, id, requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<MainDataBean>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new AbstractCustomerSubscribe<MainDataBean>() {
                    @Override
                    public void onNext(MainDataBean mainDataBean) {
                        LogUtils.error(TAG, "getMainData--:" + Thread.currentThread().getName() + "-onNext-:" + mainDataBean.toString());
                    }

                    @Override
                    public void onError(ResponseBean responseBean) {
                        super.onError(responseBean);
                        LogUtils.error(TAG, "getMainData--:" + Thread.currentThread().getName() + "-responseBean-:" + responseBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.error(TAG, "getMainData--:" + Thread.currentThread().getName() + "-Throwable-:" + e.toString());
                    }
                });

    }

    /**
     * 测试retrofit 注解URL 使用方式
     *
     * @param url url地址
     */
    @SuppressLint("CheckResult")
    public void getUrlData(String url) {
        model.getUrlData(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<UrlRequestBean>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<UrlRequestBean>() {
                    @Override
                    public void accept(UrlRequestBean urlRequestBeanResponse) {
                        LogUtils.error(TAG, "---------getUrlData-----------" + urlRequestBeanResponse.toString());
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void getBanner(String headerTimestamp, String time) {
        Map<String, String> requestParams = new HashMap<>(2);
        requestParams.put("platform", "100");
        requestParams.put("platform2", "200");
        model.getBanner(headerTimestamp, time, requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<BannerBean>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<BannerBean>() {
                    @Override
                    public void accept(BannerBean bannerBean) {
                        LogUtils.error(TAG, "---------getBanner-----------" + bannerBean.toString());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void postRegister(String username, String pwd, String rePwd) {
        RegisterParamBean registerParamBean = new RegisterParamBean();
        registerParamBean.setUsername(username);
        registerParamBean.setPassword(pwd);
        registerParamBean.setRepassword(rePwd);
        model.postRegister(registerParamBean)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<Response<Void>>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<Response<Void>>() {
                    @Override
                    public void accept(Response<Void> voidResponse) {
                        LogUtils.error(TAG, "postRegister--:" + Thread.currentThread().getName() + "-Consumer-:" + voidResponse.toString());
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void postRegisterBy(String username, String pwd, String rePwd) {
        HashMap<String, String> params = new HashMap<>(3);
        params.put("username", username);
        params.put("password", pwd);
        params.put("repassword", rePwd);
        model.postRegister(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<RegisterResponseBean>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<RegisterResponseBean>() {
                    @Override
                    public void accept(RegisterResponseBean registerResponseBeanResponse) {
                        LogUtils.error(TAG, "postRegisterBy--:" + Thread.currentThread().getName() + "-Consumer-:" + registerResponseBeanResponse.toString());
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void postLogin(String username, String pwd) {
        Map<String, String> params = new HashMap<>(2);
        params.put("username", username);
        params.put("password", pwd);
        model.postLogin(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<LoginResponseBean>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean response) {
                        LogUtils.error(TAG, "postLogin--:" + Thread.currentThread().getName() + "-Consumer-:" + response.toString());
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void postLoginAgain(String username, String password, String nick) {
        model.postLogin(username, password, nick)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifeCycleUtils.<LoginResponseBean>bindUntilEvent(view, ActivityEvent.DESTROY))
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) {
                        LogUtils.error(TAG, "postLoginAgain--:" + Thread.currentThread().getName() + "-Consumer-:" + loginResponseBean.toString());
                    }
                });

    }
}

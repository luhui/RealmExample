package com.cvte.realmexample;

import android.os.CountDownTimer;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * 一个简单的计时器，到点回调#onFinish()
 * Created by mluhui on 16/1/15.
 */
public class TimerTask extends CountDownTimer {

    private TimerTaskOnFinishedListener onFinishedListener;
    private PublishSubject<Long> ticker;
    private boolean isTicking;

    public TimerTask(long millisInFuture) {
        super(millisInFuture, 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (ticker != null) {
            ticker.onNext(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        isTicking = false;
        if (onFinishedListener != null) {
            onFinishedListener.onFinished();
        }
        if (ticker != null) {
            ticker.onCompleted();
        }
    }

    public void startTask(TimerTaskOnFinishedListener onFinishedListener) {
        if (isTicking) {
            cancel();
        }
        isTicking = true;
        this.onFinishedListener = onFinishedListener;
        start();
    }

    public Observable<Long> startTask() {
        startTask(null);
        ticker = PublishSubject.create();
        return ticker;
    }

    public void stop() {
        isTicking = false;
        cancel();
    }

    public boolean isTicking() {
        return isTicking;
    }

    public interface TimerTaskOnFinishedListener {
        void onFinished();
    }
}

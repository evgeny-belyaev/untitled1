import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class IndeterminateProgressOperator<T> implements Observable.Operator<T, T>
{
    public interface ICallbacks
    {
        void hideProgress();

        void showProgress();
    }

    private ICallbacks mCallbacks;

    public IndeterminateProgressOperator(final PublishSubject<Boolean> progressStateSubject)
    {
        mCallbacks = new ICallbacks()
        {
            @Override
            public void hideProgress()
            {
                progressStateSubject.onNext(false);
            }

            @Override
            public void showProgress()
            {
                progressStateSubject.onNext(true);
            }
        };
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber)
    {
        final Subscription progressSubscription = Observable.just(true)
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribe(new Action1<Boolean>()
            {
                @Override
                public void call(Boolean aBoolean)
                {
                    mCallbacks.showProgress();
                }
            });

        return new Subscriber<T>()
        {
            @Override
            public void onCompleted()
            {
                progressSubscription.unsubscribe();
                mCallbacks.hideProgress();
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable throwable)
            {
                progressSubscription.unsubscribe();
                mCallbacks.hideProgress();
                subscriber.onError(throwable);
            }

            @Override
            public void onNext(T t)
            {
                subscriber.onNext(t);
            }
        };
    }
}

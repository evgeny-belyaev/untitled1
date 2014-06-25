import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);
        final PublishSubject<Long> delayed = PublishSubject.create();

        source.subscribe(new Action1<Long>()
        {
            @Override
            public void call(Long aLong)
            {
                log(aLong + "");
                delayed.onNext(aLong);
            }
        });



        delayed
            .delay(2, TimeUnit.SECONDS)
            .subscribe(new Action1<Long>()
            {
                @Override
                public void call(Long aLong)
                {
                    log("\t\t\tdelayed " + aLong);
                }
            });

        while (true)
        {
            sleep(50);
        }
    }

    private static void sleep(int n)
    {
        try
        {
            Thread.sleep(n);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static void log(String message)
    {
        System.out.println(message);
    }
}



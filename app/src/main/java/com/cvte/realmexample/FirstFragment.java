package com.cvte.realmexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cvte.realmexample.model.Event;
import com.cvte.realmexample.model.Games;
import com.cvte.realmexample.model.Users;

import java.util.UUID;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmObject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class FirstFragment extends Fragment {

    @Bind(R.id.textView)
    TextView textView;

    private Subscription subscription;
    Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        //1、update game and event at the same time
        updateGame();
        updateEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        textView.setText("");
        //2、listen all data change

        /**
         *high reproducibility
                io.realm.internal.async.BadVersionException: std::exception in io_realm_internal_TableQuery.cpp line 1094
                or java.lang.IllegalStateException: Caller thread behind the worker thread
                will throw 
         */
        subscription = realm.where(Games.class).findAllAsync().asObservable().subscribe(games -> {
            for (Games g :
                    games) {
                textView.append("first - game name: " + g.getName());
            }
        });

        //low reproducibility
//        subscription = realm.where(Games.class).findFirstAsync().asObservable().filter(RealmObject::isLoaded).cast(Games.class).subscribe(games -> {
//            textView.setText(games.getName());
//        });
    }

    @Override
    public void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }

    //assume updateGame data
    private void updateGame() {
        Log.e("lh", "begin updateGame first");
        new RealmAsyncTransaction().executeTransactionObservable(realm1 -> {
            Log.e("lh", "updating first");
            String updateJson = "{\n" +
                    "  \"id\": \"id1\",\n" +
                    "  \"name\": \"Hello World\",\n" +
                    "  \"games\": [\n" +
                    "    {\n" +
                    "      \"id\": \"id1\",\n" +
                    "      \"name\": \"games1\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"id2\",\n" +
                    "      \"name\": \"games2\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"id3\",\n" +
                    "      \"name\": \"games3\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            realm1.createOrUpdateAllFromJson(Users.class, updateJson);
        });
    }

    private void updateEvent() {
        String id = UUID.randomUUID().toString();
        new RealmAsyncTransaction().executeTransactionObservable(realm1 -> {
            Event games = new Event();
            games.setId(id);
            games.setName("test-name!!");
            realm1.copyToRealmOrUpdate(games);
        }).subscribe(aBoolean -> {
            Log.e("lh", "save " + id + " " + aBoolean + " result ...." + Realm.getDefaultInstance().allObjects(Event.class));
        });
    }

    public static class RealmAsyncTransaction {
        public Observable<Boolean> executeTransactionObservable(Realm realm, Realm.Transaction transaction) {
            return executeTransactionObservable(realm, transaction, false);
        }

        public Observable<Boolean> executeTransactionObservable(Realm.Transaction transaction) {
            return executeTransactionObservable(Realm.getDefaultInstance(), transaction, true);
        }

        private Observable<Boolean> executeTransactionObservable(Realm realm, Realm.Transaction transaction, boolean autoClose) {
            return Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    if (realm.isClosed()) {
                        subscriber.onCompleted();
                        return;
                    }
                realm.executeTransaction(transaction, new Realm.Transaction.Callback() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        if (autoClose) {
                            realm.close();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("realm", "execute transaction failure", e);
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                        if (autoClose) {
                            realm.close();
                        }
                    }
                });
                }
            });
        }
    }
}

package com.cvte.realmexample;


import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cvte.realmexample.model.Event;
import com.cvte.realmexample.model.Games;
import com.cvte.realmexample.model.Users;
import com.cvte.realmexample.model.dao.RealmDao;
import com.cvte.realmexample.tabbar.TabBarActivity;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private TextView textView;
    private Subscription subscription;
    private Realm realm;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        textView = (TextView)view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = realm.where(Event.class)
                .findAllAsync()
                .asObservable()
                .filter(events -> events.isLoaded())
                .subscribe(event -> {
                    textView.setText("");
                    for (Event e :
                            event) {
                        textView.append("second - event id: " + e.getId());
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }
}

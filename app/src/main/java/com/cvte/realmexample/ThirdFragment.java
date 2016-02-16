package com.cvte.realmexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cvte.realmexample.model.Games;
import com.cvte.realmexample.model.Users;
import com.cvte.realmexample.model.dao.RealmDao;

import io.realm.Realm;
import rx.Subscription;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {


    private TextView textView;
    private Subscription subscription;
    private Realm realm;

    public ThirdFragment() {
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
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        textView = (TextView)view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = RealmDao
                .getDao(Users.class)
                .getAsyncObservable("id1", realm)
                .subscribe(user -> {
                    textView.setText("third - user name: " + user.getName());
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

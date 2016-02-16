package com.cvte.realmexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cvte.realmexample.model.Games;
import com.cvte.realmexample.model.Users;
import com.cvte.realmexample.model.dao.RealmDao;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Subscription;

/**
 * Created by mluhui on 16/2/1.
 */
public class FourthFragment extends Fragment {
    @Bind(R.id.textView)
    TextView textView;

    private Subscription subscription;
    Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
//        update();
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
        Log.e("lh", "did bind");
        subscription = RealmDao
                .getDao(Games.class)
                .getAsyncObservable("id1", realm)
                .subscribe(games -> {
                    textView.setText("fourth - game name: " + games.getName());
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }
}

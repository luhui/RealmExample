package com.cvte.realmexample;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cvte.realmexample.model.Users;
import com.cvte.realmexample.tabbar.TabBarActivity;
import com.cvte.realmexample.tabbar.TabBarItem;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class BackgroundActivity extends TabBarActivity {

    private HashMap<Class, Fragment> fragmentHashMap = new HashMap<>();
    private Class[] tabs = {FirstFragment.class, SecondFragment.class, ThirdFragment.class, FourthFragment.class};
    private Realm realm;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("first", 0);
        realm = Realm.getDefaultInstance();
        if (!sharedPreferences.contains("first")) {
            //first init, create database
            realm.beginTransaction();
            realm.createOrUpdateObjectFromJson(Users.class, "{\n" +
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
                    "}");
            realm.commitTransaction();
            sharedPreferences.edit().putBoolean("first", true).apply();
        }
        TabBarItem firstTabBar = new TabBarItem(R.string.first, 0, 0, FirstFragment.class.getName());
        TabBarItem secondTabBar = new TabBarItem(R.string.second, 0, 0, SecondFragment.class.getName());
        TabBarItem thirdTabBar = new TabBarItem(R.string.third, 0, 0, ThirdFragment.class.getName());
        TabBarItem fourthTabBar = new TabBarItem(R.string.fourth, 0, 0, FourthFragment.class.getName());

        TabBarItem[] tabBarItems = {firstTabBar, secondTabBar, thirdTabBar, fourthTabBar};
        setTabBarItems(tabBarItems);
    }
}

package com.example.team1288.c2h6o;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by ssoso on 2017-07-12.
 */

public class Beer_mainFragment extends Fragment implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.beer_main, container, false);

        Log.d(getClass().toString(), "맥주페이지 생성");

        DB_Beer db_info_beer = new DB_Beer(getActivity());
        db = db_info_beer.getDB();
        Cursor cursor = db.rawQuery("SELECT * FROM " + db_info_beer.getTableName(), null);

        ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) rootView.findViewById(R.id.beerlist);
        listview.setAdapter(adapter);

        // db에 저장된 data를 아이템으로 추가
        cursor.moveToFirst(); // 첫번째 row

        while (!cursor.isAfterLast())
        {
            int idNum = cursor.getInt(0);
            String name = cursor.getString(1);
            int degree = cursor.getInt(2);
            int price = cursor.getInt(3);
            String explain = cursor.getString(4);

            // add item
            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_beer),
                idNum, name, degree, price, explain, ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow)) ;

            cursor.moveToNext(); // 다음 row
        }
        cursor.close();

        Log.d(getClass().toString(), "db data 넣기 완료");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        // get item
        ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(position) ;

        // name, degree, price, explain 을 넘겨주면서 detail 페이지로 Fragment 교체
        Bundle args = new Bundle();

        args.putString("str_name", item.getName());
        args.putInt("int_degree", item.getDegree());
        args.putInt("int_price", item.getPrice());
        args.putString("str_explain", item.getExplain());

        Fragment fragment = new Beer_detailFragment();
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.l_main_fragment, fragment).commit();
    }
}

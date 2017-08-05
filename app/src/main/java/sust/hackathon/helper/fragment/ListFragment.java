package sust.hackathon.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import sust.hackathon.helper.R;
import sust.hackathon.helper.database.MyDBFunction;

/**
 * Created by joy on 8/5/17.
 */

public class ListFragment extends Fragment {

    ListView listView;
    TextView textView;
    private FloatingActionButton floatingActionButton;
    private MyDBFunction dt;
    private ListAdapter listAdapter;
    Cursor c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_list,container,false);
        listView=(ListView)view.findViewById(R.id.listView);
        textView= (TextView) view.findViewById(R.id.text_empty);
        //textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/Aller_Rg.ttf"));
        textView.setText("List Empty !");


        dt=new MyDBFunction(getContext());


        c= dt.my_data();

        listAdapter=new sust.hackathon.helper.adapter.ListAdapter(getContext(),c);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i= (int) parent.getAdapter().getItemId(position);
                System.out.println(" i  "+i);
                System.out.println(position);
                //Intent intent=new Intent(getContext(),ShowNoteActivity.class);
                //intent.putExtra("KEY",i);
                //startActivity(intent);
            }
        });
        listView.setEmptyView(textView);

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

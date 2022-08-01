package com.example.sketcher;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;


public class DrawingFragment extends Fragment {

    private DrawingView dv = null;
    private Activity containerActivity = null;
    private ArrayList<String> list;


    public DrawingFragment() {
        super(R.layout.activity_drawing_fragment);
        list = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getContacts();
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    private void getContacts(){
        Cursor cursor = containerActivity.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            list.add(name + " :: " + contactId);
        }
        cursor.close();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_drawing_fragment, container, false);
        dv = new DrawingView(getActivity(), null);
        LinearLayout ll = view.findViewById(R.id.drawing);
        ll.addView(dv);
        view.findViewById(R.id.color_1).setOnClickListener(e -> dv.changeColor(
                getActivity(), 'r'));
        view.findViewById(R.id.color_2).setOnClickListener(e -> dv.changeColor(
                getActivity(), 'g'));
        view.findViewById(R.id.color_3).setOnClickListener(e -> dv.changeColor(
                getActivity(), 'b'));
        view.findViewById(R.id.color_4).setOnClickListener(e -> dv.changeColor(
                getActivity(), 'p'));
        view.findViewById(R.id.small).setOnClickListener(e -> dv.changeStroke('s'));
        view.findViewById(R.id.medium).setOnClickListener(e -> dv.changeStroke('m'));
        view.findViewById(R.id.large).setOnClickListener(e -> dv.changeStroke('l'));
        view.findViewById(R.id.clear).setOnClickListener(e -> dv.startNew());

        view.findViewById(R.id.share).setOnClickListener(e -> {
            dv.imageFile();
            ShareFragment sf = new ShareFragment();

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("list", list);
            sf.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().
                    beginTransaction();
            transaction.replace(R.id.frag_view, sf);
            transaction.addToBackStack(null);
            transaction.commit();

        });
        return view;
    }
}
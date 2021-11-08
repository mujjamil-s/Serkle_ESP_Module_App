package com.almaharapvtltd.serkle.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.almaharapvtltd.serkle.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllDevicesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllDevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllDevicesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DataFragAdapter adapter;
    ViewPager2 viewPager2;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AllDevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllDevicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllDevicesFragment newInstance(String param1, String param2) {
        AllDevicesFragment fragment = new AllDevicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


public static class DataFragAdapter extends FragmentStateAdapter {

        ArrayList<Items> itemsArrayListInAdap1, itemsArrayListInAdap2;

    public DataFragAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Items> list1,ArrayList<Items> list2) {
        super(fragmentActivity);
        this.itemsArrayListInAdap1 = list1;
        this.itemsArrayListInAdap2 = list2;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment fragment = new SingleFragment(itemsArrayListInAdap1);
                return fragment;
            case 1:
                Fragment fragment1 = new SingleFragment(itemsArrayListInAdap2);
                return fragment1;

        }
        return new SingleFragment(itemsArrayListInAdap1);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

public static class SingleFragment extends Fragment{
public static int ARG_OBJECT =0;
public static ArrayList<Items> itemsArrayList = new ArrayList<>();
GridView itemGrid;
FragmentAdapter fragmentAdapter;
public SingleFragment(ArrayList<Items> itemsArrayList){
   SingleFragment.itemsArrayList = itemsArrayList;

}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  =  inflater.inflate(R.layout.fragment_home, container,true);
//        ARG_OBJECT = this.getArguments().getInt("ARG_OBJECT");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // TextView textView = view.findViewById(R.id.text_test);
       // textView.setText("This is "+ ARG_OBJECT + "tab");
        itemGrid = view.findViewById(R.id.grid_view_items);
        fragmentAdapter = new FragmentAdapter(getContext(), itemsArrayList);
        itemGrid.setAdapter(fragmentAdapter);

    }

public class FragmentAdapter extends BaseAdapter{

        public Context context;
       public ArrayList<Items> ItemList;
        public FragmentAdapter(Context context, ArrayList<Items> itemList ){
            this.context = context;
            this.ItemList = itemList;
        }
    @Override
    public int getCount() {
        return this.ItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.ItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View itemView = layoutInflater.inflate(R.layout.item, parent, false);
        TextView item_text  = itemView.findViewById(R.id.card_item_text);
        final Items item = this.ItemList.get(position);
        item_text.setText(item.getItemName());
                final ToggleButton switchButton = itemView.findViewById(R.id.toggle_on_of);
                switchButton.setChecked(item.itemCheckState);
               switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if(isChecked){
                           Toast.makeText(getContext(), "Button is Checked", Toast.LENGTH_LONG).show();
                           item.setItemCheckState(true);
                       } else {
                           Toast.makeText(getContext(), "Button is Not Checked", Toast.LENGTH_LONG).show();
                           item.setItemCheckState(false);

                       }
                   }
               });
               return itemView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
}
}



public static class Items {
    private String itemName;

    private String roomName;
    private Drawable itemImage;
    private boolean itemCheckState;

    public Items(String itemName, String roomName, Drawable itemImage, boolean itemCheckState) {
        this.itemName = itemName;
        this.roomName = roomName;
        this.itemImage = itemImage;
        this.itemCheckState = itemCheckState;
    }
    public Items (String itemName, boolean itemCheckState){
        this.itemName = itemName;
        this.itemCheckState = itemCheckState;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Drawable getItemImage() {
        return itemImage;
    }

    public void setItemImage(Drawable itemImage) {
        this.itemImage = itemImage;
    }

    public boolean isItemCheckState() {
        return itemCheckState;
    }

    public void setItemCheckState(boolean itemCheckState) {
        this.itemCheckState = itemCheckState;
    }


}
}
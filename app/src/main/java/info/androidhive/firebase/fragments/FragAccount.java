package info.androidhive.firebase.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.R;

/**
 * Created by Samit
 */
public class FragAccount extends Fragment {
    RecyclerView recyclerView;
    private static List<DealDetailModel> demoData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Frgment ","onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Frgment ","onCreateView");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("Frgment ","onActivityCreated");

        super.onActivityCreated(savedInstanceState);

        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.myList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        demoData = new ArrayList<DealDetailModel>();
        char c = 'A';
        for (byte i = 0; i < 20; i++) {
            DealDetailModel dealDetailModel = new DealDetailModel();
            dealDetailModel.name = ""+c++;
            dealDetailModel.age = (byte) (20 + i);
            demoData.add(dealDetailModel);
        }
        /*adapter = new RecyclerViewAdapter(getActivity(),demoData,null);
        recyclerView.setAdapter(adapter);*/
    }


    @Override
    public void onAttach(Context context) {
        Log.d("Frgment ","onAttach");

        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Frgment ","onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Frgment ","onResume");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Frgment ","onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Frgment ","onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Frgment ","onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Frgment ","onDetach");
    }
}

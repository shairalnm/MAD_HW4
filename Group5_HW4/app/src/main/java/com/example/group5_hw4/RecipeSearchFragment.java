package com.example.group5_hw4;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class RecipeSearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<String> ingredients_list = new ArrayList<>();
    private RecipeSearchFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    Button searchButton;
    TextView textViewDish;
    EditText searcheditText;


    public RecipeSearchFragment() {

    }

    public static RecipeSearchFragment newInstance(String param1, String param2) {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
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
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Recipe Puppy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        recyclerView = view.findViewById(R.id.ingredients_recyclerView);
        recyclerView.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        adapter = new IngredientsListAdapter(ingredients_list);
        recyclerView.setAdapter(adapter);

        if(ingredients_list.size() == 0) {
            ingredients_list.add("");
            adapter.notifyDataSetChanged();
        }

        searcheditText = view.findViewById(R.id.searcheditText);
        searchButton = view.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searcheditText.getText().length() > 0)
                {
                    RequestParams url = new RequestParams();
                    StringBuilder stringBuilder = new StringBuilder();
                    if(ingredients_list.get(0).length() > 0) 
                    {
                        stringBuilder.append(ingredients_list.get(0));
                    }
                    for(int i = 1; i < ingredients_list.size(); i++)
                    {
                        if(ingredients_list.get(i).length() > 0) 
                        {
                            stringBuilder.append("," + ingredients_list.get(i));
                        }
                    }

                    url.addParameter("q", searcheditText.getText().toString());
                    url.addParameter("i", stringBuilder.toString());
                    String urlStr = url.getEncodedUrl("http://www.recipepuppy.com/api/");
                    mListener.recipeLoadFragmentFunction(urlStr);
                }
                else{
                    Toast.makeText(getActivity(), "Please enter the values.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeSearchFragmentInteractionListener) {
            mListener = (RecipeSearchFragmentInteractionListener) context;
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


    public interface RecipeSearchFragmentInteractionListener {
        void recipeLoadFragmentFunction(String url);
    }
}

package com.tomybdeveloper.mimoteka.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tomybdeveloper.mimoteka.R;
import com.tomybdeveloper.mimoteka.model.Meme;
import com.tomybdeveloper.mimoteka.util.MimotekaAPI;

import java.util.ArrayList;
import java.util.List;


public class MemeListFragment extends Fragment {

    private MimotekaAPI mimotekaAPI;
    private RecyclerView recyclerView;
    private MemeAdapter memeAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView noMemePosted;
    private List<Meme> memeList;

    private final CollectionReference collectionReference = db.collection("Meme");

    public MemeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_meme_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mimotekaAPI = MemeListFragmentArgs.fromBundle(getArguments()).getUserApi();
        noMemePosted = view.findViewById(R.id.noMemeId);

        memeList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_addPost:

                Navigation.findNavController(requireActivity() , R.id.fragmentContainerView).navigate(R.id.action_memeListFragment_to_postFragment);
//                NavDirections action = MemeListFragmentDirections.actionMemeListFragmentToPostFragment(mimotekaAPI);
//                Navigation.findNavController(requireActivity() , R.id.fragmentContainerView).navigate(action);

                break;

            case R.id.action_signout:
                if (firebaseUser != null && firebaseAuth != null ) {
                    firebaseAuth.signOut();
                    Navigation.findNavController(requireActivity() , R.id.fragmentContainerView).navigate(R.id.action_memeListFragment_to_firstFragment);

                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot memes : queryDocumentSnapshots) {

                                Meme meme = memes.toObject(Meme.class);
                                memeList.add(meme);

                            }


                            memeAdapter = new MemeAdapter(getContext(), memeList);
                            recyclerView.setAdapter(memeAdapter);
                            memeAdapter.notifyDataSetChanged();

                        } else {
                            noMemePosted.setVisibility(View.VISIBLE);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
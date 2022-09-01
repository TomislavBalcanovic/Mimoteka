package com.tomybdeveloper.mimoteka.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomybdeveloper.mimoteka.R;

import java.util.Objects;


public class FirstFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private Button buttonFirst;

    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // access do support action bara u fragmentu
        //Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();


        buttonFirst = view.findViewById(R.id.buttonFirst);

        buttonFirst.setOnClickListener(view1 -> Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(R.id.action_firstFragment_to_loginFragment));

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        currentUser = firebaseAuth.getCurrentUser();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        if (firebaseAuth != null){
//            firebaseAuth.removeAuthStateListener(authStateListener);
//        }
//    }
}
package com.tomybdeveloper.mimoteka.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tomybdeveloper.mimoteka.R;
import com.tomybdeveloper.mimoteka.util.MimotekaAPI;


public class LoginFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private EditText editTextPw;
    private ProgressBar progressBar;
    private Button buttonLog;
    private Button buttonCr;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");


    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();


        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextLoginEmail);
        editTextPw = view.findViewById(R.id.editTextLoginPw);
        progressBar = view.findViewById(R.id.loginProggressBar);
        buttonLog = view.findViewById(R.id.buttonLogin);
        buttonCr = view.findViewById(R.id.buttonCrAcc2);
        editTextPw.setTextColor(getResources().getColor(R.color.Light_french_beige));
        autoCompleteTextView.setTextColor(getResources().getColor(R.color.Light_french_beige));

        buttonCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(R.id.action_loginFragment_to_createAccFragment);
            }
        });

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginEmailPasswordUser(editTextPw.getText().toString(), autoCompleteTextView.getText().toString());


            }
        });

    }

    private void loginEmailPasswordUser(String email, String pwd) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String currentUserId = firebaseUser.getUid();

                            collectionReference.whereEqualTo("userId", currentUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    assert value != null;
                                    if (!value.isEmpty()) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                        for (QueryDocumentSnapshot snapshot : value) {

                                            MimotekaAPI mimotekaAPI = MimotekaAPI.getInstance();
                                            mimotekaAPI.setUserId(snapshot.getString("userId"));
                                            mimotekaAPI.setUsername(snapshot.getString("username"));


//                                            NavDirections action = LoginFragmentDirections.actionLoginFragmentToMemeListFragment(null);
//                                            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);

//                                       Navigation.findNavController(requireActivity() , R.id.fragmentContainerView).navigate(R.id.action_loginFragment_to_memeListFragment);

                                       NavDirections action = LoginFragmentDirections.actionLoginFragmentToMemeListFragment(mimotekaAPI);
                                       Navigation.findNavController(requireActivity() , R.id.fragmentContainerView).navigate(action);

                                        }

                                    }


                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
        } else {
            Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_LONG).show();
        }

    }
}
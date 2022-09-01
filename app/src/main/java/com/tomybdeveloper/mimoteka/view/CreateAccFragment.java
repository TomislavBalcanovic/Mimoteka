package com.tomybdeveloper.mimoteka.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomybdeveloper.mimoteka.R;
import com.tomybdeveloper.mimoteka.util.MimotekaAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateAccFragment extends Fragment {

    private Button button;
    private EditText editTextUser;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");


    public CreateAccFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_acc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        button = view.findViewById(R.id.buttonCrAcc);
        editTextUser = view.findViewById(R.id.editTextCrAcc);
        editTextPassword = view.findViewById(R.id.editTextCrPw);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        progressBar = view.findViewById(R.id.createAccProgressBar);


//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                firebaseUser = firebaseAuth.getCurrentUser();
//
//                if (firebaseUser != null){
//                    //user logged in
//                }
//                else {
//                    // no user yet
//                }
//            }
//        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String username = editTextUser.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(username)) {

                    createUserEmailAccount(email, password, username);

                    Toast.makeText(requireContext(), "Everything is ok", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(requireContext(), "Empty Fields not allowed", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void createUserEmailAccount(String email, String password, String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(username)) {

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String currentUserId = firebaseUser.getUid();

                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("username", username);
                                userObj.put("userId", currentUserId);

                                collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.getResult().exists()) {

                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String name = task.getResult().getString("username");

                                                                    MimotekaAPI mimotekaAPI = MimotekaAPI.getInstance();
                                                                    mimotekaAPI.setUsername(name);
                                                                    mimotekaAPI.setUserId(currentUserId);

                                                                    NavDirections action = CreateAccFragmentDirections.actionCreateAccFragmentToMemeListFragment(mimotekaAPI);
                                                                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
//                                                                    requireActivity().finish();

                                                                }


                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


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

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        firebaseUser = firebaseAuth.getCurrentUser();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }
}


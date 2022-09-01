package com.tomybdeveloper.mimoteka.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tomybdeveloper.mimoteka.R;
import com.tomybdeveloper.mimoteka.model.Meme;
import com.tomybdeveloper.mimoteka.util.MimotekaAPI;

import java.util.Date;



public class PostFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PostFragment";
    private static final int GALLERY_CODE = 1;
    private static final int RESULT_OK = -1;
    private ImageView imageViewPost;
    private ImageView addPostButton;
    private TextView usernameTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button button;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private MimotekaAPI mimotekaAPI;


    private String currentUserId;
    private String currentUserName;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference collectionReference = db.collection("Meme");
    private StorageReference storageReference;
    private Uri imageUri;


    public PostFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

//        mimotekaAPI = PostFragmentArgs.fromBundle(getArguments()).getUserApi2();

        imageViewPost = view.findViewById(R.id.imageViewPost);
        addPostButton = view.findViewById(R.id.postIdButtonCamera);
        usernameTextView = view.findViewById(R.id.textViewPostUser);
        titleEditText = view.findViewById(R.id.editTextTitlePost);
        descriptionEditText = view.findViewById(R.id.editTextDescriptionPost);
        button = view.findViewById(R.id.buttonPostSave);
        progressBar = view.findViewById(R.id.postProgressBar);

        if (MimotekaAPI.getInstance() != null) {
            currentUserId = MimotekaAPI.getInstance().getUserId();
            currentUserName = MimotekaAPI.getInstance().getUsername();

            usernameTextView.setText(currentUserName);
        }

        addPostButton.setOnClickListener(this);
        button.setOnClickListener(this);

        progressBar.setVisibility(View.INVISIBLE);

//        authStateListener = firebaseAuth -> {
//            firebaseUser = firebaseAuth.getCurrentUser();
//            if (firebaseUser != null) {
//
//            }
//            else {
//
//            }
//
//        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPostSave:
                if (firebaseUser != null && firebaseAuth != null) {
                    saveMeme();
                }
                break;

            case R.id.postIdButtonCamera:

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;
        }
    }

    private void saveMeme() {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null) {

            StorageReference filepath = storageReference.child("meme_images").child("my_image" + Timestamp.now().getSeconds());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageUrl = uri.toString();

                                    Meme meme = new Meme();
                                    meme.setTitle(title);
                                    meme.setDescription(description);
                                    meme.setTimeAdded(new Timestamp(new Date()));
                                    meme.setUserName(currentUserName);
                                    meme.setUserId(currentUserId);
                                    meme.setImageUrl(imageUrl);


                                    collectionReference.add(meme).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            String name = task.getResult().getString("username");

                                                            mimotekaAPI = MimotekaAPI.getInstance();
                                                            mimotekaAPI.setUserId(currentUserId);
                                                            mimotekaAPI.setUsername(name);

//                                                            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(R.id.action_postFragment_to_memeListFragment);
                                                            NavDirections action = PostFragmentDirections.actionPostFragmentToMemeListFragment(mimotekaAPI);
                                                            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
//                                                            requireActivity().finish();


                                                        }

                                                    });


                                                }

                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                                }
                                            });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                imageViewPost.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
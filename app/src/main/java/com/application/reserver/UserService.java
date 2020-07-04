package com.application.reserver;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserService {
    private static final UserService instance = new UserService();
    private static final String TAG = "UserService";
    /* create service instance */
    public static UserService getInstance() {
        return instance;
    }
    /* constructor */
    public UserService(){}
    /* create user instance */
    private User user;
    /* get user info */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        /* saving user info to fire store database collection */
        this.user = user;
        if (user != null){
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            CollectionReference usersCollection = database.collection("users");
            usersCollection.document(user.getUid()).set(user.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i(TAG, "onComplete: success");
                }
                else{
                    task.getException().printStackTrace();
                }
                }
            });
        }
    }

    public void fetchUser(final FirebaseUser user){
        /* getting user info from fire base database */
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = database.collection("users");
        usersCollection.document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String email = documentSnapshot.getString("email");
                String name = documentSnapshot.getString("name");
                String phone = documentSnapshot.getString("phone");
                User userFetch = new User(user.getUid(), email, null, name, phone);
                setUser(userFetch);
                if (onUserListener != null){
                    onUserListener.onSuccess(userFetch);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        if (onUserListener != null){
                            onUserListener.onFail();
                        }
                    }
                });
    }
    private OnUserListener onUserListener;
    public interface OnUserListener{
        void onSuccess(User user);
        void onFail();
    }

    public void setOnUserListener(OnUserListener onUserListener) {
        this.onUserListener = onUserListener;
    }
}

package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

//import com.example.myapplication.FirstFragmentDirections;
import com.example.myapplication.databinding.FragmentFirstBinding;

// ****************** Exercise 11 ********************
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
// ****************** Exercise 11 ********************

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    //**************************************************
    // Define TAG as a class-level constant
    private static final String TAG = "FirstFragment";
    //**************************************************

    // member variable for showCountTextView of type TextView for Task 8 Step 4
    TextView showCountTextView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        binding = FragmentFirstBinding.inflate(inflater, container, false);
        //return binding.getRoot(); // commented out for Task 8 Step 4 to work (original code)

        // For Task 8 Step 4 to show count
        View fragmentFirstLayout = binding.getRoot();

        // commented out for Task 8 Step 4 to work
        // Inflate the layout for this fragment
        // View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);

        // Get the count text view for Task 8 Step 4
        showCountTextView = fragmentFirstLayout.findViewById(R.id.textview_first);

        // For Task 8 Step 4 to show count
        return fragmentFirstLayout;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textviewFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );

        // added random button functionality for Task 9 Step 7
        view.findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // find count text view for Task 9 Step 7
                int currentCount = Integer.parseInt(showCountTextView.getText().toString());
                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount);
                NavHostFragment.findNavController(FirstFragment.this).navigate(action);
            }
        });

        // added this to display message when clicking on toast button for Task 8 Step 2
        view.findViewById(R.id.toast_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast myToast = Toast.makeText(getActivity(), "Hello toast!", Toast.LENGTH_SHORT);
//                myToast.show();

                //************************************* Exercise 11 *******************************************

                // Access a Cloud Firestore instance from your Activity
                // from Initialize Cloud Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();

//                Create a new collection and a document using the following example code.
//                From Add data section

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("first", "Ada");
                user.put("last", "Lovelace");
                user.put("born", 1815);

                // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                // Create a new user with a first, middle, and last name
                Map<String, Object> user1 = new HashMap<>();
                user1.put("first", "Alan");
                user1.put("middle", "Mathison");
                user1.put("last", "Turing");
                user1.put("born", 1912);

                // Add a new document with a generated ID
                db.collection("users")
                        .add(user1)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                // from Read data section
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
                //************************************* Exercise 11 *******************************************
            }
        });

        // added this for interactive count button for Task 8 Step 3
        view.findViewById(R.id.count_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countMe(view);
            }
        });
    }

    // will be invoked when the count button is clicked and click listener called
    // For Task 8 Step 3
    private void countMe(View view) {

        // Get the value of the text view
        String countString = showCountTextView.getText().toString();

        // Convert value to a number and increment it
        Integer count = Integer.parseInt(countString);
        count++;

        // Display the new value in the text view.
        showCountTextView.setText(count.toString());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}


//package com.example.myapplication;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;
//
////import com.example.myapplication.FirstFragmentDirections;
//import com.example.myapplication.databinding.FragmentFirstBinding;
//
//public class FirstFragment extends Fragment {
//
//    private FragmentFirstBinding binding;
//
//    // member variable for showCountTextView of type TextView for Task 8 Step 4
//    TextView showCountTextView;
//
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//
//        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        //return binding.getRoot(); // commented out for Task 8 Step 4 to work (original code)
//
//        // For Task 8 Step 4 to show count
//        View fragmentFirstLayout = binding.getRoot();
//
//        // commented out for Task 8 Step 4 to work
//        // Inflate the layout for this fragment
//        // View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
//
//        // Get the count text view for Task 8 Step 4
//        showCountTextView = fragmentFirstLayout.findViewById(R.id.textview_first);
//
//        // For Task 8 Step 4 to show count
//        return fragmentFirstLayout;
//
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        binding.textviewFirst.setOnClickListener(v ->
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
//        );
//
//        // added random button functionality for Task 9 Step 7
//        view.findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // find count text view for Task 9 Step 7
//                int currentCount = Integer.parseInt(showCountTextView.getText().toString());
//                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount);
//                NavHostFragment.findNavController(FirstFragment.this).navigate(action);
//            }
//        });
//
//        // added this to display message when clicking on toast button for Task 8 Step 2
//        view.findViewById(R.id.toast_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast myToast = Toast.makeText(getActivity(), "Hello toast!", Toast.LENGTH_SHORT);
//                myToast.show();
//            }
//        });
//
//        // added this for interactive count button for Task 8 Step 3
//        view.findViewById(R.id.count_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                countMe(view);
//            }
//        });
//    }
//
//    // will be invoked when the count button is clicked and click listener called
//    // For Task 8 Step 3
//    private void countMe(View view) {
//
//        // Get the value of the text view
//        String countString = showCountTextView.getText().toString();
//
//        // Convert value to a number and increment it
//        Integer count = Integer.parseInt(countString);
//        count++;
//
//        // Display the new value in the text view.
//        showCountTextView.setText(count.toString());
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//}

//package com.example.myapplication;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.example.myapplication.databinding.FragmentFirstBinding;
//
//public class FirstFragment extends Fragment {
//
//    private FragmentFirstBinding binding;
//
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//
//        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        binding.buttonFirst.setOnClickListener(v ->
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
//        );
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//}
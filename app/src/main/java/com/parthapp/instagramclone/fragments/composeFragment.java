package com.parthapp.instagramclone.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parthapp.instagramclone.LoginActivity;
import com.parthapp.instagramclone.MainActivity;
import com.parthapp.instagramclone.Post;
import com.parthapp.instagramclone.R;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class composeFragment extends Fragment {


    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public EditText postDes;
    private Button capImage;
    private ImageView postImage;
    private Button uploadPost;
    //private Button logoutBtn;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private static final String TAG = "ComposeFragement";



    public composeFragment() {
        // Required empty public constructor
    }

    //The onCreateView method is called when Fragment should create its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    //This event is triggered soon after onCreateView()
    //Any view setup should occur here. E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postDes = view.findViewById(R.id.postDes);
        capImage = view.findViewById(R.id.takePhoto);
        postImage = view.findViewById(R.id.takenPhoto);
        uploadPost = view.findViewById(R.id.uploadPost);


        capImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ParseUser.logOut();
//                ParseUser currentUser = ParseUser.getCurrentUser();
//                if (currentUser == null) {
//                    Toast.makeText(getContext(), "Successfully Logged out", Toast.LENGTH_SHORT);
//                    //goLoginActivity();
//                } else {
//                    Toast.makeText(getContext(), "Having problems Logging out!", Toast.LENGTH_SHORT);
//                }
//            }
//        });

        //queryPosts();
        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = postDes.getText().toString();
                if (des.isEmpty()) {
                    Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || postImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(des, currentUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                postImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(String des, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(des);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post was saved");
                Toast.makeText(getContext(), "Post was uploaded!", Toast.LENGTH_SHORT).show();
                postDes.setText("");
                postImage.setImageResource(0);
            }
        });
    }

//    private void goLoginActivity() {
//        Intent i = new Intent(this, LoginActivity.class);
//        startActivity(i);
//        finish();
//    }

}
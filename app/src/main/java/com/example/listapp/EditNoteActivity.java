package com.example.listapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    Intent data;
    EditText titleedit,contentedit;
    FloatingActionButton saveeditnote;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        titleedit=findViewById(R.id.edittitleofnote);
        contentedit=findViewById(R.id.editcontentofnote);
        saveeditnote=findViewById(R.id.saveeditnote);
        Toolbar toolbar=findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        saveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String newtitle=titleedit.getText().toString();
           String newcontent=contentedit.getText().toString();

           if (newtitle.isEmpty() || newcontent.isEmpty())
           {
               Toast.makeText(getApplicationContext(),"You need to fill the title and the note!",Toast.LENGTH_LONG).show();
               return;
           }
           else
           {
               DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
               Map<String,Object> note=new HashMap<>();
               note.put("title",newtitle);
               note.put("content",newcontent);
               documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_LONG).show();
                       startActivity(new Intent(EditNoteActivity.this,NotesActivity.class));
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"Failed to update the note",Toast.LENGTH_LONG).show();
                   }
               });
           }

            }
        });

        String notetitle=data.getStringExtra("title");
        String contentnote=data.getStringExtra("content");
        titleedit.setText(notetitle);
        contentedit.setText(contentnote);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
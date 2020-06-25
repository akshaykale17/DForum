package com.example.dforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ForumPage extends AppCompatActivity {

    String name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText text;
    Button send;
    private RecyclerView re;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_page);


        text = findViewById(R.id.edittext_chatbox);
        send = findViewById(R.id.button_chatbox_send);
        Intent in = getIntent();
        name = in.getStringExtra("Name");
        System.out.println(name);
        re = findViewById(R.id.review);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> mail = new HashMap<>();
                mail.put("body",text.getText().toString());
                mail.put("name",name);
               // mail.put("timestamp", Timestamp.now());
                db.collection("mails")
                        .add(mail)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                System.out.println("Done");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Failed");
                            }
                        });

            }
        });


        Query que  = db.collection("mails");
        FirestoreRecyclerOptions<ModelMsg> options = new FirestoreRecyclerOptions.Builder<ModelMsg>()
                .setQuery(que,ModelMsg.class)
                .build();

       adapter = new FirestoreRecyclerAdapter<ModelMsg, MsgHolder>(options) {
            @NonNull
            @Override
            public MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msgholder,parent,false);
                return new MsgHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MsgHolder holder, int position, @NonNull ModelMsg model) {
                holder.msgname.setText(model.getName() );
                holder.msgbody.setText(model.getBody());
            }
        };

       re.setHasFixedSize(true);
       re.setLayoutManager(new LinearLayoutManager(this));
       re.setAdapter(adapter);

    }

    private class MsgHolder extends RecyclerView.ViewHolder {
        private TextView msgname;
        private TextView msgbody;

        public MsgHolder(@NonNull View itemView) {
            super(itemView);

            msgname = itemView.findViewById(R.id.msgname);
            msgbody = itemView.findViewById(R.id.msgbody);

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
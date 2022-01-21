package ph.edu.uic.mysqlloginregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

public class MainActivity extends Activity {
    public static final String TAG = "TAG";
    TextView fullname, email,veryMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button verifyBtn, addtask;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullname = findViewById(R.id.profileName);

        email = findViewById(R.id.profileEmail);

        fAuth= FirebaseAuth.getInstance();

        fStore= FirebaseFirestore.getInstance();

        verifyBtn = findViewById(R.id.verifybtn);

        veryMsg = findViewById(R.id.textView5);

        addtask = findViewById(R.id.addtask);

        userID = fAuth.getCurrentUser().getUid();

        final FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified())
        {
            verifyBtn.setVisibility(View.VISIBLE);
            veryMsg.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(MainActivity.this,"Verfication email has been sent.",Toast.LENGTH_SHORT);

                        }
                    });

                }
            });

        }

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }
                else
                {
                    fullname.setText(documentSnapshot.getString("Fullname"));

                    email.setText(documentSnapshot.getString("Email"));

                }

            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), addingtask.class));
            }
        });

    }



    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }
}
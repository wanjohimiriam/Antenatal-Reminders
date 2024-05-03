package com.example.AntenatalReminders;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class CaptureFragment extends Fragment {
    View view;
    private EditText nameT, emailT, companyT, pnumberT, designationT;

    private Button buttonSubmit;
    private Button addSubmit;
    private Button confirmSubmit;
    private ImageView search_img;
    ArrayList<String> items = new ArrayList<>();
    //    private SpinnerDialog spinnerDialog;
    //private SpinnerDialog spinnerDialog;
    private JSONArray item_visitors;
    private ArrayList<String> full_names;
    private ArrayList<String> company_name;
    private ArrayList<String> email;
    private ArrayList<String> pnumber;
    private ArrayList<String> designation;
    private Timer timer;

    private ListView listview;

    private ArrayAdapter<String> adapter;

    private List<String> allItems;
    private List<String> displayedItems;
    DatabaseReference db, nameref, Cref, PNref, DesRef, Eref;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        view = inflater.inflate(R.layout.capturefragment, container, false);

        nameT = view.findViewById(R.id.nameT);
        emailT = view.findViewById(R.id.emailT);
        companyT = view.findViewById(R.id.companyT);
        pnumberT = view.findViewById(R.id.pnumberT);
        designationT = view.findViewById(R.id.designationT);
        addSubmit = view.findViewById(R.id.buttonNew);
        confirmSubmit = view.findViewById(R.id.buttonConfirm);

        addSubmit.setOnClickListener(v -> post());
        confirmSubmit.setOnClickListener(v -> post());

        Log.d("CaptureFragment", "View initialized successfully");
        db = FirebaseDatabase.getInstance().getReference();
        nameref = db.child("Name");
        Cref = db.child("Company");
        Eref = db.child("Email");
        PNref = db.child("PhoneNumber");
        DesRef = db.child("Designation");

        return view;
    }

    private void post() {
        if (nameT.getText().toString().isEmpty()) {
            nameT.setError("This field is required");
            nameT.requestFocus();
        } else if (companyT.getText().toString().isEmpty()) {
            companyT.setError("This field is required");
            companyT.requestFocus();
        } else if (emailT.getText().toString().isEmpty()) {
            emailT.setError("This field is required");
            emailT.requestFocus();
        } else if (pnumberT.getText().toString().isEmpty()) {
            pnumberT.setError("This field is required");
            pnumberT.requestFocus();
        } else if (designationT.getText().toString().isEmpty()) {
            designationT.setError("This field is required");
            designationT.requestFocus();
        } else {
            final String name = nameT.getText().toString().trim().toUpperCase();
            final String company = companyT.getText().toString().trim().toUpperCase();
            final String email = emailT.getText().toString().trim();
            final String pnumber = pnumberT.getText().toString().trim();
            final String designation = designationT.getText().toString().trim();

            sendToDB(name, company, email, pnumber, designation);
            sendSMSMessage();
        }
    }

    private void sendToDB(String name, String company, String email, String pnumber, String designation) {
        nameref.setValue(name);
        Cref.setValue(company);
        Eref.setValue(email);
        PNref.setValue(pnumber);
        DesRef.setValue(designation);
    }

    protected void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.SEND_SMS)) {
                Toast.makeText(requireContext(), "SMS permission is required to send SMS.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            // Permission already granted; send SMS
            String message ;
            message= "Hello Mr Karari Welcome to Karatina University. Hoping to see you again next time";
            String phonenumber = "+254797917978";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenumber, null, message, null, null);
            Log.d(TAG, "sendSMSMessage: "+phonenumber);
            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSMessage();
                } else {
                    Toast.makeText(getActivity(), "SMS permission denied.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}

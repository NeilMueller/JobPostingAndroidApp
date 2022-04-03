package ca.dal.csci3130.quickcash.jobmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceDAO;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceDAOAdapter;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceInterface;
import ca.dal.csci3130.quickcash.usermanagement.Preferences;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;

public class AvailableJobsActivity extends FragmentActivity implements OnMapReadyCallback {

    private DAO dao;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private List<Job> jobList;
    private FusedLocationProviderClient fusedLocationClient;
    private static final Integer REQUEST_CODE = 123;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    private LatLng userLocation;
    private final ArrayList<Marker> mJobMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_jobs);

        dao = new JobDAOAdapter(new JobDAO());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.jobMap);

        jobList = new ArrayList<>();
        // grabs the location services api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button returnHome = findViewById(R.id.btn_returnHome_employee);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(AvailableJobsActivity.this, EmployeeHomeActivity.class);
            startActivity(intent);
        });

        //import Preference logic
        Button importButton = (Button) findViewById(R.id.buttonImportPreferences);
        importButton.setOnClickListener(view -> importPref());

        getJobs();

        //filter jobs logic
        Button filterJobButton = (Button) findViewById(R.id.buttonApplyFilter);
        filterJobButton.setOnClickListener(view -> {
            String jobType = grabJobType();
            String payRate = grabPayRate();
            String duration = grabDuration();
            getFilteredJobs(jobType, payRate, duration);
        });
    }

    /**
     * Gets all the jobs from the db
     */
    protected void getJobs() {
        DatabaseReference jobRef = dao.getDatabaseReference();
        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    if(job != null && job.acceptingApplications()){
                        jobList.add(job);
                    }
                }

                // start loading the map
                loadMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - getJobs:", error.getMessage());
            }
        });
    }

    /**
     * Gets all the jobs from the db
     */
    protected void getFilteredJobs(String jobType, String payRateS, String durationS) {
        jobList.clear();
        removeMarkers();
        DatabaseReference jobRef = dao.getDatabaseReference();

        boolean jobTypeSpecified = !jobType.isEmpty();
        double payRate = payRateS.isEmpty() ? 0 : Double.parseDouble(payRateS);
        double duration = durationS.isEmpty() ? 100 : Double.parseDouble(durationS);

        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    if(job != null) {
                        boolean jobInPref = payRate < job.getPayRate() && duration > job.getDuration() && job.acceptingApplications();
                        // if job type was specified, check whether its equal or not. If not, then just check the other prefs.
                        if(jobInPref && (!jobTypeSpecified || jobType.equalsIgnoreCase(job.getJobType()))){
                            jobList.add(job);
                        }
                    }
                }

                if(jobList.isEmpty()){
                    createToast(R.string.no_job_found);
                }
                else {
                    createToast(R.string.job_found);
                }

                // start loading the map
                loadMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - getFilteredJobs:", error.getMessage());
            }
        });
    }

    /**
     * Remove all markers
     */
    private void removeMarkers(){
        for (Marker marker : mJobMarkers){
            marker.remove();
        }
    }
    /**
     * Checks/Asks for location permissions and starts map load
     */
    protected void loadMap() {
        // checks if the permission is already granted
        if (ContextCompat.checkSelfPermission(AvailableJobsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // if it is already granted, then get current location and start the map
            getCurrentLocationAndStartMap();
        } else {
            // if not then ask for it
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE);
        }
    }

    /**
     * Gets current location of the user and starts the map
     */
    protected void getCurrentLocationAndStartMap() {
        if (ContextCompat.checkSelfPermission(AvailableJobsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(AvailableJobsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //This error is a android studio bug, the permissions are added
        fusedLocationClient
                .getCurrentLocation(100, cancellationTokenSource.getToken()) // 100 is PRIORITY_HIGH_ACCURACY
                .addOnSuccessListener(location -> {
                    // if location is not null, set it to user location
                    if (location != null) {
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        // get the map ready
                        mapFragment.getMapAsync(AvailableJobsActivity.this);
                    }
                });
    }

    /**
     * Invoked when map is ready
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // add job pins to map
        addJobPinsToMap();

        // if location was not retrievable for some reason, set to default - Dalhousie University
        LatLng location = userLocation == null ? new LatLng(44.636585, -63.5938442) : userLocation;

        // mark the location and zoom into that
        MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .title("Your Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
        mMap.addMarker(markerOptions).showInfoWindow();

        mMap.setOnInfoWindowClickListener(marker -> {
            Intent intent = new Intent(AvailableJobsActivity.this, JobAdActivity.class);
            intent.putExtra("JobID", marker.getTag().toString());
            startActivity(intent);
            // need to find a way to pass the corresponding job to the jobAdActivity

        });

    }

    /**
     * @deprecated
     * Check out registerForActivityResult()
     * Link: https://developer.android.com/reference/androidx/fragment/app/Fragment
     *
     * This method is called when the user accepts or denies the asked permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    @Deprecated
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { //NOSONAR
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // if request_code has not changed, the process of asking the permission was successful
        if (requestCode == REQUEST_CODE) {
            // if it was granted then get the current location
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndStartMap();
            } else {
                // if not move forward and start the map with a default location
                Toast.makeText(AvailableJobsActivity.this, "Permission denied by user !!!", Toast.LENGTH_SHORT).show();
                mapFragment.getMapAsync(AvailableJobsActivity.this);
            }
        }
    }

    /**
     * add pins to the map and create links from pins to clickable ads
     */
    protected void addJobPinsToMap(){
        for(JobInterface job : jobList){
            // add pin
            LatLng pin = new LatLng(job.getLatitude(), job.getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(pin)
                    .title(job.getJobTitle()));
            marker.setTag(job.getJobID());

            mJobMarkers.add(marker);
            // **can add marker links here**
        }
    }

    /**
     * Imports filter settings into edit text
     */
    private void importPref(){
        EditText jobTypeEdit = (EditText) findViewById(R.id.editTextSearchJobType);
        EditText payRateEdit = (EditText) findViewById(R.id.editTextSearchPayRate);
        EditText durationEdit = (EditText) findViewById(R.id.editTextSearchDuration);

        DAO dao1 = new PreferenceDAOAdapter(new PreferenceDAO());
        DatabaseReference databaseReference = dao1.getDatabaseReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean noPref = true;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PreferenceInterface preferenceItem = dataSnapshot.getValue(Preferences.class);
                    if(preferenceItem != null && checkID(preferenceItem)){
                        jobTypeEdit.setText(preferenceItem.getJobType());
                        payRateEdit.setText(String.valueOf(preferenceItem.getPayRate()));
                        durationEdit.setText(String.valueOf(preferenceItem.getDuration()));
                        noPref = false;
                        break;
                    }
                }
                //if no preferences found
                if(noPref){
                    createToast(R.string.toast_no_preference_found);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - importPref:", error.getMessage());
            }
        });
    }

    private boolean checkID(PreferenceInterface preference){
        String id = grabEmail();
        if(id == null){
            return false;
        }
        return id.equals(preference.getUserID());
    }

    /**
     * Returns the email of the user signed in
     * @return
     */

    private String grabEmail() {

        SessionManagerInterface session = SessionManager.getSessionManager(this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn){
            return  session.getKeyEmail();
        }
        return null;
    }

    /**
     * returns text in jobtype
     */

    private String grabJobType(){
        EditText jobTypeEdit = (EditText) findViewById(R.id.editTextSearchJobType);
        return jobTypeEdit.getText().toString();
    }

    /**
     * returns text in payRate
     */

    private String grabPayRate(){
        EditText payRateEdit = (EditText) findViewById(R.id.editTextSearchPayRate);
        return payRateEdit.getText().toString();
    }

    /**
     * returns text in duration
     */

    private String grabDuration(){
        EditText durationEdit = (EditText) findViewById(R.id.editTextSearchDuration);
        return durationEdit.getText().toString();
    }

    /**
     * method to create Toast message upon error
     *
     * @param messageId
     */
    protected void createToast(int messageId) {
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }
}
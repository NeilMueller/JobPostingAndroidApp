package ca.dal.csci3130.quickcash.jobmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;

public class AvailableJobsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private List<Job> jobList;
    private FusedLocationProviderClient fusedLocationClient;
    private static final Integer REQUEST_CODE = 123;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_jobs);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.jobMap);

        jobList = new ArrayList<>();
        // grabs the location services api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button returnHome = findViewById(R.id.btn_returnHome_employee);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvailableJobsActivity.this, EmployeeHomeActivity.class);
                startActivity(intent);
            }
        });

        getJobs();
    }

    /**
     * Gets all the jobs from the db
     */
    protected void getJobs() {
        JobDAO jobDAO = new JobDAO();
        DatabaseReference jobRef = jobDAO.getDatabaseReference();

        jobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    jobList.add(job);
                }

                // start loading the map
                loadMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }

    /**
     * Checks/Asks for location permissions and starts map load
     */
    protected void loadMap() {
        // checks if the permission is already granted
        if (ContextCompat.checkSelfPermission(AvailableJobsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // if it is already granted, then get current location and start the map
            getCurrentLocationAndStartMap();

            // the code to add clickable ads would be added here or in the dummy method below
            methodToAddClickableAds();
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
        if (ActivityCompat.checkSelfPermission(AvailableJobsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AvailableJobsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //This error is a android studio bug, the permissions are added
        fusedLocationClient
                .getCurrentLocation(100, cancellationTokenSource.getToken()) // 100 is PRIORITY_HIGH_ACCURACY
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // if location is not null, set it to user location
                        if (location != null) {
                            userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            // get the map ready
                            mapFragment.getMapAsync(AvailableJobsActivity.this);
                        }
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

        // **after map is ready continue with the normal flow**
    }

    protected void methodToAddClickableAds(){ }

    /**
     * This method is called when the user accepts or denies the asked permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
            mMap.addMarker(new MarkerOptions().position(pin).title(job.getJobTitle()));

            // **can add marker links here**
        }
    }
}
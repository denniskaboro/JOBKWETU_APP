package com.e.jobkwetu.Home_Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.MarkerAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.e.jobkwetu.App.MyApplication.TAG;

public class Explore_Fragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    protected static final int REQUEST_CHECK_SETTINGS = 0*1;
    private Boolean mLocationPermissionGranted = false;
    private Location mLastKnownLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    private PlacesClient mplacesClient;
    private static final int DEFAULT_ZOOM = 15;
    private static final int TILT_LEVEL = 0;
    private static  final int BEARING_LEVEL = 0;
    private Marker previousMarker = null;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mDefaultLocation = new LatLng( -33.8523341 , 151.2106085);
    private static final String KEY_LOCATION = "location";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private CameraPosition mCameraPosition;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private boolean GpsStatus;
    private LocationManager locationManager;




    public Explore_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        if (savedInstanceState != null){
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Prompt the user for permission.
        getLocationPermission();

        View view = inflater.inflate(R.layout.explore_frag, container, false);

        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.explore_toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("EXPLORE");

        //Build the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();




        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException){
                    //Location settings are not satisfied, but this can be fixed
                    //by showing the user a dialog.
                    try {
                        //show the dialog by calling startResolutionFor Result(),
                        //and check the result in onAvtivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),REQUEST_CHECK_SETTINGS);
                    }catch (IntentSender.SendIntentException sendEx){
                        //Ignore the error
                    }
                }
            }
        });


        // Construct a GeoDataClient.
        Places.initialize(getActivity(),getString(R.string.google_maps_key));
        mplacesClient = Places.createClient(getActivity());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        return view;
    }

    private void CheckGpsStatus() {
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        assert  locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Allow JOBKWETU to access this device's location?")
                    .setCancelable(false)
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CheckGpsStatus();

                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //Toast.makeText(getActivity(), "gps enabled", Toast.LENGTH_SHORT).show();
        }
    }

    //Save the state of the map when the activity is paused

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap !=null) {
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            super.onSaveInstanceState(outState);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onResume() {
        CheckGpsStatus();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }




    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    /*
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    mplacesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        mLikelyPlaceNames = new String[count];
                        mLikelyPlaceAddresses = new String[count];
                        mLikelyPlaceAttributions = new List[count];
                        mLikelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        //Explore_Fragment.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    /*private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }
    */



    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                SaveDeviceLocation(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void SaveDeviceLocation(final double latitude, final double longitude) {
        final String username = MyApplication.getInstance().getPrefManager().getUser().getName();
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                EndPoints.MARKER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if(true == obj.getBoolean("success")) {
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("data");
                       /* Register user = new Register(userObj.getString("user_id"),
                                userObj.getString("phone"),
                                userObj.getString("token"),
                                userObj.getString("email"),
                                userObj.getString("name"));

                        */


                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse !=null && networkResponse.data !=null){
                    switch (networkResponse.statusCode){
                        case 400:
                            json = new String(networkResponse.data);
                            json = trimMessage(json,"message");
                            if (json !=null) DisplayMessage(json);
                            //Log.e(TAG, "onErrorResponse: "+json );
                            break;


                        //Add cases here
                    }

                }
                //Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                //Toast.makeText(getApplicationContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            //Somewhere that hass access to a context

            private void DisplayMessage(String json) {
                Toast.makeText(getContext(), "Error"  + json, Toast.LENGTH_SHORT).show();
            }

            private String trimMessage(String json, String key) {
                String trimmedString = null;
                try {
                    JSONObject obj = new JSONObject(json);
                    trimmedString = obj.getString(key);
                    //Log.e(TAG, "trimMessage: "+trimmedString );
                } catch (JSONException e){
                    e.printStackTrace();
                    return null;
                }
                return trimmedString;
            }
        }) {

            @Override
            protected Map<String, String > getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", userid);
                params.put("name", username);
                params.put("address", "Emali");
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));
                params.put("type", "Customer");
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer "+Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        mMap = map;

        getMarkerLocations(); //Get all Markers from the database and position them on the map
    }

    private void getMarkerLocations() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.MARKER2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if(true == obj.getBoolean("success")) {

                        Gson gson = new Gson();

                        JSONArray jsonArray = obj.getJSONArray("data");
                        Type type = new TypeToken<List<com.e.jobkwetu.Model.Marker>>(){}.getType();
                        String jsonstring = String.valueOf(jsonArray);
                        Toast.makeText(getActivity(), "json"+jsonstring, Toast.LENGTH_SHORT).show();
                        List<com.e.jobkwetu.Model.Marker> listdata = gson.fromJson(jsonstring, (java.lang.reflect.Type) type);
                        for (com.e.jobkwetu.Model.Marker marker : listdata){
                            fillMapViews(listdata);
                        }


                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse !=null && networkResponse.data !=null){
                    switch (networkResponse.statusCode){
                        case 400:
                            json = new String(networkResponse.data);
                            json = trimMessage(json,"message");
                            if (json !=null) DisplayMessage(json);
                            //Log.e(TAG, "onErrorResponse: "+json );
                            break;
                        //Add cases here
                    }

                }
                //Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                //Toast.makeText(getApplicationContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            //Somewhere that hass access to a context

            private void DisplayMessage(String json) {
                Toast.makeText(getContext(), "Error"  + json, Toast.LENGTH_SHORT).show();
            }

            private String trimMessage(String json, String key) {
                String trimmedString = null;
                try {
                    JSONObject obj = new JSONObject(json);
                    trimmedString = obj.getString(key);
                    //Log.e(TAG, "trimMessage: "+trimmedString );
                } catch (JSONException e){
                    e.printStackTrace();
                    return null;
                }
                return trimmedString;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer "+Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);


    }


    private void fillMapViews(final List<com.e.jobkwetu.Model.Marker> locations) {
        String strTolat = "0.470050";
        String strToLng = "37.947061";

        if (mMap == null) {
            return;
        }

        if (locations == null || locations.isEmpty()) {
            //TextView textViewAddress = (TextView) getView().findViewById(R.id.txtAddress);
            //textViewAddress.setText(getString(R.string.placeholder_location));
        }
        for (int i2 = 0; i2 < locations.size(); i2++)
        {
            if(i2==0) ////FOR ANIMATING THE CAMERA FOCUS FIRST TIME ON THE GOOGLE MAP
            {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(strTolat), Double.parseDouble(strToLng))).zoom(6).bearing(0).tilt(45).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),1000,null);
            }
            Marker locationMarker;
            locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locations.get(i2).getLat()), Double.parseDouble(locations.get(i2).getLng()))));

            locationMarker.setTag(locations);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            final String title = locations.get(i2).getName();
            String address =locations.get(i2).getAddress();
            //Marker
            final MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(Double.valueOf(strTolat),Double.valueOf(strToLng)))
                    .title(title)
                    .snippet(address);
            //Set custom info Adapter
            MarkerAdapter adapter =new MarkerAdapter(getActivity());
            mMap.setInfoWindowAdapter(adapter);
            mMap.addMarker(markerOptions).hideInfoWindow();

            final int finalI = i2;
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(getContext(), "T"+title, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

        }



    }

/*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.explore_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_get_place:
                showCurrentPlace();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

 */

}

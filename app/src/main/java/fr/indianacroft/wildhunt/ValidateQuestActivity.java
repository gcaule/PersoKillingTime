package fr.indianacroft.wildhunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ValidateQuestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button butAddNewChallenge;
    Button button_create_quest;
    EditText name_quest;
    EditText description_quest;
    Spinner spinner_quest;
    FirebaseDatabase ref;
    DatabaseReference childRef;
    ImageView imageViewAvatar;
    private String mUserId;
    private String mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_quest);

        // Pour recuperer la key d'un user (pour le lier a une quête)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mUserId = sharedPreferences.getString("mUserId", mUserId);
        Log.d("key", mUserId);
        /////////////////////////////////////////////////////////////////

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Drawer Menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Avatar
        // POUR CHANGER L'AVATAR SUR LA PAGE AVEC CELUI CHOISI
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Avatar").child(mUserId);
//        final ImageView imageViewAvatar = (ImageView) findViewById(R.id.imageViewAvatar);
//        // Load the image using Glide
//        if (storageReference.getDownloadUrl().isSuccessful()){
//            Glide.with(getApplicationContext())
//                    .using(new FirebaseImageLoader())
//                    .load(storageReference)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(imageViewAvatar);
//        }

        imageViewAvatar = (ImageView) findViewById(R.id.imageViewAvatar);
        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        // ENTER CODE HERE
        // METHODE POUR TROUVER CHALLENGE

        DatabaseReference refUser =
                FirebaseDatabase.getInstance().getReference().child("User").child(mUserId);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // on recupere la qûete créee par un user
                User user = dataSnapshot.getValue(User.class);
                String questId = user.getUser_createdquestID();

                // On parcourt les challenges à valider dans le dossier de la qûete créee (id creator) par un user
                // Sur firebase ca correspond a user_createdquestid
                DatabaseReference refAvalider = FirebaseDatabase.getInstance().
                        getReference("User").child(mUserId).child("aValider").child(questId);
                refAvalider.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            // On recupere l'Id du challenge qu'on analyse
                            String challengeIdToValidate = dsp.getKey();
                            ArrayList<Pair> mapChallengeToValidate = new ArrayList<Pair>((int) dsp.getChildrenCount());
                            // On créer un tableau de la taille de tout les challenges présent
//                            String[] userIdTableau = new String[(int) dsp.getChildrenCount()];
                            int i = 0;
                            for (DataSnapshot dsp2 : dsp.getChildren()) {
                                if ((boolean)dsp2.getValue() == false) {
                                    String userIdToValidate = dsp2.getKey();
                                    Pair<String, String> pair = new Pair<String, String>(challengeIdToValidate, userIdToValidate);
                                    mapChallengeToValidate.add(pair);
//                                    userIdTableau[i] = dsp2.getKey();



                                }

                                /* //Store data as pait in arraylist
                                ArrayList<Pair> mapChalengUser = new ArrayList<Pair>();
                                Pair<String, String> pair = new Pair<String, String>(calengeId, userId);
                                mapChalengUser.add(pair);
                                // Get Data from Pairs in ArrayList
                                chalengeId = mapChalengUser.get(index).first;
                                userId = mapChalengUser.get(index).second;*/

                                i++;
                            }
                            String test = mapChallengeToValidate.get(0).first.toString();
                            String test2 = mapChallengeToValidate.get(0).second.toString();
                            String test3 = mapChallengeToValidate.get(1).first.toString();
                            String test4 = mapChallengeToValidate.get(1).second.toString();

                            TextView challName = (TextView) findViewById(R.id.challengeNameToValidate);
                            challName.setText(test);
                            TextView idName = (TextView) findViewById(R.id.idNameToValidate);
                            idName.setText(test2);
                            TextView teseeeet = (TextView) findViewById(R.id.challengename2);
                            teseeeet.setText(test3);
                            TextView tezqtezt = (TextView) findViewById(R.id.idname2);
                            tezqtezt.setText(test4);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // Drawer Menu
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // TODO : remplacer les toasts par des liens ET faire en sorte qu'on arrive sur les pages de fragments
        if (id == R.id.nav_rules) {
            Intent intent = new Intent(getApplicationContext(), RulesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_play) {
            Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lobby) {
            Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_create) {
            startActivity(new Intent(getApplicationContext(), CreateQuestActivity.class));
        } else if (id == R.id.nav_create) {
            startActivity(new Intent(getApplicationContext(), CreateQuestActivity.class));
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), ValidateQuestActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_delete) {
            startActivity(new Intent(getApplicationContext(), ConnexionActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.example.myapplication;

import android.os.Bundle; // used for passing data between activities
import android.util.Log; // output messages to the system log to help with debugging
import android.view.LayoutInflater; // used to instantiate layout XML file into its corresponding View objects
import android.view.View; // represents a basic building block for user interface components
import android.view.ViewGroup; // ViewGroup is a subclass of View and serves as the base class for layouts
import android.widget.ArrayAdapter; // adapter that works with a data source such as an array or a list and is used to populate views like ListView
import android.widget.ListView; // view group that displays a list of scrollable items
import android.widget.TextView; // a widget used to display text to the user

import androidx.annotation.NonNull; // indicates that a parameter, field, or method return value cannot be null
import androidx.annotation.Nullable; // used to indicate that a parameter, field, or method return value can be null
import androidx.appcompat.app.AppCompatActivity; // base class for activities that use the support library action bar features

import com.google.firebase.firestore.DocumentSnapshot; // represents a snapshot of a document in a Firestore database

import java.util.ArrayList; // provides methods to add, remove, access, and manipulate elements in the list
import java.util.Collections; // provides static methods for working with collections
import java.util.Comparator; // used to define a custom ordering for objects
import java.util.List; //  ordered collection of elements

/**
 * @author Vanessa
 *
 * LeaderboardActivity is an interface class that retrieves scores
 * from Firestore and sorts the scores (from highest to lowest).
 * LeaderboardActivity displays the leaderboard with rank, email, and score
 *
 * LeaderboardActivity is a subclass of AppCompatActivity
 */

public class LeaderboardActivity extends AppCompatActivity {

    /**
     * ListView to display the leaderboard entries.
     */
    ListView listView;
    /**
     * FirestoreAccess instance for accessing Firestore database.
     */
    FirestoreAccess firestoreAccess;
    /**
     * List to store the leaderboard entries.
     */
    List<String> leaderboardEntries;

    /**
     * Called when the activity is being created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Ensure that the superclass's initialization code is executed
         * before custom initialization code.
         */
        super.onCreate(savedInstanceState);

        /**
         * Initializing variables and setting up any necessary event listeners
         * In this case leaderboard_screen.xml
         */
        setContentView(R.layout.leaderboard_screen);

        /**
         * Find views by their IDs
         */
        listView = findViewById(R.id.listView);
        /**
         * Initialize FirestoreAccess
         */
        firestoreAccess = new FirestoreAccess();
        /**
         * Initialize Array:ist
         * used to store the leaderboard entries retrieved from Firestore,
         * which consist of player email addresses and scores
         */
        leaderboardEntries = new ArrayList<>();

        /**
         * Retrieve player scores from Firestore
         */
        firestoreAccess.retrievePlayerScores("leaderboard", new FirestoreAccess.OnRetrieveScoresListener() {

            /**
             * Callback method invoked when player scores are successfully retrieved from Firestore.
             *
             * @param documentSnapshots is a list of DocumentSnapshot objects containing player score data.
             */
            @Override
            public void onRetrieveSuccess(List<DocumentSnapshot> documentSnapshots) {
                /**
                 * Process retrieved scores and populate leaderboardEntries list
                 */
                for (DocumentSnapshot document : documentSnapshots) {
                    String email = document.getString("email");
                    long score = document.getLong("score");
                    String entry = email + ": " + score;
                    leaderboardEntries.add(entry);
                }

                /**
                 * Sort the leaderboardEntries list
                 * in descending order based on scores
                 */
                Collections.sort(leaderboardEntries, new Comparator<String>() {
                    /**
                     * Compares two leaderboard entries based on their scores.
                     *
                     * @param entry1 the first object to be compared.
                     * @param entry2 the second object to be compared.
                     * @return score2 is less than, equal to, or greater than scpre1
                     */
                    @Override
                    public int compare(String entry1, String entry2) {
                        /**
                         * Split the entries to extract the scores
                         */
                        long score1 = Long.parseLong(entry1.split(": ")[1]);
                        long score2 = Long.parseLong(entry2.split(": ")[1]);
                        /**
                         * Compare the scores in reverse order for descending sort
                         */
                        return Long.compare(score2, score1);
                    }
                });

                /**
                 * Use ArrayAdapter to populate the ListView
                 */
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(LeaderboardActivity.this, R.layout.leaderboard_contents, R.id.rank, leaderboardEntries) {
                    /**
                     * Returns a view for each item in the adapter's data set.
                     *
                     * @param position The position of the item within the adapter's data set of the item whose view
                     *        we want.
                     * @param convertView The old view to reuse, if possible. Note: You should check that this view
                     *        is non-null and of an appropriate type before using. If it is not possible to convert
                     *        this view to display the correct data, this method can create a new view.
                     *        Heterogeneous lists can specify their number of view types, so that this View is
                     *        always of the right type (see {@link #getViewTypeCount()} and
                     *        {@link #getItemViewType(int)}).
                     * @param parent The parent that this view will eventually be attached to
                     * @return a View corresponding to the data at the specified position.
                     */
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        /**
                         * Inflate leaderboard_contents layout if convertView is null
                         */
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.leaderboard_contents, parent, false);
                        }

                        /**
                         * Find TextViews for rank, player, and score
                         * from fragment_contents.xml
                         */
                        TextView rankTextView = convertView.findViewById(R.id.rank);
                        TextView playerTextView = convertView.findViewById(R.id.player);
                        TextView scoreTextView = convertView.findViewById(R.id.score);

                        /**
                         * Set the rank (position + 1 because positions are zero-based)
                         */
                        rankTextView.setText(String.valueOf(position + 1));

                        /**
                         * Get the email and score from the leaderboardEntries list
                         */
                        String entry = leaderboardEntries.get(position);
                        String[] parts = entry.split(": ");
                        if (parts.length == 2) {
                            playerTextView.setText(parts[0]); // Email
                            scoreTextView.setText(parts[1]);  // Score
                        }

                        /**
                         * Return the updated view
                         */
                        return convertView;
                    }
                };

                /**
                 * Set the adapter for the ListView
                 */
                listView.setAdapter(adapter);
            }

            /**
             * Log error if retrieving leaderboard fails
             *
             * @param e is the exception that occurred during the retrieval process.
             */
            @Override
            public void onRetrieveFailure(Exception e) {
                Log.e("Firestore", "Error retrieving leaderboard", e);
            }
        });
    }
}

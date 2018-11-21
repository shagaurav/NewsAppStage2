package com.example.android.newsappstage2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String LOG_TAG = HomeActivity.class.getName();
    /**
     * URL for news data from the Gaurdians dataset
     */
    private static String REQUEST_URL =
            "http://content.guardianapis.com/search?section=games&show-tags=contributor&format=json&lang=en&order-by=newest&show-fields=thumbnail&page-size=50&api-key=4b8ea716-b82a-40db-9665-d17c535526e8";
    private NewsAdapter mAdapter;
    private TextView mNoContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById( R.id.list_item );
        mNoContentTextView = findViewById( R.id.nocontent );
        newsListView.setEmptyView( mNoContentTextView );

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter( this, new ArrayList<News>() );

        // Set the adapter on the {@link ListView}
        newsListView.setAdapter( mAdapter );

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem( position );

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse( currentNews.getArticleUrl() );
                Intent websiteIntent = new Intent( Intent.ACTION_VIEW, newsUri );

                // Send the intent to launch a new activity
                startActivity( websiteIntent );
            }
        } );
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for the bundle.
            loaderManager.initLoader( 0, null, this );
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById( R.id.loading_indicator );
            loadingIndicator.setVisibility( View.GONE );

            // Update empty state with no connection error message
            mNoContentTextView.setText( R.string.internet_error_message );
        }
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById( R.id.loading_indicator );
        loadingIndicator.setVisibility( View.GONE );

        // Set empty state text to display "No news found."
        mNoContentTextView.setText( R.string.news_error_message );
        mAdapter.clear();

        // If there is a valid list of {@link news}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll( news );
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader( this, REQUEST_URL );

    }
}

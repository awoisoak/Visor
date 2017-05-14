package com.awoisoak.visor.presentation.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.awoisoak.visor.R;
import com.awoisoak.visor.presentation.postgallery.PostGalleryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to display a Blog entry into a WebView.
 * All links will be managed externally.
 * (Pretty simple logic, not worth to apply MVP)
 *
 */
public class WebViewActivity extends AppCompatActivity {
    private static final String MARKER = PostGalleryActivity.class.getSimpleName();
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_POST_TITLE = "post_title";
    private String mContent;


    @BindView(R.id.web_view_activity_web_view) WebView mWebView;
    @BindView(R.id.web_view_toolbar) Toolbar mToolbar;
    @BindView(R.id.web_view_title) TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_36dp);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        String title = new String();
        if (bundle != null) {
            mContent = bundle.getString(EXTRA_CONTENT);
            title = bundle.getString(EXTRA_POST_TITLE);

        } else {
            Log.e(MARKER, "Bundle passed to PostGalleryActivity is null");
        }

        mTitle.setText(title);
        mWebView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        /**
         * Workaround for the non UTF characters bug in WP (like 'Â') avoiding modifying the WP DB
         * http://www.cwgtech.com/strange-character-a-in-wordpress-posts/
         * http://stackoverflow.com/questions/31870862/how-to-remove-the-%C3%82-character-from-a-string-in-java
         */
        mContent = mContent.replaceAll("[^\\x00-\\x7F]", " ");
        String summary = "<html><body><center>" + mContent + "</center></body></html>";
        mWebView.loadData(summary, "text/html", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


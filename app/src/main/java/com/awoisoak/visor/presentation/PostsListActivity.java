package com.awoisoak.visor.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsListActivity extends AppCompatActivity implements PostsListAdapter.PostItemClickListener{
    @BindView(R.id.posts_list_toolbar) Toolbar mToolbar;
    @BindView(R.id.posts_list_logo) ImageView mLogo;
    @BindView(R.id.posts_list_recycler) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ////////////////////
        //TODO Only for testing
        List<Post> postList= new ArrayList<Post>();
        Post post1 = new Post("1","01/01/01","01/01/02","titleee","http://awoisoak.com/wp-content/uploads/2017/02/dsc02970_lzn_resized-300x204.jpg","url_attachment");
        Post post2 = new Post("2","01/01/01","01/01/02","titleee2","http://awoisoak.com/wp-content/uploads/2016/07/dsc00106.resized.jpg","url_attachment");
        postList.add(post1);
        postList.add(post2);

        PostsListAdapter adapter = new PostsListAdapter(postList,this,this);
        mRecyclerView.setAdapter(adapter);

        /////////////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostItemClick(int item) {
        Toast.makeText(this, "item "+item+ "was clicked!", Toast.LENGTH_SHORT).show();
    }
}

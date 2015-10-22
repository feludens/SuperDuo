package barqsoft.footballscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity
{
    public static int selected_match_id;
    public static int current_fragment = 2;
    public final String LOG_TAG = this.getClass().getSimpleName();
    private final String save_tag = "Save Test";
    private PagerFragment my_main;
    private String myMain;
    private String selectedId;
    private String fragment;
    private String pagerCurrent;
    private String selectedMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMain = this.getString(R.string.my_main);
        selectedId = this.getString(R.string.selected_id);
        fragment = this.getString(R.string.fragment);
        pagerCurrent = this.getString(R.string.pager_current);
        selectedMatch = this.getString(R.string.selected_match);
        Log.d(LOG_TAG, this.getString(R.string.main_activity_created));
        if (savedInstanceState == null) {
            my_main = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, my_main)
                    .commit();
        }
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
        if (id == R.id.action_about)
        {
            Intent start_about = new Intent(this,AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Log.v(save_tag, this.getString(R.string.will_save));
        Log.v(save_tag, fragment + String.valueOf(my_main.mPagerHandler.getCurrentItem()));
        Log.v(save_tag, selectedId + selected_match_id);
        outState.putInt(this.getString(R.string.pager_current),my_main.mPagerHandler.getCurrentItem());
        outState.putInt(selectedMatch ,selected_match_id);
        getSupportFragmentManager().putFragment(outState,myMain,my_main);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        Log.v(save_tag, this.getString(R.string.will_retrive));
        Log.v(save_tag, fragment + String.valueOf(savedInstanceState.getInt(pagerCurrent)));
        Log.v(save_tag, selectedId + savedInstanceState.getInt(selectedMatch));
        current_fragment = savedInstanceState.getInt(pagerCurrent);
        selected_match_id = savedInstanceState.getInt(selectedMatch);
        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, myMain);
        super.onRestoreInstanceState(savedInstanceState);
    }
}

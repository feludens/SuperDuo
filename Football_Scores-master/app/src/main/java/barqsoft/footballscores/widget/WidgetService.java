package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Felipe on 10/17/2015.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetViewsFactory());
    }

    class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private Cursor mCursor = null;

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }

            final long identityToken = Binder.clearCallingIdentity();
            Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
            String formatString = getResources().getString(R.string.date_format);
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            String todayDate = format.format(new Date());

            mCursor = getContentResolver().query(uri,
                    ScoresQuery.PROJECTION,
                    null,
                    new String[]{todayDate},
                    null);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }
        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (i == AdapterView.INVALID_POSITION ||
                    mCursor == null || !mCursor.moveToPosition(i)) {
                return null;
            }
            final RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);

            String homeTeamName = mCursor.getString(ScoresQuery.HOME);
            String awayTeamName = mCursor.getString(ScoresQuery.AWAY);
            String homeScore = mCursor.getString(ScoresQuery.HOME_GOALS);
            String awayScore = mCursor.getString(ScoresQuery.AWAY_GOALS);
            String xText = getResources().getString(R.string.symbol_x);

            String dash = getResources().getString(R.string.symbol_dash);
            String negativeOne = getResources().getString(R.string.number_1);

            if(homeScore.equals(negativeOne)) homeScore = dash;
            if(awayScore.equals(negativeOne)) awayScore = dash;

            views.setTextViewText(R.id.home_name, homeTeamName);
            views.setTextViewText(R.id.away_name, awayTeamName);
            views.setTextViewText(R.id.home_score_textview, homeScore);
            views.setTextViewText(R.id.away_score_textview, awayScore);
            views.setTextViewText(R.id.x_textview, xText);

            views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(homeTeamName));
            views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(awayTeamName));

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                views.setContentDescription(R.id.home_crest, homeTeamName);
                views.setContentDescription(R.id.away_crest, awayTeamName);
                views.setContentDescription(R.id.home_name, homeTeamName);
                views.setContentDescription(R.id.away_name, awayTeamName);
            }

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.widget_list_item);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            if (mCursor.moveToPosition(i))
                return mCursor.getLong(ScoresQuery.MATCH_ID);
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private interface ScoresQuery {
        String[] PROJECTION = {
                DatabaseContract.scores_table.HOME_COL,
                DatabaseContract.scores_table.AWAY_COL,
                DatabaseContract.scores_table.HOME_GOALS_COL,
                DatabaseContract.scores_table.AWAY_GOALS_COL,
                DatabaseContract.scores_table.MATCH_ID
        };

        int HOME = 0;
        int AWAY = 1;
        int HOME_GOALS = 2;
        int AWAY_GOALS = 3;
        int MATCH_ID = 4;
    }
}

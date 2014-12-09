package uk.co.smcnamee.elreg.app.utils;

import android.content.Context;
import android.media.MediaActionSound;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import uk.co.smcnamee.elreg.app.R;
import uk.co.smcnamee.elreg.app.activities.MainActivity;
import uk.co.smcnamee.elreg.app.layouts.results.Result;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Simon on 08/12/2014.
 */
public class FeedGrabber {

    private static FeedGrabber grabber;
    private static Context context;
    public static RetrieveFeedData asyncTask;
    InputStream is;

    public static synchronized FeedGrabber getInstance(Context c){
        FeedGrabber feedGrabber = grabber;
        context = c;

        if(feedGrabber == null){
            feedGrabber = new FeedGrabber();
        }

        return feedGrabber;
    }

    public void grabResults(String feed){
        if (asyncTask == null){
            asyncTask = new RetrieveFeedData();
            asyncTask.execute(feed);
        }else if (!(asyncTask.getStatus() == AsyncTask.Status.RUNNING)){
            asyncTask = new RetrieveFeedData();
            asyncTask.execute(feed);
        }else if(asyncTask.getStatus() == AsyncTask.Status.RUNNING){
            asyncTask.cancel(true);
            asyncTask = new RetrieveFeedData();
            asyncTask.execute(feed);
        }

    }


    private class RetrieveFeedData extends AsyncTask<String,Void,List<Result>>{

        @Override
        protected List<Result> doInBackground(String... params) {
            List<Result> results = new ArrayList<Result>();
            try {
                HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
                HttpGet httpget = new HttpGet(params[0]); // Set the action you want to do
                HttpResponse response = httpclient.execute(httpget); // Executeit
                HttpEntity entity = response.getEntity();
                is = entity.getContent(); // Create an InputStream with the response

                results = new RssParser(context).read(is);

                is.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            return results;
        }

        protected void onPostExecute(List<Result> result) {
            MainActivity ma = (MainActivity)context;
            LinearLayout container = (LinearLayout)ma.findViewById(R.id.containerll);
            container.removeAllViews();

            for(Result r : result){
               container.addView(r);
            }

            ma.toggleProgressBar();
        }
    }
}

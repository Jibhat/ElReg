package uk.co.smcnamee.elreg.app.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import uk.co.smcnamee.elreg.app.R;
import uk.co.smcnamee.elreg.app.activities.MainActivity;
import uk.co.smcnamee.elreg.app.fragments.articles.Article;
import uk.co.smcnamee.elreg.app.layouts.results.Result;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a543097 on 09/12/2014.
 */
public class HtmlArticleGrabber {

    private static HtmlArticleGrabber grabber;
    private static Context context;
    public static AsyncReportGrabber asyncTask;

    private static String title;
    private static String heading;

    InputStream is;

    public static synchronized HtmlArticleGrabber getInstance(Context c, String titl, String headin){
        HtmlArticleGrabber htmlGrabber = grabber;
        context = c;
        title = titl;
        heading = headin;


        if(htmlGrabber == null){
            htmlGrabber = new HtmlArticleGrabber();
        }

        return htmlGrabber;
    }

    public void grabArticle(String feed){
        if (asyncTask == null){
            asyncTask = new AsyncReportGrabber();
            asyncTask.execute(feed);
        }else if (!(asyncTask.getStatus() == AsyncTask.Status.RUNNING)){
            asyncTask = new AsyncReportGrabber();
            asyncTask.execute(feed);
        }else if(asyncTask.getStatus() == AsyncTask.Status.RUNNING){
            asyncTask.cancel(true);
            asyncTask = new AsyncReportGrabber();
            asyncTask.execute(feed);
        }
    }




    private class AsyncReportGrabber extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String results = "";
            try {
                HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
                HttpGet httpget = new HttpGet(strings[0]); // Set the action you want to do
                HttpResponse response = httpclient.execute(httpget); // Executeit
                HttpEntity entity = response.getEntity();
                is = entity.getContent(); // Create an InputStream with the response

                results = new HtmlArticleParser(context).read(is);

                is.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            ((MainActivity)context).getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, Article.newInstance(title, heading.split("\n")[0], string))
                                .addToBackStack(null)
                                .commit();
        }
    }
}

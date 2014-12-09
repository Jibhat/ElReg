package uk.co.smcnamee.elreg.app.layouts.results;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import uk.co.smcnamee.elreg.app.R;
import uk.co.smcnamee.elreg.app.activities.MainActivity;
import uk.co.smcnamee.elreg.app.utils.HtmlArticleGrabber;

import java.net.URI;

/**
 * Created by Simon on 08/12/2014.
 */
public class Result extends FrameLayout {

    private String title;
    private String summary;
    private String updated;
    private String link;

    public Result(final Context context) {
        super(context);
        setPadding(5, 5, 5, 5);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(LayoutInflater.from(context).inflate(R.layout.layout_result, null));

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HtmlArticleGrabber.getInstance(context, title, summary).grabArticle(link);
            }
        });
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(title);

    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
        TextView tv = (TextView) findViewById(R.id.summary);
        tv.setText(summary);
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
        TextView tv = (TextView) findViewById(R.id.updated);
        tv.setText(updated);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

package uk.co.smcnamee.elreg.app.utils;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import uk.co.smcnamee.elreg.app.layouts.results.Result;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Simon on 08/12/2014.
 */
public class RssParser {

    private XMLReader reader;
    private RssHandler handler;
    private static Context context;

    public RssParser(Context context) throws Exception{

        this.context = context;

        SAXParserFactory _f = SAXParserFactory.newInstance();
        SAXParser _p = _f.newSAXParser();
        reader = _p.getXMLReader();
        handler = new RssHandler();
        reader.setContentHandler(handler);
    }

    public List<Result> read(InputStream anInputStream)
            throws Exception {
        reader.parse(new InputSource(anInputStream));
        return handler.getResult();
    }

    private static class RssHandler extends DefaultHandler {

        private List<Result> results;
        private Result result;
        private String currentElement;
        private StringBuilder summary = new StringBuilder();

        public List<Result> getResult() {
            return results;
        }

        @Override
        public void startDocument() throws SAXException {
            results = new ArrayList<Result>();
        }

        @Override
        public void startElement(String aUri, String aLocalName, String aQName, Attributes aAttributes
        ) throws SAXException {

            currentElement = aQName;

            if ("entry".equals(aQName)) {
                results.add(result = new Result(context));
            }else if ("link".equals(aQName)&& (result != null)){
                result.setLink(aAttributes.getValue("href"));
            }
        }

        @Override
        public void endElement(
                String aUri, String aLocalName, String aQName
        ) throws SAXException {
            if("summary".equals(aQName)){
                result.setSummary(Html.fromHtml(summary.toString()).toString());
                summary = new StringBuilder();
            }

            currentElement = null;
        }

        @Override
        public void characters(char[] aCh, int aStart, int aLength)
                throws SAXException {
            if (("title".equals(currentElement)) && (result != null)){
                result.setTitle(new String(aCh, aStart, aLength));
            } else if ("summary".equals(currentElement) && (result != null)) {
                summary.append(new String(aCh, aStart, aLength));
            } else if ("updated".equals(currentElement) && (result != null)) {
                String date = new String(aCh, aStart, aLength);
                String updated = "";
                Calendar c = getCalendarFromISO(date);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE hh:mm");
                updated = sdf.format(c.getTime());
                result.setUpdated(updated);
            }
        }
    }

    public static Calendar getCalendarFromISO(String datestring) {


        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()) ;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        try {
            Date date = dateformat.parse(datestring);
            date.setHours(date.getHours()-1);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return calendar;
    }
}

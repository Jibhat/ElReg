package uk.co.smcnamee.elreg.app.utils;

import android.content.Context;
import android.util.Log;
import org.ccil.cowan.tagsoup.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringReader;


/**
 * Created by a543097 on 09/12/2014.
 */
public class HtmlArticleParser {

    private Parser reader;
    private HtmlHandler handler;
    private static Context context;

    private static final ThreadLocal<Parser> parser = new ThreadLocal<Parser>() {
        protected Parser initialValue() {
            return new Parser();
        }
    };

    public HtmlArticleParser(Context context){
        this.context = context;
        reader = new Parser();
        handler = new HtmlHandler();
        reader.setContentHandler(handler);
    }

    public String read(InputStream is) throws Exception{
        String article = "";

        reader.parse(new InputSource(is));

        article = handler.getText();
        Log.e("article", article);

        return article;

    }

    private static class HtmlHandler extends DefaultHandler{
        private StringBuilder sb = new StringBuilder();
        private boolean foundBody = false;
        private boolean keep = true;

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (keep && foundBody) {
                sb.append(ch, start, length);
            }
        }

        public String getText() {
            return sb.toString();
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if (localName.equalsIgnoreCase("script")) {
                keep = false;
            }else if (localName.equalsIgnoreCase("div")){
                if(atts.getIndex("id") != -1){
                    if(atts.getValue("id").equalsIgnoreCase("body")){
                        keep = true;
                        foundBody = true;
                    } else if(atts.getValue("id").equalsIgnoreCase("article_body_btm")){
                        keep = false;
                        foundBody = false;
                    }
                }
            } else if(localName.equalsIgnoreCase("p") && atts.getLength() > 0){
                keep = false;
            }
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if(localName.equalsIgnoreCase("div") && foundBody){
                //foundBody = false;
            }
            keep = true;
        }
    }


}

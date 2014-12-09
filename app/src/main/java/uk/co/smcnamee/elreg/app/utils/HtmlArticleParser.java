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

        return article;

    }

    private static class HtmlHandler extends DefaultHandler{
        private StringBuilder sb = new StringBuilder();
        private boolean foundBody = false;
        private boolean keep = true;
        private String currentElement = "";
        private String lastElement = "";

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (keep && foundBody && (currentElement.equalsIgnoreCase("p")
                    || (currentElement.equalsIgnoreCase("a") && lastElement.equalsIgnoreCase("p"))
                    || (currentElement.equalsIgnoreCase("i") && lastElement.equalsIgnoreCase("p"))
                    || (currentElement.equalsIgnoreCase("blockquote")))) {
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
            }else if(localName.equalsIgnoreCase("p") && atts.getLength() == 0){
                keep = true;
                currentElement = qName;
            }else if(localName.equalsIgnoreCase("a") && foundBody && lastElement.equalsIgnoreCase("p")){
                keep = true;
                currentElement = qName;
            }else if(localName.equalsIgnoreCase("i") && foundBody && lastElement.equalsIgnoreCase("p")){
                keep = true;
                currentElement = qName;
            }else if(localName.equalsIgnoreCase("blockquote") && foundBody){
                keep = true;
                currentElement = qName;
            }else{
                keep = false;
            }
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if(keep && foundBody && (localName.equalsIgnoreCase("p") || localName.equalsIgnoreCase("blockquote"))){
                sb.append("\n\n");
                lastElement = currentElement;
            }else if((localName.equalsIgnoreCase("a") || localName.equalsIgnoreCase("i") || localName.equalsIgnoreCase("blockquote"))
                    && foundBody && keep){

            }else{
                keep = true;
                lastElement = currentElement;
                currentElement = "";
            }

        }
    }


}

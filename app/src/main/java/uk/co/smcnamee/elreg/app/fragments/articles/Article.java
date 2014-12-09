package uk.co.smcnamee.elreg.app.fragments.articles;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.w3c.dom.Text;
import uk.co.smcnamee.elreg.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Article.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Article#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Article extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "param1";
    private static final String ARG_SUBHEAD = "param2";
    private static final String ARG_BODY = "param3";

    // TODO: Rename and change types of parameters
    private String title;
    private String heading;
    private String body;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Article newInstance(String param1, String param2, String param3) {
        Article fragment = new Article();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, param1);
        args.putString(ARG_SUBHEAD, param2);
        args.putString(ARG_BODY, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public Article() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            heading = getArguments().getString(ARG_SUBHEAD);
            body = getArguments().getString(ARG_BODY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout sv = (LinearLayout)inflater.inflate(R.layout.fragment_blank, container, false);
        TextView tv = (TextView)sv.findViewById(R.id.title_text);
        tv.setText(title);
        tv = (TextView)sv.findViewById(R.id.heading_text);
        tv.setText(heading);
        tv = (TextView)sv.findViewById(R.id.body_text);
        tv.setText(body);

        return sv;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

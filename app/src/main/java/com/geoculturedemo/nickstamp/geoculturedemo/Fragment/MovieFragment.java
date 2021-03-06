package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.FontUtils;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MovieFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private TextView tvMovieTitle, tvMovieGenre, tvMovieCast, tvMovieDirector, tvMovieWriter, tvMovieRating, tvMovieRuntime, tvMovieSynopsis;
    private ImageView ivMovieImage;
    private ProgressBar pbImage;

    private Movie movie;
    private View fragmentView;

    public MovieFragment() {

    }

    public static MovieFragment newInstance(Movie movie1) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {

            fragmentView = inflater.inflate(R.layout.fragment_movie_details, container, false);

            ivMovieImage = (ImageView) fragmentView.findViewById(R.id.ivMovieImage);
            pbImage = (ProgressBar) fragmentView.findViewById(R.id.pbImage);

            tvMovieTitle = (TextView) fragmentView.findViewById(R.id.tvMovieTitle);
            tvMovieGenre = (TextView) fragmentView.findViewById(R.id.tvMovieGenre);
            tvMovieCast = (TextView) fragmentView.findViewById(R.id.tvMovieCast);
            tvMovieDirector = (TextView) fragmentView.findViewById(R.id.tvMovieDirector);
            tvMovieWriter = (TextView) fragmentView.findViewById(R.id.tvMovieWriter);
            tvMovieRating = (TextView) fragmentView.findViewById(R.id.tvMovieRating);
            tvMovieRuntime = (TextView) fragmentView.findViewById(R.id.tvMovieRuntime);
            tvMovieSynopsis = (TextView) fragmentView.findViewById(R.id.tvMovieSynopsis);

            FontUtils.setRobotoFont(getContext(), fragmentView);

            tvMovieRating.setText(movie.getRating());
            tvMovieRuntime.setText(movie.getRuntime());
            tvMovieGenre.setText(movie.getGenre());
            tvMovieDirector.setText(movie.getDirector());
            tvMovieCast.setText(movie.getCast());

            if (movie.getCast() == null || movie.getCast().trim().length() == 0)
                tvMovieCast.setText(getString(R.string.text_not_available));
            if (movie.getDirector() == null || movie.getDirector().trim().length() == 0)
                tvMovieDirector.setText(getString(R.string.text_not_available));

            new MovieDetailsParser().execute();
        }

        return fragmentView;
    }

    public class MovieDetailsParser extends AsyncTask<Void, Void, Void> {

        public MovieDetailsParser() {


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            //try to connect 3 times
            int tries = 0;
            boolean success = false;

            while (tries < 3 && !success) {
                tries++;

                try {
                    // Connect to the web site
                    Document document = Jsoup.connect(movie.getUrl()).get();

                    // Using Elements to get the class data
                    Elements images = document.select("#img_primary");
                    if (images.size() > 0) {
                        Element image = images.get(0);
                        movie.setImgUrl(image.getElementsByTag("img").attr("src"));
                    }
                    Element overview = document.getElementById("overview-top");

                    String title = overview.getElementsByClass("header").text();
                    movie.setTitle(title);

                    Elements writers = overview.getElementsByAttributeValue("itemprop", "creator").select("a[itemprop=url]");
                    String writer = "";
                    for (Element w : writers) {
                        writer += w.text() + " , ";
                    }
                    movie.setWriter(writer.substring(0, writer.length() - 3));

                    Elements storylines = document.select("#titleStoryLine");
                    if (storylines.size() > 0) {

                        String synopsis = storylines.first().getElementsByTag("p").text();
                        int pos = synopsis.indexOf("Written by");
                        synopsis = synopsis.substring(0, pos);
                        movie.setSynopsis(synopsis);
                    }


                    success = true;

                } catch (IOException e) {
                    Log.i("nikos", "IO Exception");
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    Log.i("nikos", "Out of bounds Exception");
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            Picasso.with(getContext())
                    .load(movie.getImgUrl())
                    .into(ivMovieImage);

            AnimationUtils.crossfade(ivMovieImage, pbImage);

            tvMovieTitle.setText(movie.getTitle());
            tvMovieWriter.setText(movie.getWriter());
            tvMovieSynopsis.setText(movie.getSynopsis());

            if (movie.getWriter() == null || movie.getWriter().trim().length() == 0)
                tvMovieWriter.setText(getString(R.string.text_not_available));
            if (movie.getSynopsis() == null || movie.getSynopsis().trim().length() == 0)
                tvMovieSynopsis.setText(getString(R.string.text_not_available));

        }

    }

}

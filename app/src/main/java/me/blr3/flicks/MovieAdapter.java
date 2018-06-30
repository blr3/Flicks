package me.blr3.flicks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.blr3.flicks.models.Config;
import me.blr3.flicks.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // list of movies declared as instance field
    ArrayList<Movie> movies;
    // config needed for image urls
    Config config;
    // context for rendering
    Context context;

    // initialize with list by generating a constructor
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
    }


    // creates and inflates a new view
    // !!!!! what does this mean ?????
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context and create the inflater
        context = parent.getContext();
        // !!!! what is the from notion ???
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie_layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // binds a inflated view to a new item
    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        // load image using glide
        Glide.with(context)
                .load(imageUrl)
                .apply(
                        RequestOptions.placeholderOf(R.drawable.flicks_movie_placeholder)
                                .error(R.drawable.flicks_movie_placeholder)
                                .fitCenter()
                                .transform(new RoundedCornersTransformation(15, 0))
                ).into(holder.ivPosterImage);
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }


    // create the viewholder as a static inner class
    // !!!!! confused why we need double constructors
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);


        }
    }
}


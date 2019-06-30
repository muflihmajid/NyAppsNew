package com.example.nytimesapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nytimesapps.Activity.detailActivity;
import com.example.nytimesapps.Model.Article;
import com.example.nytimesapps.R;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Article> articleList;
    public int pos1;
    public RecyclerViewAdapter(Context mContext, List<Article> movieList){
        this.mContext = mContext;
        this.articleList = movieList;
    }
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_card, viewGroup, false);

        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder( RecyclerViewAdapter.MyViewHolder viewHolder, int i){
        final Article article = articleList.get(i);
        viewHolder.title.setText(article.getHeadline().getMain());

        if(article.getMultimedia().isEmpty())
        {
            Glide.with(mContext)
                    .load(R.drawable.load)
                    .placeholder(R.drawable.load)
                    .into(viewHolder.thumbnail);
        }
        else
        {
            String poster = article.getMultimedia().get(1).getUrl();
            if(article.getMultimedia().size() > 2) {
                Glide.with(mContext)
                        .load(poster)
                        .placeholder(R.drawable.load)
                        .into(viewHolder.thumbnail);
            }
        }


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, snippset;
        public ImageView thumbnail;
        public MaterialFavoriteButton materialFavoriteButtonNice;
        public Article art,movie1;


        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            snippset = (TextView) view.findViewById(R.id.snipped);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            materialFavoriteButtonNice = (MaterialFavoriteButton) view.findViewById(R.id.favorite_button1);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    pos1 = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Article clickedDataItem = articleList.get(pos);
                        Intent intent = new Intent(mContext, detailActivity.class);
                        intent.putExtra("movies", clickedDataItem );
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
//            materialFavoriteButtonNice.setOnFavoriteChangeListener(
//                    new MaterialFavoriteButton.OnFavoriteChangeListener(){
//                        @Override
//                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite){
//                            if (favorite){
//                                int pos = getAdapterPosition();
//                                favoriteDbHelper = new FavoriteDbHelper(mContext);
//                                movie = new Movie();
//                                Double rate = movieList.get(pos).getVoteAverage();
//                                movie.setId(movieList.get(pos).getId());
//                                movie.setOriginalTitle(movieList.get(pos).getOriginalTitle());
//                                movie.setPosterPath(movieList.get(pos).getPosterPath());
//                                movie.setVoteAverage(rate);
//                                movie.setOverview(movieList.get(pos).getOverview());
//
//                                favoriteDbHelper.addFavorite(movie);
//                                Snackbar.make(buttonView, "Added to Favorite",
//                                        Snackbar.LENGTH_SHORT).show();
//                            }else{
//                                int movie_id = movieList.get(pos1).getId();
//                                favoriteDbHelper = new FavoriteDbHelper(mContext);
//                                favoriteDbHelper.deleteFavorite(movie_id);
//                                Snackbar.make(buttonView, "Removed from Favorite",
//                                        Snackbar.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    }
//            );
        }
    }
}

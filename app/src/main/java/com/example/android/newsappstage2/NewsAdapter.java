package com.example.android.newsappstage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> news) {
        super( context, 0, news );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from( getContext() ).inflate(
                    R.layout.news_list_item, parent, false );
        }

        News currentNews = getItem( position );
        TextView newsTitleTextView = (TextView) listItemView.findViewById( R.id.articletitle );
        String title = currentNews.getArticleTitle();
        newsTitleTextView.setText( title );


        TextView newsCategorytextView = (TextView) listItemView.findViewById( R.id.articlecategory );
        String category = currentNews.getArticleCategory();
        newsCategorytextView.setText( category );

        TextView newsDatetextView = (TextView) listItemView.findViewById( R.id.articledate );
        String date = currentNews.getArticleDate();
        newsDatetextView.setText( date );

        TextView newsAuthortextView = (TextView) listItemView.findViewById( R.id.articleauthors );
        String author = currentNews.getArticleAuthor();
        newsAuthortextView.setText( author );

        return listItemView;
    }

}

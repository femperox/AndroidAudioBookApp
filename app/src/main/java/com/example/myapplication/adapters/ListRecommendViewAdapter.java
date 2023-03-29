package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.classes.BookRecommendItem;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ListRecommendViewAdapter extends ArrayAdapter<BookRecommendItem>
{
    public ListRecommendViewAdapter(@NonNull Context context, ArrayList<BookRecommendItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.book_item_recommendation, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        BookRecommendItem currentItem = getItem(position);

        TextView id = listItem.findViewById(R.id.tv_bookIdRec);
        id.setText(Integer.toString(currentItem.getId()));

        ImageView picture = listItem.findViewById(R.id.book_recommend_image);
        picture.setBackgroundResource(currentItem.getPicture());

        TextView title = listItem.findViewById(R.id.book_recommend_title);
        title.setText(currentItem.getTitle());

        TextView author = listItem.findViewById(R.id.book_recommend_author);
        author.setText(currentItem.getTitle());

        TextView reader = listItem.findViewById(R.id.book_recommend_reader);
        reader.setText(currentItem.getReader());

        return listItem;
    }
}

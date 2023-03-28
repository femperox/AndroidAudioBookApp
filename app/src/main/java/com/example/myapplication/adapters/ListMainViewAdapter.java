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

import com.example.myapplication.classes.BookMainItem;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ListMainViewAdapter extends ArrayAdapter<BookMainItem>
{

    public ListMainViewAdapter(@NonNull Context context, ArrayList<BookMainItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.book_item_main, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        BookMainItem currentItem = getItem(position);

        ImageView picture = listItem.findViewById(R.id.book_main_image);
        picture.setBackgroundResource(currentItem.getPicture());

        TextView title = listItem.findViewById(R.id.book_main_title);
        title.setText(currentItem.getTitle());

        TextView time = listItem.findViewById(R.id.book_main_time);
        time.setText(currentItem.getTime().toString());

        TextView reader = listItem.findViewById(R.id.book_main_reader);
        reader.setText(currentItem.getReader());

        TextView genres = listItem.findViewById(R.id.book_main_genres);
        genres.setText(currentItem.getGenres());

        return listItem;
    }

}
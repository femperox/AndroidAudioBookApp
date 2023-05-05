package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.classes.BookMainItem;
import com.example.myapplication.R;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListMainViewAdapter extends ArrayAdapter<BookMainItem>
{
    private ArrayList<BookMainItem> myList;  // for loading main list
    private ArrayList<BookMainItem> arraylist=null;  // for loading  filter data

    public ListMainViewAdapter(@NonNull Context context, ArrayList<BookMainItem> items) {
        super(context, 0, items);
        System.out.println(items.size());
        this.myList = items;
        this.arraylist = new ArrayList<BookMainItem>();
        this.arraylist.addAll(myList);
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

        TextView id = listItem.findViewById(R.id.tv_bookId);
        id.setText(Integer.toString(currentItem.getId()));

        ImageView picture = listItem.findViewById(R.id.book_main_image);
        picture.setBackgroundResource(currentItem.getPicture());

        TextView title = listItem.findViewById(R.id.book_main_title);
        title.setText(currentItem.getTitle());

        TextView time = listItem.findViewById(R.id.book_main_time);
        time.setText(DurationFormatUtils.formatDuration(currentItem.getTime(), "HH:mm:ss", true));

        TextView reader = listItem.findViewById(R.id.book_main_reader);
        reader.setText(currentItem.getReader());

        TextView genres = listItem.findViewById(R.id.book_main_genres);
        genres.setText(currentItem.getGenres());

        return listItem;
    }

    public void filter(String charText, int filter_id) {
        charText = charText.toLowerCase(Locale.getDefault());
        myList.clear();
        if (charText.length() == 0) {
            myList.addAll(arraylist);
        }
        else
        {
            for (BookMainItem wp : arraylist) {
                switch (filter_id)
                {
                    case 0:
                        if (wp.getReader().toLowerCase(Locale.getDefault()).contains(charText))
                            myList.add(wp);
                        break;
                    case 1:
                        if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                            myList.add(wp);
                        break;
                }

            }

        }
        notifyDataSetChanged();
    }


}




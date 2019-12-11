package com.hobbiefinder.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hobbiefinder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by lucas.tomasi on 29/04/17.
 */

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder>
{
    private List<String> categorias;

    public CategoriaAdapter(List<String> list) {
        this.categorias = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_slideshow, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        viewHolder.setItemTitle(getCategorias(i));
    }

    @Override
    public int getItemCount()
    {
        if (this.categorias == null)
        {
            return 0;
        }
        return categorias.size();
    }

    public String getCategorias(int i) {
        return this.categorias.get(i);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView itemName;
        private Context context;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.tfdNome);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCard(v);
                }
            });
        }

        public void setItemTitle(String title) {
            itemName.setText(title);
        }

        public void onClickCard(final View v)
        {
//            Attachment a = getAttachment(getAdapterPosition());
        }
    }
}
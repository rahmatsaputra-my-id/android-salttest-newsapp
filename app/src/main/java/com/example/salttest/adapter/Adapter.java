package com.example.salttest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.salttest.R;
import com.example.salttest.utils.Utils;
import com.example.salttest.models.Artikel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
//import okhttp3.internal.Util;


public class Adapter extends RecyclerView.Adapter <Adapter.MyViewHolder>{

    private List<Artikel> _artikel;
    private Context _context;
    private OnItemClickListener _onItemClickListener;

    public Adapter(List<Artikel> artikels,Context context){
        this._artikel = artikels;
        this._context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewTtpe){
        View view = LayoutInflater.from(_context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(view, _onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holderr, int position){
        final MyViewHolder holder = holderr;
        Artikel model = _artikel.get(position);

        RequestOptions _requestOptions = new RequestOptions();
        _requestOptions.placeholder(Utils.getRandomDrawbleColor());
        _requestOptions.error(Utils.getRandomDrawbleColor());
        _requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        _requestOptions.centerCrop();
//        _requestOptions.timeout(3000);

        Glide.with(_context)
                .load(model.getUrlToImage())
                .apply(_requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder._proses_gmbr.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder._proses_gmbr.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder._gmbr);

        holder._judul.setText(model.getTitle());
        holder._deskripsi.setText(model.getDescription());
        holder._sumber.setText(model.getSource().getName());
        holder._tampilanWaktu.setText(" \u2022 "+ Utils.DateToTimeFormat(model.getPublishedAt()));
        holder._publishedAt.setText(Utils.DateFormat(model.getPublishedAt()));
        holder._author.setText(model.getAuthor());
    }

    @Override
    public int getItemCount(){
        return _artikel.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this._onItemClickListener = onItemClickListener;
    }

    private interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView _judul, _deskripsi, _author, _publishedAt, _sumber, _tampilanWaktu;
        ImageView _gmbr;
        ProgressBar _proses_gmbr;
        OnItemClickListener _onItemClickListener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

//            itemView.setOnClickListener(this);
            _judul = itemView.findViewById(R.id.judul);
            _deskripsi = itemView.findViewById(R.id.deskripsi);
            _author = itemView.findViewById(R.id.author);
            _publishedAt = itemView.findViewById(R.id.publishedAt);
            _sumber = itemView.findViewById(R.id.sumber);
            _tampilanWaktu = itemView.findViewById(R.id.tampilanWaktu);
            _gmbr = itemView.findViewById(R.id.gmbr);
            _proses_gmbr = itemView.findViewById(R.id.proses_gmbr);

            this._onItemClickListener = onItemClickListener;
        }

//        @Override
//        public void onClick(View view) {
//            _onItemClickListener.onItemClick(view, getAdapterPosition());
//
//        }
    }
}

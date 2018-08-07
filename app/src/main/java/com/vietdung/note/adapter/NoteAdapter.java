package com.vietdung.note.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.note.R;
import com.vietdung.note.activity.UpdateNoteActivity;
import com.vietdung.note.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.RecyclerviewHolder> {
    private List<Note> noteList;
    Activity context;
    public static String rq_itent_id="abc";
    public static String rq_itent_position="xyz";


    public NoteAdapter(Activity context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_note, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.RecyclerviewHolder holder, final int position) {
        holder.tv_Date.setText(noteList.get(position).getDateCreate());
        holder.tv_Note.setText(noteList.get(position).getNote());
        holder.tv_Title.setText(noteList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateNoteActivity.class);
                i.putExtra(rq_itent_id, noteList.get(position).getId());
                i.putExtra(rq_itent_position,position);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        TextView tv_Note, tv_Title, tv_Date, tv_Color;
        ImageView iv_Clock;

        public RecyclerviewHolder(View itemView) {
            super(itemView);
            tv_Title = itemView.findViewById(R.id.tvTitle);
            tv_Note = itemView.findViewById(R.id.tvHomeNote);
            tv_Date = itemView.findViewById(R.id.tvDate);


            iv_Clock = itemView.findViewById(R.id.ivClock);
            tv_Color = itemView.findViewById(R.id.tvColor);
        }
    }
}

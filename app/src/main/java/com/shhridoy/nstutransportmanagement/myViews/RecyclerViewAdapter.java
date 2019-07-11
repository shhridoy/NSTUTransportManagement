package com.shhridoy.nstutransportmanagement.myViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shhridoy.nstutransportmanagement.R;
import com.shhridoy.nstutransportmanagement.myObjects.BusSchedule;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BusSchedule> busScheduleList = null;
    private Context context;
    private String tag = null;
    private int previousPosition = -1;

    public RecyclerViewAdapter(Context context, List<BusSchedule> busScheduleList,  String tag) {
        this.context = context;
        this.busScheduleList = busScheduleList;
        this.tag = tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_schedule_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        /*if (position > previousPosition) { // scrolling down
            AnimationUtils.animateRecyclerView(holder.itemView, true);
        } else { // scrolling up
            AnimationUtils.animateRecyclerView(holder.itemView, false);
        }
        previousPosition = position;
        AnimationUtils.setFadeAnimation(holder.itemView);*/

        String key = busScheduleList.get(position).getKey();
        final String busTitle = busScheduleList.get(position).getBus_title();
        String busType = busScheduleList.get(position).getBus_type();
        String startTime = busScheduleList.get(position).getStart_time();
        String startPoint = busScheduleList.get(position).getStart_point();
        String endPoint = busScheduleList.get(position).getEnd_point();
        final String going = busScheduleList.get(position).getVote();

        holder.textView.setText(busTitle+"\n"+busType+"\n"+startTime+"\n"+startPoint+"\n"+endPoint+"\n"+going);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, busTitle+": "+going+" will be going.", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return busScheduleList.size();
    }

    private void removeAt(int position) {
        busScheduleList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, busScheduleList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener {

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {

        }*/
    }

}
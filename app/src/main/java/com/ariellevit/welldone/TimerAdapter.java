package com.ariellevit.welldone;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class TimerAdapter extends ArrayAdapter<Timer> {

    final MediaPlayer ring = MediaPlayer.create(getContext(), R.raw.cant_stop);
    boolean isCountdown = false;

    public static class ViewHolder{
        TextView name;
        TextView time;
        Button start;


    }


    public TimerAdapter(Context context, ArrayList<Timer> timers) {
        super(context,0, timers);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        //Get the item date for this position
        final Timer timer = getItem(position);

        final TimerAdapter.ViewHolder ViewHolder;

        //Check if an existing view is being reused, otherwise inflate a new view from row layout
        if (convertView == null){
            ViewHolder = new TimerAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.meal_list_row,parent,false);

            ViewHolder.name = (TextView) convertView.findViewById(R.id.listMealName);
            ViewHolder.time = (TextView) convertView.findViewById(R.id.listMealTime);
            ViewHolder.start = (Button) convertView.findViewById(R.id.buttonMealStart);

            convertView.setTag(ViewHolder);


            final Button startButton = ViewHolder.start;
            startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isCountdown){
                            long id = timer.getTimerId();
                            TimerDbAdapter db = new TimerDbAdapter(getContext());
                            db.open();
                            db.updateStartTimer(id);
                            db.close();
                            long start = timer.getStart();
                            long time = timer.getTime();
                            reverseTimer(time, ViewHolder.time);

                            startButton.setText("PAUSE");
                            isCountdown = true;

                        } else {
                            long now = Calendar.getInstance().getTimeInMillis();
                            long foodTime = timer.getTime()*1000;
                            long start = timer.getStart();
                            long result1 = (start + foodTime) - now ;
                            long result = result1 / 1000;

                            secToPause(result, ViewHolder.time);

                        }

                    }
                });




        }else {
            ViewHolder = (TimerAdapter.ViewHolder) convertView.getTag();
        }


        ViewHolder.name.setText(timer.getName());





        if(timer.getStart() < Calendar.getInstance().getTimeInMillis() && timer.getStart() > 0){
            long now = Calendar.getInstance().getTimeInMillis();
            long foodTime = timer.getTime()*1000;
            long result1 = (timer.getStart() + foodTime) - now ;
            long result = result1 / 1000;

            reverseTimer(result, ViewHolder.time);
        }else ViewHolder.time.setText("00:00");





        return convertView;
    }



    // //////////////COUNT DOWN START/////////////////////////
    public void reverseTimer(long Seconds, final TextView tv){

        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Done");
//                ring.start();
//                ring.seekTo(2);
            }
        }.start();
    }
    // //////////////COUNT DOWN END/////////////////////////


    public void secToPause (long Seconds, final TextView tv){
        int seconds = (int) Seconds;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        tv.setText(String.format("%02d", minutes)
                + ":" + String.format("%02d", seconds));

    }


}

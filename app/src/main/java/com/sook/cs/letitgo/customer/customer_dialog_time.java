package com.sook.cs.letitgo.customer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Date;


public class customer_dialog_time extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private String time;
    private int mSeq;
    private MyViewHolder holder;
    private DBHelperCart helper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE)+10;
        TimePickerDialog dialog = new TimePickerDialog(
                getContext(), TimePickerDialog.THEME_HOLO_LIGHT, this, hour, min, DateFormat.is24HourFormat(getContext()));

        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = getDate() + (hourOfDay < 10 ? " 0" : " ") + hourOfDay + (minute < 10 ? ":0" : ":") + minute + ":00";
        holder.cBinding.tvTime.setText(time);
        helper.updateTime(mSeq, time);
    }

    public void setValues(MyViewHolder holder, DBHelperCart helper, int mSeq) {
        this.holder = holder;
        this.helper = helper;
        this.mSeq = mSeq;
    }

    public String getDate() {
        long now = System.currentTimeMillis();
        Date currentDate = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(currentDate);
        return date;
    }
}

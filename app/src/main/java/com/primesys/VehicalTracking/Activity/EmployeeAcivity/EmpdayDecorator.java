package com.primesys.VehicalTracking.Activity.EmployeeAcivity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.format.DateFormat;

import com.primesys.VehicalTracking.Dto.EmpAttendanceDTO;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import java.util.Date;


/**
 * Created by pt002 on 18/12/17.
 */


public class EmpdayDecorator implements CalendarCellDecorator {


    @Override
    public void decorate(CalendarCellView cellView, Date date) {

        String dayOfTheWeek = (String) DateFormat.format("EEE", date); // Thu
        int day          =Integer.parseInt(DateFormat.format("dd",   date).toString()); // 20
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String monthNumber  = (String)DateFormat.format("MM",   date); // 06
        String year         =(String) DateFormat.format("yyyy", date); // 2013
        String dateString = Integer.toString(date.getDate());

        for (EmpAttendanceDTO obj:LeaveApplicationActivity.attendsncelist) {
            //   Log.e("-----myMonth-------", date.getDate()+"--"+obj.getDay());

            if (date.getDay()!=0&&Integer.parseInt(obj.getDay())==day&&monthString.equalsIgnoreCase(obj.getMonth())){

                //     Log.e("------------", date.getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                if (obj.getAtt_type().equalsIgnoreCase("2")){
                    SpannableString string = new SpannableString(dateString + "\nPresent");
//                    string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
//                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    cellView.getDayOfMonthTextView().setText(string);
                    cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#008000"));

                    //     Log.e("----1111111111--------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                }
                else if (obj.getAtt_type().equalsIgnoreCase("1"))
                {
                    SpannableString string = new SpannableString(dateString + "\nAbsent");
                  /*  string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
                    cellView.getDayOfMonthTextView().setText(string);
                    cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#0000CD"));

                    //   Log.e("----2222222222--------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());



                }else if (obj.getAtt_type().equalsIgnoreCase("3"))
                {
                    SpannableString string = new SpannableString(dateString + "\nHoliday");
                   /* string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
                    cellView.getDayOfMonthTextView().setText(string);
                    cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#ff0000"));

                    //   Log.e("----333333333------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                }else if (obj.getAtt_type().equalsIgnoreCase("4"))
                {

                    if (obj.getIs_grant().equalsIgnoreCase("1"))
                    {
                        SpannableString string = new SpannableString(dateString + "\nLeave Req");
                  /*  string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
                        cellView.getDayOfMonthTextView().setText(string);
                        cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#FFFF00"));

                        //   Log.e("----333333333------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                        //    Log.e("----4444444444444------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                    }else {
                        SpannableString string = new SpannableString(dateString + "\nLeave Grant");
                  /*  string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
                        cellView.getDayOfMonthTextView().setText(string);
                        cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#FF0000"));

                        //    Log.e("----333333333------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());
//
                        //    Log.e("----4444444444444------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                    }

                }

            }else if (date.getDay()==0){
                SpannableString string = new SpannableString(dateString + "\nHoliday");
               /* string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
                cellView.getDayOfMonthTextView().setText(string);
                cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#ff0000"));

                //  Log.e("----55555555--------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());

            }else {
/*

                SpannableString string = new SpannableString(dateString + "\nAbsent");
               string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                cellView.getDayOfMonthTextView().setText(string);
                cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#0100e5"));

                Log.e("----55555555--------", date.getTime()+"--"+obj.getDay()+"--"+obj.getAtt_type());
*/

            }
        }


/*
        if (date.getDay()==0){
            SpannableString string = new SpannableString(dateString + "\nHoliday");
            string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            cellView.getDayOfMonthTextView().setText(string);
            cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#FDF5E6"));

        }else {
            SpannableString string = new SpannableString(dateString + "\nPresent");
            string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            cellView.getDayOfMonthTextView().setText(string);
            cellView.getDayOfMonthTextView().setTextColor(Color.parseColor("#8BC34A"));

        }*/

    }
}


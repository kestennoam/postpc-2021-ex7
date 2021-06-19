//package com.example.postpc_ex7;
//
//
//import android.content.Intent;
//import android.widget.CheckBox;
//import android.widget.EditText;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//@RunWith(RobolectricTestRunner.class)
//@Config(sdk = 28)
//public class tests_ex7 {
//
//    @Test
//    public void check_basic_values_in_order_screen() {
//
//        Sandwich order = new Sandwich("", false, false, 1, "");
//        Intent intent = new Intent();
//        intent.putExtra("order", order);
//        NewOrderActivity activityUnderTest = Robolectric.buildActivity(OrderActivity.class, intent).create().visible().get();
//        com.google.android.material.slider.Slider pickles = activityUnderTest.findViewById(R.id.picklesInsert);
//
//        Assert.assertEquals(order.getPickles() ,(int) pickles.getValue());
//        Assert.assertEquals(order.isHummus() ,((CheckBox) activityUnderTest.findViewById(R.id.hummusSwitch)).isChecked());
//        Assert.assertEquals(order.isTahini() ,((CheckBox) activityUnderTest.findViewById(R.id.tahiniSwitch)).isChecked());
//        Assert.assertEquals(order.getCustomerName() ,((EditText) activityUnderTest.findViewById(R.id.commentEditText)).getText().toString());
//        Assert.assertEquals(order.getComment() ,((EditText) activityUnderTest.findViewById(R.id.commentEditText)).getText().toString());
//    }
//
//    @Test
//    public void check_change_of_pickles_in_order(){
//        Order order = new Order("", false, false, 1, "");
//        Intent intent = new Intent();
//        intent.putExtra("order", order);
//        OrderActivity activityUnderTest = Robolectric.buildActivity(OrderActivity.class, intent).create().visible().get();
//        com.google.android.material.slider.Slider pickles = activityUnderTest.findViewById(R.id.picklesInsert);
//        pickles.setValue(5);
//        Assert.assertEquals((int) pickles.getValue(), 5);
//    }
//
//    @Test
//    public void check_change_of_checkBoxes_in_order(){
//        Order order = new Order("", false, false, 1, "");
//        Intent intent = new Intent();
//        intent.putExtra("order", order);
//        OrderActivity activityUnderTest = Robolectric.buildActivity(OrderActivity.class, intent).create().visible().get();
//        CheckBox hummus = (CheckBox) activityUnderTest.findViewById(R.id.hummusCheckBox);
//        CheckBox tahini = (CheckBox) activityUnderTest.findViewById(R.id.tahiniCheckBox);
//        hummus.setChecked(true);
//        tahini.setChecked(true);
//        Assert.assertEquals(true ,((CheckBox) activityUnderTest.findViewById(R.id.hummusCheckBox)).isChecked());
//        Assert.assertEquals(true ,((CheckBox) activityUnderTest.findViewById(R.id.tahiniCheckBox)).isChecked());
//    }
//
//
//
//}

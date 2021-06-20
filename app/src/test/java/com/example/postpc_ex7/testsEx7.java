package com.example.postpc_ex7;


import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class testsEx7 {

    @Test
    public void check_default_values_in_new_order_screen() {
        // setup
        Sandwich sandwich = new Sandwich("", 0, true, false, "");
        Intent intent = new Intent();
        intent.putExtra("sandwich", sandwich);

        // test
        NewOrderActivity activityUnderTest = Robolectric.buildActivity(NewOrderActivity.class, intent).create().visible().get();
        com.google.android.material.textview.MaterialTextView pickles = activityUnderTest.findViewById(R.id.picklesTitleTextView);

        // verify
        Assert.assertEquals(sandwich.isTahini(), ((Switch) activityUnderTest.findViewById(R.id.tahiniSwitch)).isChecked());
        Assert.assertEquals(sandwich.isHummus(), ((CheckBox) activityUnderTest.findViewById(R.id.hummusSwitch)).isChecked());
        Assert.assertEquals(sandwich.getPickles(), Integer.parseInt(pickles.getText().toString()));
        Assert.assertEquals(sandwich.getCustomerName(), ((EditText) activityUnderTest.findViewById(R.id.commentEditText)).getText().toString());
        Assert.assertEquals(sandwich.getComment(), ((EditText) activityUnderTest.findViewById(R.id.commentEditText)).getText().toString());
    }

    @Test
    public void check_change_comment_in_new_order_activity() {
        Sandwich sandwich = new Sandwich("", 0, true, false, "");
        Intent intent = new Intent();
        intent.putExtra("sandwich", sandwich);
        NewOrderActivity activityUnderTest = Robolectric.buildActivity(NewOrderActivity.class, intent).create().visible().get();

        // test
        com.google.android.material.textview.MaterialTextView comment = activityUnderTest.findViewById(R.id.commentEditText);
        comment.setText("Test");
        Assert.assertEquals(sandwich.getComment(), "Test");
    }

    //
    @Test
    public void check_change_switches_in_new_order_activity() {
        // setup
        Sandwich sandwich = new Sandwich("", 0, true, false, "");
        Intent intent = new Intent();
        intent.putExtra("sandwich", sandwich);
        NewOrderActivity activityUnderTest = Robolectric.buildActivity(NewOrderActivity.class, intent).create().visible().get();


        // test
        Switch tahini = (Switch) activityUnderTest.findViewById(R.id.tahiniSwitch);
        Switch hummus = (Switch) activityUnderTest.findViewById(R.id.hummusSwitch);
        tahini.setChecked(true);
        hummus.setChecked(false);

        // verify
        Assert.assertEquals(true, ((Switch) activityUnderTest.findViewById(R.id.tahiniSwitch)).isChecked());
        Assert.assertEquals(false, ((Switch) activityUnderTest.findViewById(R.id.hummusSwitch)).isChecked());
    }


}

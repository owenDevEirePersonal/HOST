package com.deveire.dev.host;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{

    private Button driverButton;
    private Button managerButton;

    private Button driver2Button;
    private Button manager2Button;
    private Button basicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        driver2Button = (Button) findViewById(R.id.driver2Button);
        manager2Button = (Button) findViewById(R.id.manager2Button);
        basicButton = (Button) findViewById(R.id.basicButton);



        driver2Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Station2Activity.class));
            }
        });

        manager2Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Manager2Activity.class));
            }
        });

        basicButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), NfcWriterActivity.class));
            }
        });
    }


}

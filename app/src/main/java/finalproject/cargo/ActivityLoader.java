package finalproject.cargo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

public class ActivityLoader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HomeActivity a = new HomeActivity();
        a.setArguments(getIntent().getExtras());
        ft.replace(R.id.framefragment, a);
        ft.commit();

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.botnavbar, );

    }


}

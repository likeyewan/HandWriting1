package com.example.handwriting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentManager;
import com.example.handwriting.fragment.Fragment1;
import com.example.handwriting.fragment.Fragment2;
import com.example.handwriting.R;

public class WordListActivity extends AppCompatActivity {
    TextView title;
    private TextView marketBar;
    private TextView hatBar;
    Fragment1 wordSFragment;
    Fragment2 wordWFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        marketBar = (TextView) findViewById(R.id.bar_market);
        hatBar = (TextView) findViewById(R.id.bar_hat);
        marketBar.setClickable(true);
        hatBar.setClickable(true);
        setAllNotSelected();
        marketBar.setSelected(true);
        wordSFragment = new Fragment1();
        wordWFragment = new Fragment2();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, wordSFragment).commit();
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("汉字列表");
    }
    private void setAllNotSelected() {
        marketBar.setSelected(false);
        hatBar.setSelected(false);
    }
    public void tabSelect(View v) {
        setAllNotSelected();
        switch (v.getId()) {
            case R.id.bar_market:
                marketBar.setSelected(true);
                fragmentManager.beginTransaction().replace(R.id.main_fragment,wordSFragment).commit();
                break;
            case R.id.bar_hat:
                hatBar.setSelected(true);
                fragmentManager.beginTransaction().replace(R.id.main_fragment, wordWFragment).commit();
                break;
            default:
                break;
        }
    }
}

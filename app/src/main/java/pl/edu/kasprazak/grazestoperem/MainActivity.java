package pl.edu.kasprazak.grazestoperem;

import android.app.Activity;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.min;

public class MainActivity extends Activity {

    boolean runningClock = false;
    int counter = 50;

    private Runnable worker;
    private Button action;
    private TextView clock;
    private Handler handler;
    private ArrayList<Integer> bestTriesIntArray;
    private LinearLayout lastTriesListView;
    private LinearLayout bestTriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        action = (Button) findViewById(R.id.action);
        clock = (TextView) findViewById(R.id.clock);

        bestTriesIntArray = new ArrayList<Integer>();

        lastTriesListView = (LinearLayout) findViewById(R.id.lastTriesList);
        bestTriesListView = (LinearLayout) findViewById(R.id.bestTriesList);

        // Tutaj tworzymy anonimową klasę która implementuje interfejs Runnable
        // Odpowiada to definicji class MyRunnable implements Runnable.
        worker = new Runnable() {
            @Override
            public void run() {
                Log.d("LICZNIK", "DZIAŁAM " + counter);
                clock.setText("" + counter);
                --counter;
                if (runningClock) {
                    handler.postDelayed(worker, 1);
                }
            }
        };
        handler = new Handler();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runningClock) {
                    counter = 50;
                    runningClock = true;
                    handler.postDelayed(worker, 1);
                } else {
                    runningClock = false;
                    updateLastTriesList(counter);
                    updateBestTriesList(counter);
                }
            }

            private void removeTooMuchViewsFromLayout(LinearLayout layout, Integer viewsLimit) {
                int childcount = layout.getChildCount();

                while(childcount > viewsLimit) {
                    layout.removeViewAt(viewsLimit);
                    childcount = layout.getChildCount();
                }
            }

            private void updateLastTriesList(Integer clockValue) {
                TextView newEntry = new TextView(MainActivity.this);
                newEntry.setText(""+clockValue);
                newEntry.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));


                lastTriesListView.addView(newEntry, 0);

                removeTooMuchViewsFromLayout(lastTriesListView, 10);
            }

            private void updateBestTriesList(Integer clockValue) {
                bestTriesIntArray.add(clockValue);

                Collections.sort(bestTriesIntArray, new Comparator<Integer>() {
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(Math.abs(o1), Math.abs(o2));
                    }
                });

                Integer limit = min(bestTriesIntArray.size(), 10);

                bestTriesListView.removeAllViews();

                for( Integer i=0; i<limit; ++i) {
                    TextView pos = new TextView(MainActivity.this);
                    pos.setText(""+(bestTriesIntArray.get(i)));
                    pos.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    bestTriesListView.addView(pos);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Wydaje się, że czegoś tutaj brakuje
        Log.d("CYKL_ZYCIA", "ONPAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Podobnie jak tutaj, też czegoś brakuje
        Log.d("CYKL_ZYCIA", "ONRESUME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CYKL_ZYCIA", "ONSTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CYKL_ZYCIA", "ONSTOP");
    }
}
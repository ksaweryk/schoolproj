package pl.edu.kasprazak.grazestoperem;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Integer.min;

//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity {

    // lista pol klasy - zmiennych, ktore sa widoczne w kazdej metodzie klasy
    boolean runningClock = false;
    int counter = 50;
    private Runnable worker;
    private Button action;
    private TextView clock;
    private Handler handler;
    private ArrayList<Integer> bestTriesIntArray;
    private LinearLayout lastTriesListView;
    private LinearLayout bestTriesListView;
    private Integer maxNumberOfItemsOnList = 10;

    // onCreate wykonuje sie raz, kirdy appka startuje, lub jest wznawiana po wyrzuceniu z pamieci urzadzenia
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicjalizacja pamieci dla listy Integer'ow
        bestTriesIntArray = new ArrayList<Integer>();
        // podstawienie elementow layoutu pod pola klasy, tak, aby mozna je bylo modyfikowac za pomoca kodu javy
        action = (Button) findViewById(R.id.action);
        clock = (TextView) findViewById(R.id.clock);
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
            // onClock wykonuje sie, kiedy element action (czyli nasz Button) zostaje klikniety (tapniety, na touchscreenie)
            @Override
            public void onClick(View v) {
                // jezeli zegar, nie chodzi, to ustaw na 50 i startuj, w przeciwnym razie zatrzymaj i aktualizuj listy prob
                if (!runningClock) {
                    counter = 50;
                    Log.d("BUTTON", "START" + counter);
                    runningClock = true;
                    handler.postDelayed(worker, 1);
                } else {
                    runningClock = false;
                    Log.d("BUTTON", "STOP" + counter);
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

                Log.d("LAOUT", "dodaję last try" + clockValue);
                lastTriesListView.addView(newEntry, 0);

                removeTooMuchViewsFromLayout(lastTriesListView, maxNumberOfItemsOnList);
            }

            private void updateBestTriesList(Integer clockValue) {
                bestTriesIntArray.add(clockValue);

                Collections.sort(bestTriesIntArray, new Comparator<Integer>() {
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(Math.abs(o1), Math.abs(o2));
                    }
                });

                Integer limit = min(bestTriesIntArray.size(), maxNumberOfItemsOnList);

                bestTriesListView.removeAllViews();

                for( Integer i=0; i<limit; ++i) {
                    TextView pos = new TextView(MainActivity.this);
                    pos.setText(""+(bestTriesIntArray.get(i)));
                    pos.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    Log.d("LAOUT", "dodaję best try" + clockValue);
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
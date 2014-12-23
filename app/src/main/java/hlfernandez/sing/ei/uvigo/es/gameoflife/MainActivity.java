package hlfernandez.sing.ei.uvigo.es.gameoflife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    public static final int CELL_SIZE = 50;
    private static int INTERVAL = 500;
    private static final int RESULT_SETTINGS = 1;

    private Board board;
    private Thread animatorThread;
    private boolean auto;

    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        updatePreferences();

        gridview = (GridView) findViewById(R.id.gridView);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final int numCols = metrics.widthPixels / CELL_SIZE;
        final int numRows = metrics.heightPixels / CELL_SIZE;

        gridview.setNumColumns(numCols);
        gridview.setColumnWidth(CELL_SIZE);

        this.board = new Board(Board.randomCells(numRows, numCols));

        gridview.setAdapter(new BoardAdapter(this, board, numRows, numCols));

        final Button button = (Button) findViewById(R.id.btnNext);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                board.nextGeneration();
                gridview.invalidateViews();
            }
        });

        final Button button2 = (Button) findViewById(R.id.btnClear);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                board.setCells();
                gridview.invalidateViews();
                Toast.makeText(
                    MainActivity.this,
                    "Click on any cell to create it",
                    Toast.LENGTH_SHORT)
                .show();
            }
        });

        final Button button3 = (Button) findViewById(R.id.btnRandom);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                board.setCells(Board.randomCells(numRows, numCols));
                gridview.invalidateViews();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(!auto) {
                    int row = position / numCols;
                    int col = position % numCols;
                    Cell newCell = new Cell(row, col);
                    if (board.getCells().contains(newCell)) {
                        board.getCells().remove(newCell);
                    } else {
                        board.getCells().add(newCell);
                    }
                    gridview.invalidateViews();
                }
            }
        });

        Switch mySwitch = (Switch) findViewById(R.id.switch1);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                button.setEnabled(!isChecked);
                button2.setEnabled(!isChecked);
                button3.setEnabled(!isChecked);
                auto = isChecked;
                if(isChecked){
                    animatorThread = new Thread(new BoardAnimator());
                    animatorThread.start();
                }else{
                    animatorThread.interrupt();
                    animatorThread = null;
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                updatePreferences();
                break;

        }

    }

    private void updatePreferences() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        try {
            int newInterval = Integer.valueOf(sharedPrefs.getString("pref_interval", "NULL"));
            INTERVAL = newInterval;
        } catch(Exception e){

        }
    }

    class BoardAnimator implements Runnable {
        private boolean interrupted = false;

        @Override
        public void run() {
            while (!interrupted) {
                if (auto) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            board.nextGeneration();
                            gridview.invalidateViews();
                        }
                    });

                }
                try {
                    Thread.currentThread();
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        }
    }

    @Override
    public void finalize() {
        if (this.animatorThread != null) {
            this.animatorThread.interrupt();
        }
    }
}

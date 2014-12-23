package hlfernandez.sing.ei.uvigo.es.gameoflife;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class BoardAdapter extends BaseAdapter {
    private Context mContext;
    private  Board board;
    private int rows;
    private int cols;

    public BoardAdapter(Context c, Board board, int rows, int cols) {
        mContext = c;
        this.board = board;
        this.rows = rows;
        this.cols = cols;
    }

    public int getCount() {
        return rows * cols;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        imageView = new ImageView(mContext);
        if(board.getCells().contains(
                new Cell(position/cols, position%cols))
        ){
            imageView.setImageResource(R.drawable.black);
        } else {
            imageView.setImageResource(R.drawable.blank);
        }
        imageView.setLayoutParams(
            new GridView.LayoutParams(MainActivity.CELL_SIZE, MainActivity.CELL_SIZE)
        );
        return imageView;
    }

}
package com.rosegold.todoapp.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.rosegold.todoapp.Presenter.Adapter.ToDoAdapter;
import com.rosegold.todoapp.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * Task item touch handler on Recyclerview
 */
public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;

    /**
     * Constructor Method
     * @param adapter Recyclerview adapter
     */
    public RecyclerViewTouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    /**
     * Task item move functionality disabled.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Handles swipes left and right
     * @param viewHolder adapter
     * @param direction Swiped left = edit item. Swiped right = delete item.
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are You Sure ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteTask(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext() , R.color.color_accent))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit)
                .addSwipeRightBackgroundColor(R.color.design_default_color_error)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
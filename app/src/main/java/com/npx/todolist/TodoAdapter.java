package com.npx.todolist;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private Context context;
    private TodoBus bus;
    private View.OnClickListener onComplete;

    public TodoAdapter(@NonNull Context context, int resource, List<Todo> list, View.OnClickListener onComplete) {
        super(context, resource, list);
        this.context = context;
        this.onComplete = onComplete;
        bus = new TodoBus(context);
    }

    private class ViewSelected {
        private TextView title;
        private TextView when;
        private Button btComplete;

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getWhen() {
            return when;
        }

        public void setWhen(TextView when) {
            this.when = when;
        }

        public Button getBtComplete() {
            return btComplete;
        }

        public void setBtComplete(Button btComplete) {
            this.btComplete = btComplete;
        }
    }

    @Nullable
    public View getView(int index, View viewConverter, @NonNull ViewGroup group) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final Todo item = getItem(index);

        ViewSelected select;
        if (viewConverter == null) {

            if (layoutInflater == null)
                return null;

            if (onComplete == null)
                viewConverter = layoutInflater.inflate(R.layout.list_complete_item, null);
            else
                viewConverter = layoutInflater.inflate(R.layout.list_item, null);

            select = new ViewSelected();
            select.setTitle((TextView) viewConverter.findViewById(R.id.tvTitle));
            select.setWhen((TextView) viewConverter.findViewById(R.id.tvWhen));
            if (onComplete != null) {
                Button v = viewConverter.findViewById(R.id.btComplete);
                select.setBtComplete(v);
            }
            viewConverter.setTag(select);
        } else {
            select = (ViewSelected) viewConverter.getTag();
        }

        if (item == null)
            return viewConverter;

        select.getTitle().setText(item.getTitle());

        if (item.getWhen() != null && item.getWhen() > 0) {
            DateFormat sdf = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            select.getWhen().setText(sdf.format(item.getWhen()));
        }

        if (onComplete != null)
            select.getBtComplete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bus.complete(item.getId());
                    onComplete.onClick(v);
                }
            });

        return viewConverter;
    }

}

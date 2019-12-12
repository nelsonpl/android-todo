package com.npx.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment implements IFragment {

    View.OnClickListener onBuildLvItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buildLvItem();
        }
    };

    ListView lvItems;
    TodoAdapter adapter;
    SwipeRefreshLayout mySwipeRefreshLayout;
    TodoBus bus;
    boolean isComplete;

    public TodoListFragment() {
    }

    public static TodoListFragment newInstance(boolean isComplete) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle b = new Bundle();
        b.putBoolean("ISCOMPLETE", isComplete);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.isComplete = getArguments().getBoolean("ISCOMPLETE");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_todo_list, container, false);
        bus = new TodoBus(getContext());
        configBtn(inflate);
        configLvItem(inflate);
        configSwipeRefreshLayout(inflate);
        return inflate;
    }

    private void configBtn(View inflate) {
        FloatingActionButton fab = inflate.findViewById(R.id.fab);

        if (isComplete)
            fab.setImageResource(android.R.drawable.ic_menu_delete);
        else
            fab.setImageResource(android.R.drawable.ic_input_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isComplete) {
                    bus.clear();
                    buildLvItem();
                } else {
                    Intent i = new Intent(getContext(), CreateActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void configLvItem(View inflate) {
        lvItems = inflate.findViewById(R.id.lvItems);

        adapter = new TodoAdapter(getContext(), R.id.lvItems, new ArrayList<Todo>(), onBuildLvItem);
        lvItems.setAdapter(adapter);

        buildLvItem();

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Todo item = adapter.getItem(i);

                if (item == null)
                    return;

                long msgId = item.getId();

                Intent intent = new Intent(getContext(), CreateActivity.class);
                intent.putExtra(Const.ID, msgId);
                startActivity(intent);
            }
        });
    }

    public void buildLvItem() {

        adapter.clear();

        List<Todo> list;

        if (isComplete)
            list = bus.listComplete();
        else
            list = bus.list();

        if (list == null)
            return;
        adapter.addAll(list);
    }

    private void configSwipeRefreshLayout(View inflate) {
        mySwipeRefreshLayout = inflate.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        buildLvItem();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public void update() {
        buildLvItem();
    }
}

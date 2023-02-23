package cat.dam.andy.sqlite_recyclerview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cat.dam.andy.sqlite_recyclerview.adapter.CustomRecyclerView;
import cat.dam.andy.sqlite_recyclerview.database.DatabaseHelper;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseHelper mDatabaseHelper;
    private CustomRecyclerView mAdapter;
    private RecyclerView rc_nameList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
        initDatabase();
        initListeners();
    }

    private void initViews() {
        rc_nameList = findViewById(R.id.rc_nameList);
        fab = findViewById(R.id.fab);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rc_nameList.setLayoutManager(linearLayoutManager);
        rc_nameList.setHasFixedSize(true);
    }

    private void initDatabase() {
        mDatabaseHelper = new DatabaseHelper(this);
        ArrayList<Item> items = mDatabaseHelper.listAll();
        if(items.size() > 0){
            rc_nameList.setVisibility(View.VISIBLE);
            mAdapter = new CustomRecyclerView(this, items);
            rc_nameList.setAdapter(mAdapter);

        }else {
            rc_nameList.setVisibility(View.GONE);
            Toast.makeText(this, R.string.no_contacts, Toast.LENGTH_LONG).show();
        }
    }

    private void initListeners() {
        fab.setOnClickListener(view -> showAddDialog());
    }

    private void showAddDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.dialog_new_item, null);
        final EditText et_name = subView.findViewById(R.id.et_name);
        final EditText et_phone = subView.findViewById(R.id.et_phone);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.new_contact);
        alertDialog.setView(subView);
        alertDialog.create();

        alertDialog.setPositiveButton(R.string.add_contact, (dialog, which) -> {
            final String name = et_name.getText().toString();
            final String phone = et_phone.getText().toString();

            if(TextUtils.isEmpty(name)){
                Toast.makeText(MainActivity.this, R.string.noadd_error, Toast.LENGTH_LONG).show();
            }
            else{
                Item newItem = new Item(name, phone);
                mDatabaseHelper.addContact(newItem);
                finish();
                startActivity(getIntent());
            }
        });

        alertDialog.setNegativeButton(R.string.cancel, (dialog, which) -> Toast.makeText(MainActivity.this, R.string.cancelled_task, Toast.LENGTH_LONG).show());
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabaseHelper != null){
            mDatabaseHelper.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView =  (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String nouText) {
                if (mAdapter!=null)
                    mAdapter.getFilter().filter(nouText);
                return true;
            }
        });
    }
}

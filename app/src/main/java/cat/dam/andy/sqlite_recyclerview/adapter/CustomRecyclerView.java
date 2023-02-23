package cat.dam.andy.sqlite_recyclerview.adapter;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import cat.dam.andy.sqlite_recyclerview.Item;
import cat.dam.andy.sqlite_recyclerview.database.Database;
import cat.dam.andy.sqlite_recyclerview.R;

public class CustomRecyclerView extends RecyclerView.Adapter<ViewHolder> implements Filterable{
    private final Context context;
    private ArrayList<Item> dataSet;
    private final ArrayList<Item> itemList;
    private final Database database;

    //Constructor, aquí passem els ítems que mostrarem, és a dir, el model de dades
    public CustomRecyclerView(Context context, ArrayList<Item> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
        this.itemList = dataSet;
        database = new Database(context);
    }

    //Només es crida la primera vegada en crear la llista
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Creem la vista de cada ítem a partir del nostre layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //Es crida cada vegada que es fa scroll a la pantalla i els elements desapareixen i apareixen
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //Donem valor als views de cada card mitjançant el ViewHolder
        final Item item = dataSet.get(position);
        viewHolder.getName().setText(item.getName());
        viewHolder.getPhone().setText(item.getTel());
        viewHolder.getEditImg().setOnClickListener(view -> showEditionDialog(item));
        viewHolder.getDeleteImg().setOnClickListener(view -> {
            //elimina fila de la base de dades
            database.removeContact(item.getId());
            //refresca la pàgina de l'activity per veure canvis
            ((Activity)context).finish();
            context.startActivity(((Activity) context).getIntent());
        });
    }

    // Tornem el nombre d'ítems del nostre arraylist, l'invoca automàticament el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //Gestiona la cerca amb filtre
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //si no hi ha text a la barra de cerca, mostrem tots els ítems
                    dataSet= itemList;
                } else {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (Item item : itemList) {
                        if (item.getName().toLowerCase().contains(charString)) {
                            filteredList.add(item);
                        }
                    }
                    dataSet = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSet;
                return filterResults;
            }
            //Notifica els canvis de la llista de resultats de la cerca
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataSet = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //Mostra el diàleg d'edició que permet editar un contacte
    private void showEditionDialog(final Item item){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.dialog_new_item, null);
        final EditText et_name = subView.findViewById(R.id.et_name);
        final EditText et_phone = subView.findViewById(R.id.et_phone);
        if(item != null){
            et_name.setText(item.getName());
            et_phone.setText(String.valueOf(item.getTel()));
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.contact_edition);
        alertDialog.setView(subView);
        alertDialog.create();
        alertDialog.setPositiveButton(R.string.edit_contact, (dialog, which) -> {
            final String nom = et_name.getText().toString();
            final String tel = et_phone.getText().toString();
            if(TextUtils.isEmpty(nom)){
                Toast.makeText(context, R.string.noadd_error, Toast.LENGTH_LONG).show();
            }
            else{
                if (item != null) {
                    database.updateContact(new Item(item.getId(), nom, tel));
                    //refresh the activity
                    ((Activity) context).finish();
                    context.startActivity(((Activity) context).getIntent());
                }
                else {
                    Toast.makeText(context, R.string.contact_null, Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, (dialog, which) -> Toast.makeText(context, R.string.cancelled_task, Toast.LENGTH_LONG).show());
        alertDialog.show();
    }
}

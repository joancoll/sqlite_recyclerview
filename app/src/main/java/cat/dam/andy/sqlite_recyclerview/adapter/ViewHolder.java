package cat.dam.andy.sqlite_recyclerview.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import cat.dam.andy.sqlite_recyclerview.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    //Aquí obtenim la referència als nostres elements visuals
    private final TextView name;
    private final TextView phone;
    private final ImageView img_delete;
    private final ImageView img_edit;

    public ViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tv_name);
        phone = itemView.findViewById(R.id.tv_phone);
        img_delete = itemView.findViewById(R.id.img_delete);
        img_edit = itemView.findViewById(R.id.img_edit);
    }
    //Getters
    public TextView getName() {
        return name;
    }
    public TextView getPhone() {
        return phone;
    }
    public ImageView getDeleteImg() {
        return img_delete;
    }
    public ImageView getEditImg() {
        return img_edit;
    }
}

package com.example.alwaqt;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Intent;
public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
            holder.mosqueName.setText(model.getMosqueName()+" (#"+model.getMosqueID()+")");
            holder.namaaz1.setText("Fajr            \t"+model.getFajrNamaaz());
            holder.namaaz2.setText("Zuhar         \t"+model.getZuharAzaan());
            holder.namaaz3.setText("Asr             \t"+model.getAsrNamaaz());
            holder.namaaz4.setText("Maghrib    \t"+model.getMaghribNamaaz());
            holder.namaaz5.setText("Isha            \t"+model.getIshaNamaaz());

            holder.btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+model.getLocation()));
                    view.getContext().startActivity(intent);
                    //model.getLocation();
                }
            });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView getID,mosqueName,namaaz1,namaaz2,namaaz3,namaaz4,namaaz5;
        Button search,btnMap;
        public myViewHolder(@NonNull View itemView){
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.img1);
            getID = itemView.findViewById(R.id.getID);
            search = itemView.findViewById(R.id.search);
            mosqueName = (TextView) itemView.findViewById(R.id.mosqueName);
            namaaz1 = (TextView) itemView.findViewById(R.id.namaaz1);
            namaaz2 = (TextView) itemView.findViewById(R.id.namaaz2);
            namaaz3 = (TextView) itemView.findViewById(R.id.namaaz3);
            namaaz4 = (TextView) itemView.findViewById(R.id.namaaz4);
            namaaz5 = (TextView) itemView.findViewById(R.id.namaaz5);
            btnMap = (Button) itemView.findViewById(R.id.btnMap);
        }

    }
}

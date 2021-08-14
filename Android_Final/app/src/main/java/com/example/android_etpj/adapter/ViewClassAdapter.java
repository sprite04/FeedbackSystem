package com.example.android_etpj.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Trainee;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewClassAdapter extends RecyclerView.Adapter<ViewClassAdapter.ViewClassViewHolder> {
    private List<String> traineeIDList;
    private Activity activity;

    public void setData(List<String> list){
        this.traineeIDList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity=new Activity();
        activity=(Activity) parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common,parent,false);
        return new ViewClassAdapter.ViewClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewClassViewHolder holder, int position) {

        String traineeID = traineeIDList.get(position);
        if(traineeID==null)
            return;

        ApiService.apiService.getTraineeByUsername(traineeID).enqueue(new Callback<Trainee>() {
            @Override
            public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                Trainee trainee = (Trainee)response.body();
                Log.e("traineelist",String.valueOf(trainee.getUserId()));
                String displayText="";
                displayText="<b>" + "Number: " + "</b> " + position + 1 +"<br>"+
                        "<b>" + "Trainee ID: " + "</b> "+ trainee.getUserId() +"<br>"+
                        "<b>" + "Trainee Name: " + "</b> " + trainee.getName()+"<br>"
                ;
                holder.tvItem.setText(Html.fromHtml(displayText,1));
            }

            @Override
            public void onFailure(Call<Trainee> call, Throwable t) {
                Log.e("traineelist",t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(traineeIDList!=null)
            return traineeIDList.size();
        return 0;
    }

    public class ViewClassViewHolder extends RecyclerView.ViewHolder{
        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private ImageButton btnView;
        public ViewClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tv_content);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnDelete=itemView.findViewById(R.id.btn_delete);
            btnView=itemView.findViewById(R.id.btn_view);

            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnView.setVisibility(View.GONE);;
        }
    }
}

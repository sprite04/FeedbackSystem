package com.example.android_etpj.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.R;
import com.example.android_etpj.interfaces.ExchangeCommentResult;
import com.example.android_etpj.models.CommentResult;
import com.example.android_etpj.models.Trainee;
import com.example.android_etpj.models.Trainer;

import java.util.List;

public class CommentResultAdapter extends RecyclerView.Adapter<CommentResultAdapter.CommentViewHolder>{
    private List<CommentResult> commentResultList;
    private Activity activity;
    private ExchangeCommentResult exchange;
    private Object user;

    public CommentResultAdapter(ExchangeCommentResult exchange,Object user) {
        this.exchange = exchange;
        this.user=user;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity = new Activity();
        activity = (Activity) parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_comment, parent, false);
        return new CommentResultAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentResult commentResult = commentResultList.get(position);
        if (commentResult == null)
            return;

        //Xử lý cho admin
        String displayText = "";
        if(user instanceof Trainer || user instanceof Trainee) {
            displayText = "<b>" + "No: " + "</b> " + position+1 + "<br>"+
                    "<b>" + "Content: " + "</b> " + commentResult.getComment() + "<br>";
        }else{
            displayText = "<b>" + "No: " + "</b> " + position+1 + "<br>" +
                    "<b>" + "Trainee ID: " + "</b> " + commentResult.getTraineeId() + "<br>" +
                    "<b>" + "Content: " + "</b> " + commentResult.getComment() + "<br>";
        }
        holder.tvItem.setText(Html.fromHtml(displayText, 1));
    }

    @Override
    public int getItemCount() {
        if (commentResultList != null)
            return commentResultList.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_content);
        }
    }
    public void setData(List<CommentResult> commentResultsList) {
        this.commentResultList = commentResultsList;
        notifyDataSetChanged();
    }
}

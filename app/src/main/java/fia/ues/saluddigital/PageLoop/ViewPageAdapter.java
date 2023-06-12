package fia.ues.saluddigital.PageLoop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fia.ues.saluddigital.R;

public class ViewPageAdapter extends RecyclerView.Adapter<ViewPageAdapter.ViewPagerHolder> {
    public class ViewPagerHolder extends RecyclerView.ViewHolder{
        public ViewPagerHolder(View itemView){
            super(itemView);

        }

        ImageView imageView = itemView.findViewById(R.id.ivItemImg);
    }


    private List<Integer> image;

    public ViewPageAdapter(List<Integer> image) {

        this.image = image;
    }

    public List<Integer> getImage() {
        return image;
    }

    public void setImage(List<Integer> image) {
        this.image = image;
    }

    @NonNull
    @Override
    public ViewPageAdapter.ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPageAdapter.ViewPagerHolder holder, int position) {
        Integer curImage = image.get(position);
        holder.imageView.setImageResource(curImage);
    }

    @Override
    public int getItemCount() {
        return image.size();
    }
}

package lk.javainstitute.petbuddy.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.javainstitute.petbuddy.Domain.CategroyModel;
import lk.javainstitute.petbuddy.R;
import lk.javainstitute.petbuddy.databinding.ViewholderCategoryBinding;

public class CategroyAdapter extends RecyclerView.Adapter<CategroyAdapter.Viewholder> {
    private List<CategroyModel> items;  // List to hold the data

    public CategroyAdapter(List<CategroyModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);  // Return the ViewHolder with binding
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the item at the current position
        CategroyModel item = items.get(position);

        // Bind the title to the TextView
        holder.binding.titleCat.setText(item.getName());

        // Load the image using Glide (item.getPicture() should be a valid URL or resource ID)
        Glide.with(holder.itemView.getContext())
                .load(item.getPicture())  // Load the image URL or resource
                .into(holder.binding.picCat);  // Set the image into the ImageView

        // Set a random background color based on the position
        int[] backgrounds = {
                R.drawable.blue_rec_bg,
                R.drawable.green_rec_bg,
                R.drawable.purple_rec_bg,
                R.drawable.o_rec_bg,
        };
        int backgroundRes = backgrounds[position % 4];  // Cycle through the backgrounds
        holder.binding.getRoot().setBackgroundResource(backgroundRes);  // Set the background resource for the item
    }

    @Override
    public int getItemCount() {
        return items.size();  // Return the size of the list
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        public ViewholderCategoryBinding binding;  // Declare the ViewBinding

        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());  // Use the root view of the binding
            this.binding = binding;  // Initialize the binding
        }
    }
}

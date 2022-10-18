package layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.group7.R
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private val restaurantList : ArrayList<RestaurantDetail>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_restaurant,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = restaurantList[position]
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.tvHeading.text = currentItem.heading
        holder.tvHeading2.text = currentItem.heading2


    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){




        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)      
        val tvHeading : TextView = itemView.findViewById(R.id.tvHeading)
         val tvHeading2 : TextView = itemView.findViewById(R.id.tvHeading2)
    }


}
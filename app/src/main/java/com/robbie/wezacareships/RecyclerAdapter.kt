package com.robbie.wezacareships


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

//check below line connects to model
//creates a list of items
class RecyclerAdapter(var context: Context, var shipList: ArrayList<Model>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    private lateinit var inflater: LayoutInflater
    private var container: ViewGroup? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_shipid: TextView
        val txt_port: TextView
        val txt_shipName: TextView
        val imageView: ImageView
        val cardView: CardView
        init {
            txt_shipid = itemView.findViewById(R.id.txt_shipid)
            txt_shipName = itemView.findViewById(R.id.txt_ship_name)
            txt_port = itemView.findViewById(R.id.txt_port)
            imageView = itemView.findViewById(R.id.imageView)
            cardView = itemView.findViewById(R.id.cardview_2)
        }
    }

    //below lines link to single item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shipList[position] // take the first item is at 0
        //it automatically loops to other positions in news list

        holder.txt_shipid.text = item.ship_id
        holder.txt_shipName.text =  item.ship_name
        holder.txt_port.text = item.home_port




        //holder.itemView.txt_title.text = item.title
        //holder.itemView.imageView.setImageResource(item.image)
        Glide.with(context)
            .load(item.image) //image link
            .override(300,200)
            .into(holder.imageView)//place it in image view


        //make  click listener
        //when 1 item is clicked
        holder.cardView.setOnClickListener{
            Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show()
            //share prefferences
            val prefs:SharedPreferences =context.getSharedPreferences("ship",
                Context.MODE_PRIVATE)
            //put details of the clicked items,to prefferences
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString("ship_id", item.ship_id)
            editor.putString("ship_name",item.ship_name)
            editor.putString("year_built",item.year_built)
            editor.putString("ship_type",item.ship_type)
            editor.putString("weight_kg",item.weight_kg)
            editor.putString("home_port",item.home_port)

            editor.putString("image", item.image)
            editor.apply()//there now saved on the phone
            //go to single item
            bottomSheetStations()

        }//end
    }

    private fun bottomSheetStations() {

            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(context)

            // on below line we are inflating a layout file which we have created.

            inflater = LayoutInflater.from(context)

            val view = inflater.inflate(R.layout.single_bottomsheet, container, true)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "evansitoden94@gmail.com", null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }
            // on below line we are creating a variable for our button
            // which we are using to dismiss our dialog.
        val prefs: SharedPreferences = context.getSharedPreferences("ship",
            AppCompatActivity.MODE_PRIVATE
        )

        val toolbarLayout = view.findViewById(R.id.toolbar_layout) as CollapsingToolbarLayout
        val shipname =prefs.getString("ship_name","")
        toolbarLayout.title = shipname
        val pshipid = view.findViewById(R.id.tv_shipid) as TextView
        val shipId =prefs.getString("ship_id","")
        pshipid.text = shipId

        val pshipname = view.findViewById(R.id.tv_shipname) as TextView
        val shipName =prefs.getString("ship_name","")
        pshipname.text = shipName

        val pport = view.findViewById(R.id.tv_port) as TextView
        val home =prefs.getString("home_port","")
        pport.text = home
        val pyear = view.findViewById(R.id.tv_ship_type) as TextView
        val year =prefs.getString("year_built","")
        pyear.text = year
        val ptype = view.findViewById(R.id.tv_weight) as TextView
        val type =prefs.getString("ship_type","")
        ptype.text = type
        val pweight = view.findViewById(R.id.tv_year) as TextView
        val weight =prefs.getString("weight_kg","")
        pweight.text = weight

        val pimage = view.findViewById(R.id.pimage) as ImageView
        val image_url =prefs.getString("image","")

        Glide.with(context)
            .load(image_url)//image link
            .override(600,400)
            .into(pimage)//place it in image view

        val btnClose = view.findViewById(R.id.idBtnDismiss) as Button

        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
        }


    //
    override fun getItemCount() = shipList.size

    fun getFilter(): Filter {
        return object : Filter() {
             override fun performFiltering(charSequence: CharSequence): FilterResults {
                val searchString = charSequence.toString()
                if (searchString.isEmpty()) {
                } else {
                    val tempFilteredList: ArrayList<Model> = ArrayList()
                    for (ship in  shipList) {

                        // search for user title
                        if (ship.ship_name.lowercase(Locale.getDefault()).contains(searchString)) {
                            tempFilteredList.add(ship)
                        }
                    }
                    shipList = tempFilteredList
                }
                val filterResults = FilterResults()
                filterResults.values =  shipList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults,
            ) {
                shipList = filterResults.values as ArrayList<Model>
               notifyDataSetChanged()
            }
        }
    }

}
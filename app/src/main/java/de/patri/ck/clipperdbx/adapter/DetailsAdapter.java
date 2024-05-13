package de.patri.ck.clipperdbx.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import de.patri.ck.clipperdbx.R;
import de.patri.ck.clipperdbx.app.Config;
import de.patri.ck.clipperdbx.common.model.Clipper;
import de.patri.ck.clipperdbx.common.utils.LruBitmapCache;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  ArrayList<Clipper> itemList;
  Context context;

  public DetailsAdapter(Context context, ArrayList<Clipper> itemList) {
    this.context = context;
    this.itemList = itemList;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ClipperHolder vHolder = null;
    View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail, parent, false);
    vHolder = new ClipperHolder(layoutView);
    return vHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
    ((ClipperHolder) holder).setName.setTypeface(font);
    ((ClipperHolder) holder).setName.setText(itemList.get(position).getName());

    final String own = itemList.get(position).getOwn();
    if(Objects.equals(own, "1")) {
      ((ClipperHolder) holder).setName.setTextColor(ContextCompat.getColor(context, R.color.gruen));
    }  else if(Objects.equals(own, "0")) {
      ((ClipperHolder) holder).setName.setTextColor(ContextCompat.getColor(context, R.color.rot));
    }
    String img_url = Config.IMG_URL+"/items/"+itemList.get(position).getImage()+".png";
    ImageLoader.ImageCache img_cache = new LruBitmapCache();
    ImageLoader imgLoader = new ImageLoader(newRequestQueue(context), img_cache);
    imgLoader.get(img_url, ImageLoader.getImageListener(((ClipperHolder) holder).setImage, R.drawable.load, R.drawable.clipperdb_blank_item));
    ((ClipperHolder) holder).setImage.setImageUrl(img_url, imgLoader);
    ((ClipperHolder) holder).setDatum.setText(itemList.get(position).getDatum());
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }

  @Override
  public int getItemViewType(int position) {
    return itemList.get(position) != null ? 1 : 0;
  }

  public void clearData() {
    if(itemList != null) {
      itemList.clear();
      this.notifyDataSetChanged();
    }
  }

  public static class ClipperHolder extends RecyclerView.ViewHolder {

    TextView setName, setDatum;
    NetworkImageView setImage;

    public ClipperHolder(View itemView) {

      super(itemView);
      setName  = itemView.findViewById(R.id.name);
      setImage = itemView.findViewById(R.id.image);
      setDatum = itemView.findViewById(R.id.datum);

      itemView.setTag(this);
    }
  }

  private static final int DISK_USAGE_BYTES = Config.CACHE_SiZE;

  private static RequestQueue newRequestQueue(Context context) {

    File rootCache = context.getExternalCacheDir();
    if(rootCache == null) {
      rootCache = context.getCacheDir();
    }
    File cacheDir = new File(rootCache, "img_cache");
    cacheDir.mkdirs();

    HttpStack stack = new HurlStack();
    Network network = new BasicNetwork(stack);
    DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DISK_USAGE_BYTES);
    RequestQueue queue = new RequestQueue(diskBasedCache, network);
    queue.start();

    return queue;
  }

}
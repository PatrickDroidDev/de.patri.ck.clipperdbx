package de.patri.ck.clipperdbx.fragmente;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.patri.ck.clipperdbx.R;
import de.patri.ck.clipperdbx.adapter.ClipperAdapter;
import de.patri.ck.clipperdbx.app.Config;
import de.patri.ck.clipperdbx.common.model.Clipper;
import de.patri.ck.clipperdbx.common.utils.RecyclerSpace;
import de.patri.ck.clipperdbx.common.utils.pAppUtils;

public class ClipperSet extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  public final String TAG = Config.TITLE_SETS;

  ArrayList<Clipper> itemList;

  RecyclerView rv;
  ClipperAdapter clprAdapter;

  int numSpace  = 2;
  int numColumn = 2;

  RequestQueue reqQueue;
  LinearLayoutManager layoutManager;
  SwipeRefreshLayout swipeLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View content = inflater.inflate(R.layout.fragment_clipper_set, container, false);

    swipeLayout  = content.findViewById(R.id.swipeLayout);
    swipeLayout.setColorScheme(R.color.pink, R.color.pink_translucent);
    swipeLayout.setOnRefreshListener(this);

    rv = content.findViewById(R.id.rv);
    rv.setHasFixedSize(true);

    Cache cache     = new DiskBasedCache(getContext().getCacheDir(), Config.CACHE_SiZE);
    Network network = new BasicNetwork(new HurlStack());
    reqQueue        = new RequestQueue(cache, network);
    reqQueue.start();

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Config.API_URL+"/clipper/sets/", null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        rv.setVisibility(View.VISIBLE);
        try {
          Gson gson = new Gson();
          JSONArray jArray = response.getJSONArray("clipper");
          for(int p=0; p<jArray.length(); p++) {
            JSONObject jsonObject = jArray.getJSONObject(p);
            Clipper rvdata = gson.fromJson(String.valueOf(jsonObject), Clipper.class);
            itemList.add(rvdata);
          }
        } catch(JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        pAppUtils.snack(getContext(), getView(), error.toString());
      }
    });

    itemList = new ArrayList<Clipper>();
    clprAdapter = new ClipperAdapter(getContext(), itemList);
    rv.setAdapter(clprAdapter);

    layoutManager = new GridLayoutManager(getContext(), numColumn);
    rv.setLayoutManager(layoutManager);
    rv.addItemDecoration(new RecyclerSpace(numSpace, numColumn));

    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
    rv.setLayoutAnimation(controller);
    rv.scheduleLayoutAnimation();

    jsonObjectRequest.setTag(TAG);
    reqQueue.add(jsonObjectRequest);
    clprAdapter.notifyDataSetChanged();

    return content;
  }

  @Override
  public void onRefresh() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        clprAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
      }
    }, 2500);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    clprAdapter.clearData();
  }

}
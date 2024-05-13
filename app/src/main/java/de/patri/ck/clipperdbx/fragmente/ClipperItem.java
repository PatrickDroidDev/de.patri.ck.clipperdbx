package de.patri.ck.clipperdbx.fragmente;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.os.Handler;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.Network;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import com.android.volley.Request;
import org.json.JSONException;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.view.animation.LayoutAnimationController;
import android.view.animation.AnimationUtils;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import de.patri.ck.clipperdbx.Main;
import de.patri.ck.clipperdbx.R;
import de.patri.ck.clipperdbx.adapter.ClipperItemAdapter;
import de.patri.ck.clipperdbx.app.Config;
import de.patri.ck.clipperdbx.app.pApp;
import de.patri.ck.clipperdbx.common.model.Clipper;
import de.patri.ck.clipperdbx.common.utils.RecyclerSpace;
import de.patri.ck.clipperdbx.common.utils.pAppUtils;

public class ClipperItem extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  public final String TAG = Config.TITLE_iTEMS;

  ArrayList<Clipper> itemList;

  RecyclerView rv;
  ClipperItemAdapter clprAdapter;
  LinearLayoutManager layoutManager;

  int numSpace  = 2;
  int numColumn = 6;

  RequestQueue reqQueue;
  SwipeRefreshLayout swipeLayout;
  String clpr_type;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	((Main) getContext()).setSubTitle(Config.TITLE_iTEMS);
	View content = inflater.inflate(R.layout.fragment_clipper_item, container, false);

	swipeLayout = content.findViewById(R.id.swipeLayout);
	swipeLayout.setColorScheme(R.color.pink, R.color.gruen);
	swipeLayout.setOnRefreshListener(this);

	rv = content.findViewById(R.id.rv);
	rv.setHasFixedSize(true);

	Cache cache     = new DiskBasedCache(getContext().getExternalCacheDir());
	Network network = new BasicNetwork(new HurlStack());
	reqQueue        = new RequestQueue(cache, network);
	reqQueue.start();

	JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Config.API_URL+"/clipper/items/", null, new Response.Listener<JSONObject>() {
  	@Override
	  public void onResponse(JSONObject response) {
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
	clprAdapter = new ClipperItemAdapter(getContext(), itemList);
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
				pAppUtils.snack(getContext(), getView(), "Items aktualisiert!");
			}
		}, 2500);
	}

}
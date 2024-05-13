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
import de.patri.ck.clipperdbx.common.model.Clipper;
import de.patri.ck.clipperdbx.common.utils.RecyclerSpace;
import de.patri.ck.clipperdbx.common.utils.pAppUtils;

public class Start extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  private static final String TAG = Config.TITLE_START;

  ArrayList<Clipper> itemList;
  public final String ITEMS = "ITEMS";

  RecyclerView rv;
  ClipperItemAdapter clprAdapter;
  LinearLayoutManager layoutManager;

  int numSpace  = 2;
  int numColumn = 6;

  RequestQueue reqQueue;
  SwipeRefreshLayout swipeLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	((Main) getContext()).setSubTitle(Config.TITLE_START);
	View content = getLayoutInflater().inflate(R.layout.fragment_start, container, false);

	swipeLayout = content.findViewById(R.id.swipeLayout);
	swipeLayout.setColorScheme(R.color.pink, R.color.pink_translucent);
	swipeLayout.setOnRefreshListener(this);

	rv = content.findViewById(R.id.rv);
	rv.setHasFixedSize(true);

	itemList = new ArrayList<Clipper>();
	layoutManager = new GridLayoutManager(getContext(), numColumn);

	rv.setLayoutManager(layoutManager);
	rv.addItemDecoration(new RecyclerSpace(numSpace, numColumn));

    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
    rv.setLayoutAnimation(controller);
	rv.scheduleLayoutAnimation();

    clprAdapter = new ClipperItemAdapter(getContext(), itemList);
	rv.setAdapter(clprAdapter);

	Cache cache     = new DiskBasedCache(getContext().getExternalCacheDir());
	Network network = new BasicNetwork(new HurlStack());
    reqQueue        = new RequestQueue(cache, network);
	reqQueue.start();

	String JSON_URL = Config.API_URL + "/clipper/last/104";

	JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
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

  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
	super.onViewStateRestored(savedInstanceState);
	if(savedInstanceState != null) {

	}
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	outState.putParcelableArrayList(ITEMS, outState.getParcelableArrayList(ITEMS));
  }

  @Override
  public void onDestroy() {
	super.onDestroy();
	clprAdapter.clearData();
  }

}

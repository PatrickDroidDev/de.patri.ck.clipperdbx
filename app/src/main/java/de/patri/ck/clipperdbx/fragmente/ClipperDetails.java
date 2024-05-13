package de.patri.ck.clipperdbx.fragmente;

import com.android.volley.RequestQueue;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.Response;
import org.json.JSONObject;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import com.android.volley.VolleyError;
import android.view.animation.LayoutAnimationController;
import android.view.animation.AnimationUtils;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import de.patri.ck.clipperdbx.Main;
import de.patri.ck.clipperdbx.R;
import de.patri.ck.clipperdbx.adapter.DetailsAdapter;
import de.patri.ck.clipperdbx.app.Config;
import de.patri.ck.clipperdbx.common.model.Clipper;
import de.patri.ck.clipperdbx.common.utils.RecyclerSpace;
import de.patri.ck.clipperdbx.common.utils.pAppUtils;

public class ClipperDetails extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	private static final String TAG = Config.TITLE_DETAiL;

	ArrayList<Clipper> itemList;

	RecyclerView rv;
	DetailsAdapter clprAdapter;
	LinearLayoutManager layoutManager;

	int numSpace  = 3;
	int numColumn = 2;

	RequestQueue reqQueue;
	SwipeRefreshLayout swipeLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		((Main) getContext()).setSubTitle(Config.TITLE_DETAiL);
		View content = inflater.inflate(R.layout.fragment_details, container, false);

		swipeLayout = content.findViewById(R.id.swipeLayout);
		swipeLayout.setColorScheme(R.color.pink, R.color.pink_translucent);
		swipeLayout.setOnRefreshListener(this);

		rv = content.findViewById(R.id.rv);
		rv.setHasFixedSize(true);

		itemList = new ArrayList<>();
		layoutManager = new GridLayoutManager(getContext(), numColumn);

		rv.setLayoutManager(layoutManager);
		rv.addItemDecoration(new RecyclerSpace(numSpace, numColumn));

		final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
		rv.setLayoutAnimation(controller);
		rv.scheduleLayoutAnimation();

		clprAdapter = new DetailsAdapter(getContext(), itemList);
		rv.setAdapter(clprAdapter);

		String JSON_URL = Config.API_URL +"/clipper/"+ getArguments().getString("clpr_type") +"/"+ getArguments().getInt("_id");

		Cache cache     = new DiskBasedCache(getContext().getExternalCacheDir());
		Network network = new BasicNetwork(new HurlStack());
		reqQueue        = new RequestQueue(cache, network);
		reqQueue.start();

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

}
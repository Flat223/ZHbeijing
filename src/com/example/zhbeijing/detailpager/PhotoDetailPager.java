package com.example.zhbeijing.detailpager;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhbeijing.R;
import com.example.zhbeijing.domain.PhotosBean;
import com.example.zhbeijing.domain.PhotosBean.PhotosNewsData;
import com.example.zhbeijing.global.GlobalConstant;
import com.example.zhbeijing.utils.CacheUtils;
import com.example.zhbeijing.utils.PhotoBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PhotoDetailPager extends BaseDetailPager implements OnClickListener {

	private ListView lv_photo;
	private GridView gv_photo;
	private TextView tv_title;
	private ImageButton iv_exchange;	
	private ArrayList<PhotosNewsData> mPhotoArray;
	private String photoUrl;
	private boolean mIsListView = true;

	public PhotoDetailPager(final Activity mActivity, ImageButton iv_exchange) {
		super(mActivity);
		
		this.iv_exchange = iv_exchange;
		iv_exchange.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if (mIsListView) {
			gv_photo.setVisibility(View.VISIBLE);
			lv_photo.setVisibility(View.GONE);
			mIsListView = false;
			iv_exchange.setImageResource(R.drawable.icon_pic_list_type);
			Log.e("TAG", "mIsListView" + " = " + mIsListView);
		} else {
			gv_photo.setVisibility(View.GONE);
			lv_photo.setVisibility(View.VISIBLE);
			mIsListView = true;
			iv_exchange.setImageResource(R.drawable.icon_pic_grid_type);
			Log.e("TAG", "mIsListView" + " = " + mIsListView);
		}
	}
	
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_photo_detail, null);

		lv_photo = (ListView) view.findViewById(R.id.lv_photo_detail);
		gv_photo = (GridView) view.findViewById(R.id.gv_photo_detail);

		//此时的iv_exchange还未被初始,不能进行事件操作
//		iv_exchange.setVisibility(View.VISIBLE);
		
		return view;
	}

	@Override
	public void initData() {

		photoUrl = GlobalConstant.PHOTO_URL;
		String cache = CacheUtils.getCache(mActivity, photoUrl);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, photoUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				CacheUtils.setCache(mActivity, photoUrl, result);
				processData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}
		});
	}

	protected void processData(String json) {
		Gson gson = new Gson();
		PhotosBean mPhotosBean = gson.fromJson(json, PhotosBean.class);

		mPhotoArray = mPhotosBean.data.news;

		lv_photo.setAdapter(new PhotoAdapter());
		gv_photo.setAdapter(new PhotoAdapter());
	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public PhotoAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
		}

		@Override
		public int getCount() {
			return mPhotoArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mPhotoArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.item_photo_detail, null);
				holder = new ViewHolder();
				holder.iv_image = (ImageView) convertView
						.findViewById(R.id.iv_image_item);
				holder.tv_desc = (TextView) convertView
						.findViewById(R.id.tv_photo_desc);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PhotosNewsData photosNewsData = mPhotoArray.get(position);

			holder.tv_desc.setText(photosNewsData.title);

			String imageUrl = photosNewsData.listimage;
			String newUrl = imageUrl.replace("10.0.2.2", "192.168.2.102");
			mBitmapUtils.display(holder.iv_image, newUrl);

			return convertView;
		}
	}

	class ViewHolder {
		private ImageView iv_image;
		private TextView tv_desc;
	}
}

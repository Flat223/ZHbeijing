package com.example.zhbeijing.fragment;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhbeijing.MainActivity;
import com.example.zhbeijing.R;
import com.example.zhbeijing.domain.NewsMenu.NewsMenuData;
import com.example.zhbeijing.pager.NewsCenterPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LeftMenuFragment extends BaseFragment {

	private ArrayList<NewsMenuData> mDataArray;
	
	@ViewInject(R.id.lv_left_menu)
	private ListView mListView;
	private TextView tv_menu;
	private leftMenuAdapter adapter;
	private int mCurrentPos;
	
	@Override
	public void initData() {
		
	}
	
	@Override
	public View initView() {
		
		View view = View.inflate(mActivity, R.layout.fragment_leftmenu,null);
//		lv_left_menu =  (ListView) view.findViewById(R.id.lv_left_menu);
		
		ViewUtils.inject(this, view);
		
		Log.d("TAG", "this : " + this);
		
		return view;
	}

	public void setMenuData(ArrayList<NewsMenuData> datas){
		mCurrentPos = 0;
		
		mDataArray = datas;
		
		adapter = new leftMenuAdapter();
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mCurrentPos = position;
				adapter.notifyDataSetChanged();
				
				setMenuToggle();
				
				setCurrentMenuDetailPager(position);
			}
		});
	}
	
	protected void setCurrentMenuDetailPager(int position) {
		MainActivity mMainUi = (MainActivity) mActivity;
		ContentFragment fragment = mMainUi.getContentFragment();
		NewsCenterPager pager = fragment.getNewsCenterPager();
		pager.setDetailPager(position);
	}

	protected void setMenuToggle() {
		MainActivity mMainUi =  (MainActivity) mActivity;
		mMainUi.getSlidingMenu().toggle();
	}

	class leftMenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mDataArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mDataArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_leftmenu, null);	
			}
			
			tv_menu = (TextView) convertView.findViewById(R.id.tv_item_left);
			NewsMenuData item = mDataArray.get(position);
			tv_menu.setText(item.title);
			
			if (position == mCurrentPos) {
				tv_menu.setEnabled(true);
			} else {
				tv_menu.setEnabled(false);
			}
			
			return convertView;
		}
		
	}
}

package com.example.zhbeijing.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhbeijing.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

@SuppressLint("SimpleDateFormat")
public class PullToRefreshListView extends ListView implements OnScrollListener{

	@ViewInject(R.id.iv_refresh_row)
	private ImageView iv_row;

	@ViewInject(R.id.pb_refresh_loading)
	private ProgressBar pb_loading;

	@ViewInject(R.id.tv_refresh_hint)
	private TextView tv_hint;

	@ViewInject(R.id.tv_refresh_time)
	private TextView tv_time;

	private int measuredHeight;
	
	private View headView;
	private int startY = -1;

	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;
	private int mCurrentState = -1;

	private RotateAnimation animationDown;

	private RotateAnimation animationUp;

	private int paddingY;

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeadView();
		initFootView();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		initHeadView();
		initFootView();
	}

	private void initHeadView() {
		headView = View.inflate(getContext(), R.layout.lv_pull_refresh, null);
		ViewUtils.inject(this, headView);

		this.addHeaderView(headView);
		headView.measure(0, 0);
		measuredHeight = headView.getMeasuredHeight();
		headView.setPadding(0, -measuredHeight, 0, 0);

		showAnimation();
		setcurrentTime();
		setOnScrollListener(this);
	}
	
	private void initFootView() {
		footerView = View.inflate(getContext(), R.layout.list_item_footer, null);
		ViewUtils.inject(this, footerView);
		
		addFooterView(footerView);
		footerView.measure(0, 0);
		footerHeight = footerView.getMeasuredHeight();
		
		footerView.setPadding(0, -footerHeight, 0, 0);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {
				startY = (int) ev.getY();
			}
			
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getY();
			int dy = endY - startY;
			int position = getFirstVisiblePosition();

			if (dy > 0 && position == 0) {
				paddingY = dy - measuredHeight;
				headView.setPadding(0, paddingY, 0, 0);
				if (paddingY <= 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				} else if (paddingY > 0
						&& mCurrentState != STATE_RELEASE_TO_REFRESH) {
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				}
				
				return true;
			}

			break;

		case MotionEvent.ACTION_UP:

			if (mCurrentState == STATE_REFRESHING) {
				break;
			}
			
			int paddingTop = headView.getPaddingTop();
			if (mCurrentState == STATE_PULL_TO_REFRESH) {
				headView.setPadding(0, -measuredHeight, 0, 0);
				refreshState();
			} else if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				
				headView.setPadding(0, 0, 0, 0);
				refreshState();
				if (onRefreshListenner != null) {
					onRefreshListenner.onRefresh();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tv_hint.setText("下拉刷新");
			iv_row.setVisibility(View.VISIBLE);
			pb_loading.setVisibility(View.INVISIBLE);
			iv_row.startAnimation(animationUp);

			break;
		case STATE_RELEASE_TO_REFRESH:
			tv_hint.setText("松开刷新");
			iv_row.startAnimation(animationDown);
			break;
		case STATE_REFRESHING:
			tv_hint.setText("正在刷新");
			iv_row.clearAnimation();
			iv_row.setVisibility(View.INVISIBLE);
			pb_loading.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	public void onRefreshComplete(boolean success) {
		if (isLoadMore) {
			footerView.setPadding(0, -footerHeight, 0, 0);
			isLoadMore = false;
		}
		
		headView.setPadding(0, -measuredHeight, 0, 0);
		tv_hint.setText("下拉刷新");
		iv_row.setVisibility(View.VISIBLE);
		pb_loading.setVisibility(View.INVISIBLE);
		mCurrentState = STATE_PULL_TO_REFRESH;
		
		if (success) {
			setcurrentTime();
		}
	}

	private void setcurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		tv_time.setText(time);
	}

	private void showAnimation() {

		animationDown = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationDown.setFillAfter(true);
		animationDown.setDuration(200);

		animationUp = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationDown.setFillAfter(true);
		animationUp.setDuration(200);
	}
	
	private OnRefreshListenner onRefreshListenner;

	private View footerView;

	public void setOnRefreshListenner(OnRefreshListenner onRefreshListenner){
		this.onRefreshListenner = onRefreshListenner;
	}
	
	public interface OnRefreshListenner{
		void onRefresh();
		void onLoadMore();
	}
	
	private boolean isLoadMore = false;

	private int footerHeight;
	
	//上下滑动监听
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int count = getCount();
		
		if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() == (count - 1)) {
			if (!isLoadMore) {
				if (onRefreshListenner != null) {
					
					setSelection(count - 1);
					isLoadMore = true;
					footerView.setPadding(0, 0, 0, 0);
					onRefreshListenner.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
}

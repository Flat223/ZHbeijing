package com.example.zhbeijing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsDetailActivity extends Activity implements OnClickListener {
	
	@ViewInject(R.id.web_news)
	private WebView mWebView;
	
	@ViewInject(R.id.ibn_news_back)
	private ImageButton ibn_back;
	
	@ViewInject(R.id.ibn_news_share)
	private ImageButton ibn_share;
	
	@ViewInject(R.id.ibn_news_textSize)
	private ImageButton ibn_textSize;

	private WebSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		
		ViewUtils.inject(this);
		
		settings = mWebView.getSettings();
		
		settings.setBuiltInZoomControls(true);//显示缩放按钮
		settings.setUseWideViewPort(true);//双击缩放
		settings.setJavaScriptEnabled(true);//支持Jsp功能
		
		String mWebUrl = getIntent().getStringExtra("WebUrl");
		newUrl = mWebUrl.replace("10.0.2.2", "192.168.2.102");
		mWebView.loadUrl(newUrl);
		
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
//		mWebView.goBack();
//		mWebView.goForward();
		
		ibn_back.setOnClickListener(this);
		ibn_share.setOnClickListener(this);
		ibn_textSize.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		
		case R.id.ibn_news_back:
			finish();
			break;
			
		case R.id.ibn_news_share:
			showShare();
			break;
			
		case R.id.ibn_news_textSize:
			showChooseDialog();
			break;

		default:
			break;
		}
	}

	private int mTempCount;
	private int mCurrentCount = 2;

	private String newUrl;
	private void showChooseDialog() {
		String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
		new AlertDialog.Builder(this)
			.setTitle("字体设置")
			
			.setSingleChoiceItems(items, mCurrentCount, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mTempCount = which;
				}
			})
			
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mCurrentCount = mTempCount;
					switch (mCurrentCount) {
					case 0:
						settings.setTextSize(TextSize.LARGEST);
						break;
						
					case 1:
						settings.setTextSize(TextSize.LARGER);
						break;
						
					case 2:
						settings.setTextSize(TextSize.NORMAL);
						break;
						
					case 3:
						settings.setTextSize(TextSize.SMALLER);
						break;
						
					case 4:
						settings.setTextSize(TextSize.SMALLEST);
						break;

					default:
						break;
					}
				}
			})
			
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			
			.show();
	}
	

	// 确保SDcard下面存在此张图片test.jpg
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		
		oks.setTheme(OnekeyShareTheme.SKYBLUE);//修改主题样式
		
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(newUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(newUrl);

		// 启动分享GUI
		oks.show(this);
	}
}

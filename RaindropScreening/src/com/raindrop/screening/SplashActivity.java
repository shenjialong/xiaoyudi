package com.raindrop.screening;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raindrop.customview.NavigationBar;
import com.raindrop.util.Contants;
import com.raindrop.util.SystemExitBroadcastReceiver;

public class SplashActivity extends Activity {

	NavigationBar myNavigationbar;
	ListView questionTypeLV;
	SystemExitBroadcastReceiver exitReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		init();
	}
	
	public void init(){
		exitReceiver=new SystemExitBroadcastReceiver();
		initNavigationbar();
		initQuestionTypeListview();
	}
	
	
	public void initNavigationbar(){
		myNavigationbar=(NavigationBar)findViewById(R.id.helpNb);
		myNavigationbar.setBtnRightVisble(false);
		myNavigationbar.setBtnLeftVisble(false);
		myNavigationbar.setTvTitle("请选择题库");
	}
	@Override
	public void onResume(){
		super.onResume();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Contants.EXIT);
		Log.i("sjl", "Splash页面注册 系统级监听器..");
		this.registerReceiver(exitReceiver, filter);
	}
	
	public void initQuestionTypeListview(){
		String [] adapterSource=new String[]{"题库A(适合0到16个月儿童)","题库B(适合16到30个月儿童)","题库C(适合2岁以上儿童)"};
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,adapterSource);
		questionTypeLV=(ListView)findViewById(R.id.questionTypeLV);
		questionTypeLV.setAdapter(adapter);
		questionTypeLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent =new Intent(SplashActivity.this, MainActivity.class);
				intent.putExtra("questionType", arg2);
				startActivity(intent);
//				SplashActivity.this.finish();
				overridePendingTransition(R.anim.fadein, R.anim.fadeout); 
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_help, menu);
		return true;
	}

}

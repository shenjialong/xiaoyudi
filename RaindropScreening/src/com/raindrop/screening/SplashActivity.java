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
		myNavigationbar.setTvTitle("��ѡ�����");
	}
	@Override
	public void onResume(){
		super.onResume();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Contants.EXIT);
		Log.i("sjl", "Splashҳ��ע�� ϵͳ��������..");
		this.registerReceiver(exitReceiver, filter);
	}
	
	public void initQuestionTypeListview(){
		String [] adapterSource=new String[]{"���A(�ʺ�0��16���¶�ͯ)","���B(�ʺ�16��30���¶�ͯ)","���C(�ʺ�2�����϶�ͯ)"};
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

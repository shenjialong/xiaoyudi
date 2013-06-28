package com.raindrop.screening;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.raindrop.customview.NavigationBar;
import com.raindrop.util.Contants;
import com.raindrop.util.HttpUtil;
import com.umeng.fb.FeedbackAgent;

public class MainActivity extends Activity implements OnClickListener{

	
     ImageButton imageBtnYes;
     ImageButton imageBtnNo;
     LinearLayout displayLL;
     LinearLayout displayPartLL;
     NavigationBar navigationBar;
     ImageView questionIv; 
     TextView questionTv;
     Map <Integer,String> questions;
     Map <String,Integer> answers;
     ProgressBar progressbar;
     String [] questionsArray;
     int questionLength;
     int questionType;
     int currentNum;
     FeedbackAgent agent;
     
	 Handler myhandler=new Handler(){
		 @Override
		 public void handleMessage(Message msg) {
			      super.handleMessage(msg);
			      String jsonstring =msg.obj.toString();
//			      questionTv.setText(jsonstring);
	      }
	 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	private void init(){
		initUI();
		setNavigationBar();
		setImageButton();
		initQuestions();
	}
	
	private void initUI(){
		 imageBtnYes=(ImageButton)findViewById(R.id.imageBtnYes);
	     imageBtnNo=(ImageButton)findViewById(R.id.imageBtnNo);
	     displayLL=(LinearLayout)findViewById(R.id.displayLL);
	     questionTv=(TextView)findViewById(R.id.questionTv);
	     progressbar=(ProgressBar)findViewById(R.id.progressBar1);
	     displayPartLL=(LinearLayout)findViewById(R.id.displayPartLL);
	     questionIv=(ImageView)findViewById(R.id.questionIv);
	     answers=new HashMap<String, Integer>();
	     agent = new FeedbackAgent(MainActivity.this);
	     agent.sync();
	}
	
	
	private void setImageButton(){
		imageBtnYes.setOnClickListener(this);
		imageBtnNo.setOnClickListener(this);
	}
	
	private void setNavigationBar(){
		navigationBar=(NavigationBar)findViewById(R.id.navigationBar1);
		navigationBar.setTvTitle("M-CHAT表");
		navigationBar.setBtnLeftVisble(false);
		navigationBar.setBtnRightText("操作");
		navigationBar.setBtnRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new Builder(MainActivity.this);
				builder.setTitle("操作").setItems(R.array.operations, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
							case 0:
//								重新开始答题
								currentNum=0;
								setQuestionContent();
								Intent intent =new Intent(MainActivity.this, SplashActivity.class);
								startActivity(intent);
								finish();
								overridePendingTransition(R.anim.fadein, R.anim.fadeout); 
								Toast.makeText(MainActivity.this, "题库初始化完成，请开始答题", Toast.LENGTH_SHORT).show();
								break; 
							case 1:
							    agent.startFeedbackActivity();
								break; 
							case 2:
//								退出应用
//								广播通知所有Activity 关闭所有活动Activity
								Intent exitIntent=new  Intent();
								exitIntent.setAction(Contants.EXIT);
								Log.i("sjl", "begin sendBroadcast。。");
								MainActivity.this.sendBroadcast(exitIntent);
								
								finish();   
								overridePendingTransition(R.anim.fadein, R.anim.fadeout); 
//								不能用下面这句 销毁资源  否则 后台线程关闭  导致不能广播到监听者 by sjl 20130627
//								System.exit(0);
								break;
						}
					}
				}).setPositiveButton("取消", null).show();
				
			}
		});
	}
	
	private void initQuestions(){
		Intent intent=this.getIntent();
		questionType=intent.getIntExtra("questionType", 1);
		Resources res=this.getResources();
		
		switch (questionType) {
			case 0:
				questionsArray=res.getStringArray(R.array.questionType1);
				break;
			case 1:
				questionsArray=res.getStringArray(R.array.questionType2);
				break;
			case 2:
				questionsArray=res.getStringArray(R.array.questionType3);
				break;
			default:
				break;
		};
		questionLength=questionsArray.length;
		currentNum=0;
		progressbar.setMax(questionLength);
		progressbar.setProgress(1);
		setQuestionContent();
	}
	
	private void setQuestionContent(){
		questionTv.setText(Html.fromHtml("<b>"+questionsArray[currentNum]+"</b>"));
		String filed="question"+currentNum;
		Class<?> c;
		Field f;
		try {
			c=Class.forName("com.raindrop.screening.R"+"$"+"drawable");
			f = c.getField(filed);
			questionIv.setImageResource(Integer.valueOf(f.get(c.newInstance()).toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		int key=currentNum+1;
		if(v.getId()==R.id.imageBtnYes){
				answers.put(key+"", 1);
		}else if(v.getId()==R.id.imageBtnNo){
				answers.put(key+"", 0);
		}
		currentNum++;
		if(currentNum<questionLength){
			setQuestionContent();
			progressbar.setProgress(currentNum+1);
		}else{
			commitAnswers();
		}
		startAlphaAnimation(displayPartLL);
	}
	public JSONObject formatRequestParams(){
		JSONObject jsonparams=new JSONObject();
		try {
		for(int i=0;i<answers.size();i++){
				jsonparams.put("question"+(i+1), answers.get((i+1)+""));
		}
			jsonparams.put("userid", "china");
			jsonparams.put("terminal", "android");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonparams;
	}
	JSONObject json;
	public void commitAnswers(){
		Toast.makeText(this, "提交至百度云服务器，请等待..", Toast.LENGTH_SHORT).show();
		json=formatRequestParams();
		Thread mythread=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String,String> requestParams=new HashMap<String, String>();
					requestParams.put("answer", json.toString());
					String jsonstring=HttpUtil.postRequest("datacommit", requestParams);
					Message me=new Message();
					Log.i("sjl", "服务器返回值："+jsonstring);
					me.obj=jsonstring;
					myhandler.sendMessage(me);
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
			});
//		向服务器提交数据
		mythread.start();
		handleResult();
	}
	public void  handleResult(){
//		根据不同的题库 进行不同的逻辑判断
		int register=0;
		Resources rs=this.getResources();
		String [] answerStandard=rs.getStringArray(R.array.answers);
		int dangerIndexNum=0;
		boolean result=true; 
		switch(questionType){
			case 0:
//				由于相关的题库尚未搜集好，该类型处理逻辑暂且搁置
				break; 
			case 1:
//				测试结果：阳性。说明 ：该量表用于筛查 16~30月龄 儿 童 ,共 23 项 由 家 长 填 写 。 单 项 阳 性 判 别 标 准 :答 案选项为“是”或“否”,第 1、18、20、2项选“是”, 其余各项选“否”时即判断该项为阳性;总阳性判别 标准:总共 23项中≥3项阳性或 6项核心项目中 (核 心 项 目 为 第 2、7、9、13、14、15 项 )≥ 2 项 阳 性 为 孤独症高风险,需进一步电话随访,仍未通过者则需 进一步评估。
//				16到30个月情况
				for(int i=0;i<questionsArray.length;i++){
					if(answerStandard.equals(answers.get(i)));
					{
						register++;
						if(i==2||i==7||i==9||i==13||i==14||i==15){
							dangerIndexNum++;
						}
						if(dangerIndexNum>=2){
							result=false;
							break;
						}
					}
					if(register>=3){
						result=false;
						break;
					}
				}
				break; 
			case 2:
//				同0，由于相关的题库尚未搜集好，该类型处理逻辑暂且搁置
				break; 
			default:
				break; 
		}
		if(!result){
			questionTv.setText(Html.fromHtml("测试结果：阳性 <br/> 孤独症高风险,请咨询相关工作人员<br/>需进一步电话随访,仍未通过者则需进一步评估"));
		}else{
			questionTv.setText("测试结果：非阳性");
		}
	}
	
	public void startAlphaAnimation(View view){
		AlphaAnimation aa=new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
	}
	@Override  
	public boolean onKeyUp(int keyCode, KeyEvent event)   
	{   
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {   
//			实现键盘上返回键操作时的画面渐变效果
	    	finish();   
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);   
			return true;   
	    }   
	    return super.onKeyUp(keyCode, event);   
	} 
	
	
}

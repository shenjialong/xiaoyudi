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
		navigationBar.setTvTitle("M-CHAT��");
		navigationBar.setBtnLeftVisble(false);
		navigationBar.setBtnRightText("����");
		navigationBar.setBtnRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new Builder(MainActivity.this);
				builder.setTitle("����").setItems(R.array.operations, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
							case 0:
//								���¿�ʼ����
								currentNum=0;
								setQuestionContent();
								Intent intent =new Intent(MainActivity.this, SplashActivity.class);
								startActivity(intent);
								finish();
								overridePendingTransition(R.anim.fadein, R.anim.fadeout); 
								Toast.makeText(MainActivity.this, "����ʼ����ɣ��뿪ʼ����", Toast.LENGTH_SHORT).show();
								break; 
							case 1:
							    agent.startFeedbackActivity();
								break; 
							case 2:
//								�˳�Ӧ��
//								�㲥֪ͨ����Activity �ر����лActivity
								Intent exitIntent=new  Intent();
								exitIntent.setAction(Contants.EXIT);
								Log.i("sjl", "begin sendBroadcast����");
								MainActivity.this.sendBroadcast(exitIntent);
								
								finish();   
								overridePendingTransition(R.anim.fadein, R.anim.fadeout); 
//								������������� ������Դ  ���� ��̨�̹߳ر�  ���²��ܹ㲥�������� by sjl 20130627
//								System.exit(0);
								break;
						}
					}
				}).setPositiveButton("ȡ��", null).show();
				
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
		Toast.makeText(this, "�ύ���ٶ��Ʒ���������ȴ�..", Toast.LENGTH_SHORT).show();
		json=formatRequestParams();
		Thread mythread=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String,String> requestParams=new HashMap<String, String>();
					requestParams.put("answer", json.toString());
					String jsonstring=HttpUtil.postRequest("datacommit", requestParams);
					Message me=new Message();
					Log.i("sjl", "����������ֵ��"+jsonstring);
					me.obj=jsonstring;
					myhandler.sendMessage(me);
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
			});
//		��������ύ����
		mythread.start();
		handleResult();
	}
	public void  handleResult(){
//		���ݲ�ͬ����� ���в�ͬ���߼��ж�
		int register=0;
		Resources rs=this.getResources();
		String [] answerStandard=rs.getStringArray(R.array.answers);
		int dangerIndexNum=0;
		boolean result=true; 
		switch(questionType){
			case 0:
//				������ص������δ�Ѽ��ã������ʹ����߼����Ҹ���
				break; 
			case 1:
//				���Խ�������ԡ�˵�� ������������ɸ�� 16~30���� �� ͯ ,�� 23 �� �� �� �� �� д �� �� �� �� �� �� �� �� ׼ :�� ��ѡ��Ϊ���ǡ��򡰷�,�� 1��18��20��2��ѡ���ǡ�, �������ѡ����ʱ���жϸ���Ϊ����;�������б� ��׼:�ܹ� 23���С�3�����Ի� 6�������Ŀ�� (�� �� �� Ŀ Ϊ �� 2��7��9��13��14��15 �� )�� 2 �� �� �� Ϊ �¶�֢�߷���,���һ���绰���,��δͨ�������� ��һ��������
//				16��30�������
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
//				ͬ0��������ص������δ�Ѽ��ã������ʹ����߼����Ҹ���
				break; 
			default:
				break; 
		}
		if(!result){
			questionTv.setText(Html.fromHtml("���Խ�������� <br/> �¶�֢�߷���,����ѯ��ع�����Ա<br/>���һ���绰���,��δͨ���������һ������"));
		}else{
			questionTv.setText("���Խ����������");
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
//			ʵ�ּ����Ϸ��ؼ�����ʱ�Ļ��潥��Ч��
	    	finish();   
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);   
			return true;   
	    }   
	    return super.onKeyUp(keyCode, event);   
	} 
	
	
}

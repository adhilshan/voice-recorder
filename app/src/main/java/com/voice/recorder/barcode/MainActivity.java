package com.voice.recorder.barcode;

import com.voice.recorder.barcode.SplashActivity;
import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.airbnb.lottie.*;
import com.google.android.material.button.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private double n = 0;
	private double n2_min = 0;
	private double n3_hrs = 0;
	private double ran = 0;
	private double Audiosampling = 0;
	private double BitRate = 0;
	
	private ArrayList<String> letters = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout toolbar;
	private LinearLayout onrec;
	private LinearLayout linear4;
	private LinearLayout linear7;
	private ImageView imageview4;
	private TextView textview3;
	private TextView textview4;
	private LinearLayout linear6;
	private CardView cardview1;
	private TextView textview1;
	private TextView textview2;
	private LinearLayout linear2;
	private ImageView imageview5;
	private LottieAnimationView lottie1;
	private MaterialButton materialbutton1;
	private MaterialButton materialbutton2;
	
	private SpeechRecognizer spt;
	private TimerTask t1;
	private Calendar cal1 = Calendar.getInstance();
	private Intent i1 = new Intent();
	private Intent i2 = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		toolbar = findViewById(R.id.toolbar);
		onrec = findViewById(R.id.onrec);
		linear4 = findViewById(R.id.linear4);
		linear7 = findViewById(R.id.linear7);
		imageview4 = findViewById(R.id.imageview4);
		textview3 = findViewById(R.id.textview3);
		textview4 = findViewById(R.id.textview4);
		linear6 = findViewById(R.id.linear6);
		cardview1 = findViewById(R.id.cardview1);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		linear2 = findViewById(R.id.linear2);
		imageview5 = findViewById(R.id.imageview5);
		lottie1 = findViewById(R.id.lottie1);
		materialbutton1 = findViewById(R.id.materialbutton1);
		materialbutton2 = findViewById(R.id.materialbutton2);
		spt = SpeechRecognizer.createSpeechRecognizer(this);
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i2.setClass(getApplicationContext(), ListActivity.class);
				startActivity(i2);
			}
		});
		
		materialbutton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				toolbar.setBackgroundColor(0xFFF44336);
				lottie1.setVisibility(View.VISIBLE);
				imageview5.setVisibility(View.GONE);
				materialbutton1.setVisibility(View.GONE);
				materialbutton2.setVisibility(View.VISIBLE);
				rec=new MediaRecorder();
				
				rec.setAudioEncodingBitRate((int) BitRate);
				rec.setAudioSamplingRate((int) Audiosampling);
				rec.setAudioSource(MediaRecorder.AudioSource.MIC);
				rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				rec.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
				rec.setOutputFile(FileUtil.getExternalStorageDir().concat("/Recorder/").concat(textview2.getText().toString()));
				try {
						rec.prepare();
						rec.start();
				} catch (Exception e) {}
				n = 0;
				n2_min = 0;
				n3_hrs = 0;
				t1 = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								n++;
								if (n > 59) {
									n = 0;
									n2_min++;
								}
								if (n2_min > 59) {
									n2_min = 0;
									n3_hrs++;
								}
								textview1.setText(new DecimalFormat("00").format(n3_hrs).concat(":".concat(new DecimalFormat("00").format(n2_min).concat(":".concat(new DecimalFormat("00").format(n))))));
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(t1, (int)(0), (int)(1000));
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =MainActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFFF44336);
				}
			}
		});
		
		materialbutton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				t1.cancel();
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =MainActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFFF44336);
				}
				lottie1.setVisibility(View.GONE);
				imageview5.setVisibility(View.VISIBLE);
				materialbutton1.setVisibility(View.VISIBLE);
				materialbutton2.setVisibility(View.GONE);
				textview1.setText("00:00:00");
				_randomLetterGen(25, textview2);
				try {
						rec.stop();
						rec.release();
						rec = null;
					
					_toast_error_or_not("Audio is saved to ".concat(FileUtil.getExternalStorageDir().concat("/Recorder/").concat(textview2.getText().toString().concat(" File"))), "");
				} catch(Exception e) {
					SketchwareUtil.showMessage(getApplicationContext(), "Failed To Save Audio");
				}
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =MainActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF607D8B);
				}
				toolbar.setBackgroundColor(0xFF607D8B);
				final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this).create();
				View inflate = getLayoutInflater().inflate(R.layout.saved,null); 
				dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				dialog1.setView(inflate);
				LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);
				final TextView b1 = (TextView) inflate.findViewById(R.id.b1);
				android.graphics.drawable.GradientDrawable GP = new android.graphics.drawable.GradientDrawable();
				GP.setColor(Color.parseColor("#FFFFFF"));
				GP.setCornerRadius((float)15);
				GP.setStroke((int) 0,
				Color.parseColor("#" + "#FFFFFF".replace("#", "")));
				android.graphics.drawable.RippleDrawable RK = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor("#FFFFFF")}), GP, null);
				bg.setBackground(RK);
				b1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
								dialog1.dismiss();
						}
				});
				dialog1.setCancelable(false);
				dialog1.show();
			}
		});
	}
	
	private void initializeLogic() {
		
		_randomLetterGen(25, textview2);
		FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/Recorder"));
		lottie1.setVisibility(View.GONE);
		materialbutton2.setVisibility(View.GONE);
		linear6.setVisibility(View.GONE);
		//changes in this varibales occur to change in voice quality
		Audiosampling = 44100;
		BitRate = 128000;
	}
	MediaRecorder rec;
	{
	}
	
	public void _extra() {
		
	}
	private void fo4o() {
	}
	
	
	public void _toast_error_or_not(final String _text, final String _error) {
		//read the code and their comments  carefully
		//if you want an error toast enter string value as "error"
		LayoutInflater inflater = getLayoutInflater(); View toastLayout = inflater.inflate(R.layout.custom1, null);
		
		TextView textview1 = (TextView) toastLayout.findViewById(R.id.textview1);
		textview1.setText(_text);
		LinearLayout linear1 = (LinearLayout) toastLayout.findViewById(R.id.linear1);
		
		
		if (_error.equals("error")) {
			//error toast
			android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
			textview1.setTextColor(Color.parseColor("#F44336"));
			gd.setColor(Color.parseColor("#FFFFFF"));
			gd.setCornerRadius(20);
			gd.setStroke(3, Color.parseColor("#F44336"));
			linear1.setBackground(gd);
			
			
		}
		else {
			//ordinary toast
			android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
			gd.setColor(Color.parseColor("#FFFFFF"));
			gd.setCornerRadius(20);
			gd.setStroke(3, Color.parseColor("#2196F3"));
			linear1.setBackground(gd);
			
			
		}
		Toast toast = new Toast(getApplicationContext()); toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP,0,110);
		toast.setView(toastLayout);
		toast.show();
		//code created by androcub softwares
		//follow as on instagram
		//@androcub
	}
	
	
	public void _randomLetterGen(final double _lenght, final TextView _textview) {
		cal1 = Calendar.getInstance();
		ran = SketchwareUtil.getRandom((int)(0), (int)(9999));
		textview2.setText(new SimpleDateFormat("hh_mm_EEE_d_MMM").format(cal1.getTime()).concat("_".concat(new DecimalFormat("0000").format(ran).concat(".mp3"))));
	}
	
	
	public void _Card_View(final View _view, final double _cornerradius, final String _bgcolor, final double _elevation, final double _stroke, final String _strokecolor) {
		if (_stroke == 0) {
			//𝐁𝐥𝐨𝐜𝐤 𝐜𝐫𝐞𝐚𝐭𝐞𝐝 𝐛𝐲 𝐇-6𝐢𝐱
			android.graphics.drawable.GradientDrawable cv = new android.graphics.drawable.GradientDrawable(); 
			float cornerradius = (float) _cornerradius;
			cv.setCornerRadius(cornerradius);
			cv.setColor(Color.parseColor("#" + _bgcolor.replace("#", "")));
			_view.setBackground(cv);
			float elevation = (float) _elevation;
			_view.setElevation(elevation);
		}
		else {
			android.graphics.drawable.GradientDrawable cv = new android.graphics.drawable.GradientDrawable(); 
			float cornerradius = (float) _cornerradius;
			cv.setStroke((int)_stroke, Color.parseColor("#" + _strokecolor.replace("#", "")));
			cv.setCornerRadius(cornerradius);
			cv.setColor(Color.parseColor("#" + _bgcolor.replace("#", "")));
			_view.setBackground(cv);
			float elevation = (float) _elevation;
			_view.setElevation(elevation);
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
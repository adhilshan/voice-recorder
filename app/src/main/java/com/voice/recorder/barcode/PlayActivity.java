package com.voice.recorder.barcode;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.media.MediaPlayer;
import android.net.*;
import android.net.Uri;
import android.os.*;
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
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class PlayActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private String currentfile = "";
	private String path = "";
	private double rotation = 0;
	private double rotation_number = 0;
	
	private LinearLayout bg;
	private LinearLayout screen;
	private LinearLayout linear11;
	private LinearLayout panel;
	private ImageView screen_img;
	private TextView textview5;
	private LinearLayout linear14;
	private SeekBar progress;
	private LinearLayout linear12;
	private ImageView imageview2;
	private TextView time_v1;
	private LinearLayout linear13;
	private TextView textview3;
	
	private MediaPlayer mp;
	private TimerTask t3;
	private TimerTask t2;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.play);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
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
		bg = findViewById(R.id.bg);
		screen = findViewById(R.id.screen);
		linear11 = findViewById(R.id.linear11);
		panel = findViewById(R.id.panel);
		screen_img = findViewById(R.id.screen_img);
		textview5 = findViewById(R.id.textview5);
		linear14 = findViewById(R.id.linear14);
		progress = findViewById(R.id.progress);
		linear12 = findViewById(R.id.linear12);
		imageview2 = findViewById(R.id.imageview2);
		time_v1 = findViewById(R.id.time_v1);
		linear13 = findViewById(R.id.linear13);
		textview3 = findViewById(R.id.textview3);
		
		progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				mp.seekTo((int)(progress.getProgress()));
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (mp.isPlaying()) {
					_MediaPause();
				}
				else {
					_MediaStart();
				}
			}
		});
	}
	
	private void initializeLogic() {
		textview5.setText(Uri.parse(getIntent().getStringExtra("path")).getLastPathSegment());
		path = getIntent().getStringExtra("path");
		_CreateMedia(0);
	}
	
	public void _CreateMedia(final double _pos) {
		currentfile = path;
		mp = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new java.io.File(currentfile)));
		progress.setMax((int)mp.getDuration());
		int dur = (int) mp.getDuration();
		
		int mns = (dur / 60000) % 60000;
		int scs = dur % 60000 / 1000;
		
		NumberFormat formatter = new DecimalFormat("00");
		String seconds = formatter.format(scs);
		
		String songTime = String.format("%02d:%02d", mns,  scs);
		textview3.setText(songTime);
	}
	
	
	public void _MediaStart() {
		mp.start();
		imageview2.setImageResource(R.drawable.pause);
		t2 = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try{
							progress.setProgress((int)mp.getCurrentPosition());
							int dur = (int) mp.getCurrentPosition();
							
							int mns = (dur / 60000) % 60000;
							int scs = dur % 60000 / 1000;
							
							NumberFormat formatter = new DecimalFormat("00");
							String seconds = formatter.format(scs);
							
							String currentTime = String.format("%02d:%02d", mns,  scs);
							time_v1.setText(currentTime);
						}catch(Exception e){
							SketchwareUtil.showMessage(getApplicationContext(), "Action failed");
							finish();
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(t2, (int)(400), (int)(400));
		t3 = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						rotation++;
						screen_img.setRotation((float)(rotation));
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(t3, (int)(0), (int)(40));
	}
	
	
	public void _MediaPause() {
		mp.pause();
		imageview2.setImageResource(R.drawable.playbutton);
		t3.cancel();
		rotation = 0;
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
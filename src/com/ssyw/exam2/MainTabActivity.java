package com.ssyw.exam2;

import java.util.Timer;
import java.util.TimerTask;
import com.ssyw.exam2.adapter.MainTabPagerAdapter;
import com.ssyw.exam2.controller.MainTabController;
import com.ssyw.exam2.controller.TopicController;
import com.ssyw.exam2.project.ProjectConfig;
import com.ssyw.exam2.util.FileUtil;
import com.ssyw.exam2.util.UiUtil;
import com.ssyw.exam2.widget.IconPageIndicator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainTabActivity extends FragmentActivity implements
		MoreListFragment.Callbacks, ClassicsListFragment.Callbacks {

	private ViewPager main_tab_pager;// viewpager
	private IconPageIndicator main_tab_icon_indicator;
	private MainTabPagerAdapter mtpa;
	private MainTabController mtc;
	private TextView tv_title;
	private FileUtil fu;

	// for exit
	private static Timer tExit;
	private static TimerTask task;
	private static Boolean isExit = false;
	private static Boolean hasTask = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tab);

		mtc = new MainTabController(this);

		main_tab_pager = (ViewPager) findViewById(R.id.main_tab_pager);
		main_tab_icon_indicator = (IconPageIndicator) findViewById(R.id.main_tab_icon_indicator);

		mtpa = mtc.getPagerAdapter(getSupportFragmentManager());
		main_tab_pager.setAdapter(mtpa);
		main_tab_icon_indicator.setViewPager(main_tab_pager);

		int page = getIntent().getIntExtra("page", -1);
		if (page < 0) {
			switchPage(0);
		} else {
			switchPage(page);
		}

		// for exit
		tExit = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				isExit = false;
				hasTask = true;
			}
		};

	}

	private void switchPage(int position) {
		tv_title = (TextView) findViewById(R.id.tv_title);
		main_tab_pager.setCurrentItem(position);
		tv_title.setText(mtpa.getTitles().get(position));
		main_tab_pager.setOnPageChangeListener(getOnPageChangeListener());
	}

	@Override
	// for more
	public void onMoreItemSelected(int position) {
		Intent intent=null;
		switch(position){
		case 0:
		case 1:
		case 2:
			intent = new Intent(this, MoreDetailsActivity.class);
			intent.putExtra("position", position);		
			break;
		case 3:
			intent = new Intent(this, ShareSettingActivity.class);
			break;
		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}

	@Override
	public void onClassicsIdSelected(int classicsId) {
		Intent intent = new Intent(this, ClassicsActivity.class);
		intent.putExtra("questionId", classicsId);
		startActivity(intent);
	}
	
	public void shotView(View view) {
		fu = new FileUtil(this);
		Bitmap bm = fu.shotAndSave(fu.getPic_path());

		Intent toShare = new Intent(this, ShareFriendActivity.class);
		startActivity(toShare);
		// 保存完毕，及时回收
		if (!bm.isRecycled()) {
			bm.recycle();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// for exit
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				UiUtil.showToastShort(this, R.string.main_exit_prompt);
				if (!hasTask) {
					tExit.schedule(task, 2000);
				} else {
					finish();
					System.exit(0);
				}
			}
		}
		return false;
	}

	public void toSequence(View v) {
		Intent intent = new Intent(this, TopicActivity.class);
		intent.putExtra("mode", TopicController.MODE_SEQUENCE);
		startActivity(intent);
	}

	public void toRandom(View v) {
		Intent intent = new Intent(this, TopicActivity.class);
		intent.putExtra("mode", TopicController.MODE_RANDOM);
		startActivity(intent);
	}

	public void toPracticeTest(View v) {
		Intent intent = new Intent(this, TopicActivity.class);
		intent.putExtra("mode", TopicController.MODE_PRACTICE_TEST);
		startActivity(intent);
	}

	public void toChapters(View v) {
		if (ProjectConfig.TOPIC_MODE_CHAPTERS_SUPPORT) {
			Intent intent = new Intent(this, TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_CHAPTERS);
			startActivity(intent);
		} else {
			UiUtil.showToastShort(this, R.string.please_wait);
		}
	}

	public void toIntensify(View v) {
		if (ProjectConfig.TOPIC_MODE_INTENSIFY_SUPPORT) {
			Intent intent = new Intent(this, TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_INTENSIFY);
			startActivity(intent);
		} else {
			UiUtil.showToastShort(this, R.string.please_wait);
		}
	}

	public void toStatistics(View v) {
		Intent intent = new Intent(this, StatisticsActivity.class);
		startActivity(intent);
	}

	public void toWrongTopic(View v) {
		if (mtc.checkWrongDataExist()) {
			Intent intent = new Intent(this, TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_WRONG_TOPIC);
			startActivity(intent);
		} else {
			UiUtil.showToastShort(this, R.string.data_not_exist);
		}

	}

	public void toCollect(View v) {
		if (mtc.checkCollectedDataExist()) {
			Intent intent = new Intent(this, TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_COLLECT);
			startActivity(intent);
		} else {
			UiUtil.showToastShort(this, R.string.data_not_exist);
		}

	}

	public void toRecord(View v) {
		Intent intent=new Intent(MainTabActivity.this,
				RecordActivity.class);
		startActivity(intent);
	}

	private OnPageChangeListener getOnPageChangeListener() {
		return (new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				tv_title.setText(mtpa.getTitles().get(position));
				main_tab_icon_indicator.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
			}
		});
	}

	
}

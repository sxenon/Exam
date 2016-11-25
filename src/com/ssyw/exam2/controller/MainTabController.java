package com.ssyw.exam2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.ssyw.exam2.R;
import com.ssyw.exam2.adapter.MainTabPagerAdapter;
import com.ssyw.exam2.model.QuestionBankService;

public class MainTabController {
	private Context context;
	private QuestionBankService questionBankService;
	public MainTabController(Context context) {
		super();
		this.context = context;
		questionBankService=new QuestionBankService();
	}

	public MainTabPagerAdapter getPagerAdapter(FragmentManager fm){
		
		List<Integer> icons=new ArrayList<Integer>();
		List<String> titles=new ArrayList<String>();
		
		icons.add(R.drawable.tab_group_exam);
		icons.add(R.drawable.tab_group_classics);
		icons.add(R.drawable.tab_group_more);
		
		String[] main_title=context.getResources().getStringArray(R.array.main_title);
		for(int i=0;i<main_title.length;i++){
			titles.add(main_title[i]);
		}
		
		return new MainTabPagerAdapter(titles, icons,fm);
	}
	
	public boolean checkWrongDataExist(){
		ArrayList<Map<String, Object>> wrongDataList=questionBankService.getErrorEntryList(context);
		return !wrongDataList.isEmpty();
	}
	
	public boolean checkCollectedDataExist(){
		ArrayList<Map<String, Object>> collectDataList=questionBankService.getCollectedEntryList(context);
		return !collectDataList.isEmpty();
	}
	
	public ArrayList<Map<String,Object>> getClassicsEntryList(){
		return questionBankService.getClassicsEntryList(context);
	}
}

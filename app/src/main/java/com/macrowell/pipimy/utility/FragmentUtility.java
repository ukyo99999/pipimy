package com.macrowell.pipimy.utility;

import java.util.List;

import com.macrowell.pipimy.bean.CategoriesBean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class FragmentUtility {

	/*用來判斷ContainerFragment是否已經存在*/
	public static Fragment getContainerFragment(FragmentActivity fragmentActivity,
			int viewID) {
		FragmentManager fragmentManager = fragmentActivity
				.getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(viewID);

		return fragment;
	}

	/* 使用view id新增fragment */
	public static void addFragment(FragmentActivity fragmentActivity,
			int viewID, Fragment fragment, Intent intent) {

		FragmentManager fragmentManager = fragmentActivity
				.getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		if (intent != null) {
			fragment.setArguments(intent.getExtras());
		}

		fragmentTransaction.add(viewID, fragment);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();

	}

	/* 使用view id置換fragment */
	public static void replaceFragment(FragmentActivity fragmentActivity,
			int viewID, Fragment fragment, Intent intent, boolean addToBackStack) {

		FragmentManager fragmentManager = fragmentActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		if (intent != null) {
			fragment.setArguments(intent.getExtras());
		}

		fragmentTransaction.replace(viewID, fragment);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		if (addToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}

		fragmentTransaction.commit();

	}

}

package com.hamzah.holothemer.Settings;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceActivity.Header;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage, IXposedHookZygoteInit,
		IXposedHookInitPackageResources {

	XSharedPreferences pref;

	private static final String CLASS_HEADERADAPTER = "com.android.settings.Settings$HeaderAdapter";
	public static final String KEY_ACCOUNT_TYPE = "account_type";

	String MOD_PATH;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.android.settings"))
			return;
		try {
			final Class<?> headerAdapterClass = XposedHelpers.findClass(
					CLASS_HEADERADAPTER, lpparam.classLoader);

			try {
				if (Build.VERSION.SDK_INT <= 18) {
					XposedHelpers.findAndHookMethod(headerAdapterClass,
							"getView", int.class, View.class, ViewGroup.class,
							new XC_MethodHook() {

								@Override
								protected void afterHookedMethod(
										MethodHookParam param) throws Throwable {

									Header header = (Header) XposedHelpers
											.callMethod(param.thisObject,
													"getItem",
													(Integer) param.args[0]);
									View headerIcon = (View) param.getResult();
									Object headerViewHolder = XposedHelpers
											.callMethod(headerIcon, "getTag");
									if (header.extras != null
											&& header.extras
													.containsKey(KEY_ACCOUNT_TYPE)) {

									} else {
										final ImageView icon = (ImageView) XposedHelpers
												.getObjectField(
														headerViewHolder,
														"icon");

										if (icon == null)
											return;
										Drawable d = icon.getResources()
												.getDrawable(header.iconRes);
										pref.reload();
										int settingsIconColor = pref.getInt(
												Keys.COLOUR, Color.WHITE);
										d.setColorFilter(settingsIconColor,
												Mode.SRC_IN);
										icon.setImageDrawable(d);
									}
								}
							});
				}
			} catch (Throwable t) {
				XposedBridge.log(t);
			}
			try {
				if (Build.VERSION.SDK_INT > 18) {
					XposedHelpers
							.findAndHookMethod(
									headerAdapterClass,
									"updateCommonHeaderView",
									Header.class,
									"com.android.settings.Settings.HeaderAdapter.HeaderViewHolder",
									new XC_MethodHook() {

										@Override
										protected void afterHookedMethod(
												MethodHookParam param)
												throws Throwable {

											Header header = (Header) param.args[0];
											Object headerViewHolder = param.args[1];
											if (header.extras != null
													&& header.extras
															.containsKey(KEY_ACCOUNT_TYPE)) {

											} else {
												final ImageView icon = (ImageView) XposedHelpers
														.getObjectField(
																headerViewHolder,
																"icon");

												if (icon == null)
													return;

												Drawable d = icon
														.getResources()
														.getDrawable(
																header.iconRes);
												pref.reload();
												int settingsIconColor = pref
														.getInt(Keys.COLOUR,
																Color.WHITE);
												d.setColorFilter(
														settingsIconColor,
														Mode.SRC_IN);

												icon.setImageDrawable(d);
											}
										}
									});
				}
			} catch (Throwable t) {
				XposedBridge.log(t);
			}

		} catch (Throwable t) {
			XposedBridge.log(t);
		}
	}

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		pref = new XSharedPreferences("com.hamzah.holothemer.Settings",
				Keys.PREF_NAME);
		MOD_PATH = startupParam.modulePath;
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!resparam.packageName.equals("com.android.settings"))
			return;
		resparam.res.hookLayout("com.android.settings", "layout",
				"manage_applications_item", new XC_LayoutInflated() {
					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						TextView appname = (TextView) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"app_name", "id",
										"com.android.settings"));

						appname.setTextColor(Color.BLACK);

					}
				});
	}

}
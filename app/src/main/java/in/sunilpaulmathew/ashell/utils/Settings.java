package in.sunilpaulmathew.ashell.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.color.DynamicColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import in.sunilpaulmathew.ashell.R;
import in.sunilpaulmathew.ashell.activities.aShellActivity;
import in.sunilpaulmathew.ashell.dialogs.SingleChoiceAlertDialog;
import in.sunilpaulmathew.ashell.serializables.ExtrasEntry;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 21, 2022
 */
public class Settings {

    public static Bitmap generateAlphabetIcon(String alphabet, int sizeDp, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int sizePx = (int) (sizeDp * scale + 0.5f);

        Bitmap bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        boolean isDarkMode = isDarkTheme(context);

        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isDarkMode) {
            bgPaint.setColor(Color.parseColor("#3A3A3C"));
        } else {
            bgPaint.setColor(Color.parseColor("#E4E4E6"));
        }

        RectF rect = new RectF(0, 0, sizePx, sizePx);
        canvas.drawRoundRect(rect, sizePx * 0.25f, sizePx * 0.25f, bgPaint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isDarkMode) {
            textPaint.setColor(Color.parseColor("#FFFFFF"));
        } else {
            textPaint.setColor(Color.parseColor("#1C1C1E"));
        }

        textPaint.setTextSize(sizePx * 0.45f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);

        float xPos = sizePx / 2f;
        float yPos = (sizePx / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);

        canvas.drawText(alphabet, xPos, yPos, textPaint);

        return bitmap;
    }

    public static boolean isAmoledBlackEnabled(Context context) {
        return isDarkTheme(context) && Utils.getBoolean("amoledTheme", false, context);
    }

    public static boolean isDarkTheme(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static boolean isExtrasEnabled(Context context) {
        int size = 0;
        for (int i=0; i<5; i++) {
            if (Utils.getBoolean("extras_" + i, false, context)) {
                size += 1;
            }
        }
        return size != 0;
    }

    public static int getColorAccent(Context context) {
        return getMaterial3Colors(Utils.getColor(R.color.colorBlue, context), context);
    }

    public static int getColorText(Context context) {
        return Utils.getColor(isDarkTheme(context) ? R.color.colorWhite : R.color.colorBlack, context);
    }

    private static int getMaterial3Colors(int defaultColor, Context context) {
        int color = defaultColor;
        if (DynamicColors.isDynamicColorAvailable()) {
            Context dynamicClrCtx = DynamicColors.wrapContextIfAvailable(context, com.google.android.material.R.style.MaterialAlertDialog_Material3);
            TypedArray ta = dynamicClrCtx.obtainStyledAttributes(new int[] {
                    androidx.appcompat.R.attr.colorPrimary
            });
            color = ta.getColor(0, defaultColor);
            ta.recycle();
        }
        return color;
    }

    public static int getAppThemePosition(Context context) {
        return Utils.getInt("appTheme", 0, context);
    }

    public static int getCardBackgroundColor(int icon, Context context) {
        boolean dark = Settings.isDarkTheme(context);
        String hexColor = dark ? "#1E1E23" : "#FFFFFF";
        if (icon == R.drawable.ic_shell) {
            hexColor = dark ? "#1E293B" : "#F1F5F9";
        } else if (icon == R.drawable.ic_technology) {
            hexColor = dark ? "#064E3B" : "#D1FAE5";
        } else if (icon == R.drawable.ic_charging_high) {
            hexColor = dark ? "#78350F" : "#FEF3C7";
        } else if (icon == R.drawable.ic_folder) {
            hexColor = dark ? "#4C1D95" : "#EDE9FE";
        } else if (icon == R.drawable.ic_apps) {
            hexColor = dark ? "#0C4A6E" : "#E0F2FE";
        } else if (icon == R.drawable.ic_logcat) {
            hexColor = dark ? "#881337" : "#FFE4E6";
        }
        return Color.parseColor(hexColor);
    }

    public static int getIconColor(int icon, Context context) {
        boolean dark = Settings.isDarkTheme(context);
        String hexColor = dark ? "#FFFFFF" : "#000000";
        if (icon == R.drawable.ic_shell) {
            hexColor = dark ? "#475569" : "#94A3B8";
        } else if (icon == R.drawable.ic_technology) {
            hexColor = dark ? "#34D399" : "#10B981";
        } else if (icon == R.drawable.ic_charging_high) {
            hexColor = dark ? "#FFF3E0" : "#E65100";
        } else if (icon == R.drawable.ic_folder) {
            hexColor = dark ? "#A78BFA" : "#7C3AED";
        } else if (icon == R.drawable.ic_apps) {
            hexColor = dark ? "#38BDF8" : "#0284C7";
        } else if (icon == R.drawable.ic_logcat) {
            hexColor = dark ? "#E1F5FE" : "#01579B";
        }
        return Color.parseColor(hexColor);
    }

    private static int getLanguagePosition(Context context) {
        switch (getLanguage(context)) {
            case "ar":
                return 1;
            case "en":
                return Objects.equals(getCountry(context), "US") ? 2 : 0;
            default:
                return 0;
        }
    }

    public static List<ExtrasEntry> getExtras() {
        List<ExtrasEntry> data = new ArrayList<>();
        data.add(new ExtrasEntry(R.drawable.ic_folder, R.string.file_manager, R.string.file_manager_description, 0));
        data.add(new ExtrasEntry(R.drawable.ic_apps, R.string.app_manager, R.string.app_manager_description, 1));
        data.add(new ExtrasEntry(R.drawable.ic_technology, R.string.cpu_monitor, R.string.cpu_monitor_description, 2));
        data.add(new ExtrasEntry(R.drawable.ic_charging_high, R.string.battery_monitor, R.string.battery_monitor_description, 3));
        data.add(new ExtrasEntry(R.drawable.ic_logcat, R.string.logcat, R.string.logcat_description, 4));
        return data;
    }

    public static String getAppTheme(Context context) {
        int appTheme = Utils.getInt("appTheme", 0, context);
        switch (appTheme) {
            case 2:
                return context.getString(R.string.app_theme_light);
            case 1:
                return context.getString(R.string.app_theme_dark);
            default:
                return context.getString(R.string.app_theme_auto);
        }
    }

    private static String getCountry(Context context) {
        return Utils.getString("country", java.util.Locale.getDefault().getLanguage(), context);
    }

    private static String getLanguage(Context context) {
        return Utils.getString("appLanguage", java.util.Locale.getDefault().getLanguage(), context);
    }

    public static String getLanguageDescription(Context context) {
        switch (getLanguage(context)) {
            case "ar":
                return context.getString(R.string.language_ar, "AR");
            case "en":
                return Objects.equals(getCountry(context), "US")
                        ? context.getString(R.string.language_en, "US")
                        : context.getString(R.string.app_theme_auto);
            default:
                return context.getString(R.string.app_theme_auto);
        }
    }
    public static String[] getAppThemeMenu(Context context) {
        return new String[] {
                context.getString(R.string.app_theme_auto),
                context.getString(R.string.app_theme_dark),
                context.getString(R.string.app_theme_light)
        };
    }

    private static String[] getLanguageMenu(Context context) {
        return new String[] {
                context.getString(R.string.app_theme_auto),
                context.getString(R.string.language_ar, "AR"),
                context.getString(R.string.language_en, "US")
        };
    }
    public static void initializeAppTheme(Context context) {
        int appTheme = Utils.getInt("appTheme", 0, context);
        switch (appTheme) {
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        context.setTheme(Settings.isAmoledBlackEnabled(context) ? R.style.AppTheme_Amoled : R.style.AppTheme);
    }

    private static Locale getLocale(Context context) {
        if (getCountry(context) != null) {
            return new Locale(getLanguage(context), getCountry(context));
        } else {
            return new Locale(getLanguage(context));
        }
    }

    public static void enableViewPagerSwiping(boolean enabled, Activity activity) {
        if (activity instanceof aShellActivity) {
            ((aShellActivity) activity).enableViewPagerSwiping(enabled);
        }
    }

    public static void initializeAppLanguage(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(getLocale(context));
        res.updateConfiguration(conf, dm);
    }

    public static void navigateToFragment(int position, String command, Activity activity) {
        if (activity instanceof aShellActivity) {
            ((aShellActivity) activity).navigateToFragment(position, command);
        }
    }

    public static void restartApp(Activity activity) {
        Intent mainActivity = new Intent(activity, aShellActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(mainActivity);
        activity.finish();
    }

    public static void setAppLanguage(Activity activity) {
        new SingleChoiceAlertDialog(R.drawable.ic_language, activity.getString(R.string.language),
                getLanguageMenu(activity), getLanguagePosition(activity), activity) {

            @Override
            public void onItemSelected(int position) {
                switch (position) {
                    case 1:
                        if (Objects.equals(getLanguage(activity), "ar") && Objects.equals(getCountry(activity), null)) {
                            return;
                        }
                        Utils.saveString("appLanguage", "ar", activity);
                        Utils.saveString("country", null, activity);
                        break;
                    case 2:
                        if (Objects.equals(getLanguage(activity), "en") && Objects.equals(getCountry(activity), "US")) {
                            return;
                        }
                        Utils.saveString("appLanguage", "en", activity);
                        Utils.saveString("country", "US", activity);
                        break;
                    default:
                        if (Objects.equals(getLanguage(activity), Locale.getDefault().getLanguage()) && Objects.equals(getCountry(activity), Locale.getDefault().getCountry())) {
                            return;
                        }
                        Utils.saveString("appLanguage", java.util.Locale.getDefault().getLanguage(), activity);
                        Utils.saveString("country", java.util.Locale.getDefault().getCountry(), activity);
                        break;
                }
                restartApp(activity);
            }
        }.show();
    }
    public static void setSlideInAnimation(final View viewToAnimate, int position) {
        // Only animate items appearing for the first time
        if (position > -1) {
            viewToAnimate.setTranslationY(50f);
            viewToAnimate.setAlpha(0f);

            viewToAnimate.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(150)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            // Reset properties to ensure recycled views are displayed correctly
            viewToAnimate.setTranslationY(0f);
            viewToAnimate.setAlpha(1f);
        }
    }

}
package com.core.realwear.sdk.Util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.core.realwear.sdk.R;
import com.core.realwear.sdk.views.LanguageCarouselAdapter;
import com.core.realwear.sdk.views.LanguageCarouselLayoutManager;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Luke Hopkins on 28/12/2016.
 */
public class LanguageDialog extends FullScreenDialog {
    private static final String TAG = LanguageDialog.class.getCanonicalName();

    public static class LocaleInfo implements Comparable<LocaleInfo> {
        static final Collator COLLATOR = Collator.getInstance();

        String label;
        Locale locale;

        public LocaleInfo(String label, Locale locale) {
            this.label = label;
            this.locale = locale;
        }

        public String getLabel() {
            return label;
        }

        public Locale getLocale() {
            return locale;
        }

        @Override
        public String toString() {
            return label;
        }

        @Override
        public int compareTo(@NonNull LocaleInfo another) {
            return COLLATOR.compare(label, another.label);
        }
    }

    private Locale mSelectedLocale;
    private Timer mAnimationTimer;
    private LanguageCarouselAdapter.ViewHolder mPreviewViewHolder;

    private int mCurrentPosition = -1;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public LanguageDialog(Context context) {
        this(context, 0);
    }

    public LanguageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LanguageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Locale getSelectedLanguage() {
        return mSelectedLocale;
    }

    private void setCurrentLanguage() {
        final Resources resources = getContext().getResources();

        final String[] locales = Resources.getSystem().getAssets().getLocales();
        final List<String> localeList = new ArrayList<>(locales.length);
        Collections.addAll(localeList, locales);

        // Remove the pseudolocales.
        localeList.remove("ar-XB");
        localeList.remove("en-XA");

        // Sort the locales.
        Collections.sort(localeList);

        final String[] specialLocaleCodes = resources.getStringArray(R.array.special_locale_codes);
        final String[] specialLocaleNames = resources.getStringArray(R.array.special_locale_names);

        final List<LocaleInfo> localeInfos = new ArrayList<>(localeList.size());

        final String[] localesFilter = resources.getStringArray(R.array.supported_languages);

        for (String localeName : localeList) {
            if (!filterLanguage(localeName, localesFilter)) {
                continue;
            }

            final Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                locale = Locale.forLanguageTag(localeName.replace("_", "-"));
            } else {
                // TODO: Fix this.
                locale = Locale.CANADA;
            }

            if (locale == null || "und".equals(locale.getLanguage()) ||
                    locale.getLanguage().isEmpty() || locale.getCountry().isEmpty()) {
                continue;
            }

            if (localeInfos.isEmpty()) {
                Log.v(TAG, "Adding initial " + toTitleCase(locale.getDisplayLanguage(locale)));
                localeInfos.add(new LocaleInfo(toTitleCase(locale.getDisplayLanguage(locale)), locale));
            } else {
                final LocaleInfo previousInfo = localeInfos.get(localeInfos.size() - 1);
                if (previousInfo.locale.getLanguage().equals(locale.getLanguage()) &&
                        !previousInfo.locale.getLanguage().equals("zz")) {
                    previousInfo.label = toTitleCase(getDisplayName(previousInfo.locale, specialLocaleCodes, specialLocaleNames));

                    localeInfos.add(new LocaleInfo(toTitleCase(getDisplayName(locale, specialLocaleCodes, specialLocaleNames)), locale));
                } else {
                    String displayName = toTitleCase(locale.getDisplayLanguage(locale));
                    localeInfos.add(new LocaleInfo(displayName, locale));
                }
            }
        }

        Collections.sort(localeInfos);

        final Locale currentLocale = Locale.getDefault();
        int index = 0;
        for (LocaleInfo localeInfo : localeInfos) {
            if (localeInfo.locale.equals(currentLocale)) {
                break;
            }

            index++;
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
        recyclerView.setAdapter(new LanguageCarouselAdapter(getContext(), localeInfos));
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        final LinearLayoutManager linearLayoutManager = new LanguageCarouselLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        final float itemWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140 + (24 * 2), displayMetrics);

        // Center the item to the middle of the screen.
        mCurrentPosition = index + localeInfos.size() * 10;
        linearLayoutManager.scrollToPositionWithOffset(mCurrentPosition, (int) ((displayMetrics.widthPixels - itemWidth) / 2));
        linearLayoutManager.smoothScrollToPosition(recyclerView, null, mCurrentPosition);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPreviewViewHolder = (LanguageCarouselAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(mCurrentPosition);

                if (mPreviewViewHolder != null) {
                    mPreviewViewHolder.getBackground().setBackgroundColor(getContext().getResources().getColor(R.color.radio));
                    mPreviewViewHolder.getLabel().getBackground().setTint(getContext().getResources().getColor(R.color.radio));
                    mPreviewViewHolder.getLabel().setTextColor(getContext().getResources().getColor(android.R.color.white));
                }
            }
        }, 100);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        if (mAnimationTimer != null) {
            mAnimationTimer.cancel();
            mAnimationTimer.purge();
        }

        super.dismiss();
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == 500) {
            Language.setLanguage(mSelectedLocale);

            dismiss();
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCurrentLanguage();

        mAnimationTimer = new Timer();
        mAnimationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                final Resources resources = getContext().getResources();

                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
                final LanguageCarouselLayoutManager linearLayoutManager = (LanguageCarouselLayoutManager) recyclerView.getLayoutManager();

                linearLayoutManager.smoothScrollToPosition(recyclerView, null, ++mCurrentPosition);

                final LanguageCarouselAdapter.ViewHolder viewHolder = (LanguageCarouselAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(mCurrentPosition);
                final LanguageCarouselAdapter.ViewHolder previousViewHolder = mPreviewViewHolder;

                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator animator;

                        if (viewHolder != null) {
                            animator = ObjectAnimator
                                    .ofArgb(viewHolder.getBackground(), "backgroundColor", resources.getColor(R.color.language_bg), resources.getColor(R.color.radio));
                            animator.setDuration(100);
                            animator.start();

                            animator = ObjectAnimator
                                    .ofArgb(viewHolder.getLabel().getBackground(), "tint", resources.getColor(R.color.language_bg), resources.getColor(R.color.radio));
                            animator.setDuration(100);
                            animator.start();

                            animator = ObjectAnimator
                                    .ofArgb(viewHolder.getLabel(), "textColor", resources.getColor(R.color.language_text_color), resources.getColor(android.R.color.white));
                            animator.setDuration(100);
                            animator.start();
                        }

                        if (previousViewHolder != null) {
                            // Previous item.
                            animator = ObjectAnimator
                                    .ofArgb(previousViewHolder.getBackground(), "backgroundColor", resources.getColor(R.color.radio), resources.getColor(R.color.language_bg));
                            animator.setDuration(100);
                            animator.start();

                            animator = ObjectAnimator
                                    .ofArgb(previousViewHolder.getLabel().getBackground(), "tint", resources.getColor(R.color.radio), resources.getColor(R.color.language_bg));
                            animator.setDuration(100);
                            animator.start();

                            animator = ObjectAnimator
                                    .ofArgb(previousViewHolder.getLabel(), "textColor", resources.getColor(android.R.color.white), resources.getColor(R.color.language_text_color));
                            animator.setDuration(100);
                            animator.start();
                        }
                    }
                });

                mPreviewViewHolder = viewHolder;

                mSelectedLocale = ((LanguageCarouselAdapter) recyclerView.getAdapter()).getLocaleForPosition(mCurrentPosition);
            }
        }, 2000, 2000);
    }

    @Override
    public int getLayout() {
        return R.layout.language_dialog;
    }

    private boolean filterLanguage(String name, String[] locales) {
        for (String locale : locales) {
            if (locale.equals(name)) {
                return true;
            }
        }

        return false;
    }

    private String toTitleCase(String string) {
        if (string.length() == 0) {
            return string;
        }

        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    private String getDisplayName(Locale locale, String[] specialLocaleCodes, String[] specialLocaleNames) {
        final String code = locale.toString();

        for (int i = 0; i < specialLocaleCodes.length; i++) {
            if (specialLocaleCodes[i].equals(code)) {
                return specialLocaleNames[i];
            }
        }

        return locale.getDisplayName(locale);
    }
}

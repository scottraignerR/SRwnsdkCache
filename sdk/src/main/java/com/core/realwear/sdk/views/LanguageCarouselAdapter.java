package com.core.realwear.sdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.realwear.sdk.R;
import com.core.realwear.sdk.Util.LanguageDialog;

import java.util.List;
import java.util.Locale;

/**
 * Created by william on 16/02/17.
 */
public class LanguageCarouselAdapter extends RecyclerView.Adapter<LanguageCarouselAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mRootView;

        private ImageView mIcon;
        private TextView mLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            mRootView = itemView;

            mIcon = (ImageView) mRootView.findViewById(R.id.icon);
            mLabel = (TextView) mRootView.findViewById(R.id.label);
        }

        ImageView getIcon() {
            return mIcon;
        }

        TextView getLabel() {
            return mLabel;
        }
    }

    private final Context mContext;
    private final Resources mResources;

    private List<LanguageDialog.LocaleInfo> mLocaleInfos;

    public LanguageCarouselAdapter(Context context, List<LanguageDialog.LocaleInfo> localeInfos) {
        mContext = context;
        mResources = mContext.getResources();

        mLocaleInfos = localeInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.language_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position % mLocaleInfos.size();

        final LanguageDialog.LocaleInfo localeInfo = mLocaleInfos.get(index);

        int iconResId = R.drawable.en_us;

        String localeName = localeInfo.getLocale().toString().toLowerCase();
        int resId = mResources.getIdentifier(localeName,
                "drawable", mContext.getPackageName());

        if (resId != 0) {
            iconResId = resId;
        }

        // Update the holder with the view information.
        holder.getIcon().setImageResource(iconResId);
        holder.getLabel().setText(localeInfo.getLabel());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    /**
     * Get the locale at the specified position.
     *
     * @param position Position to retrieve the locale for.
     * @return Locale Retrieved.
     */
    public Locale getLocaleForPosition(int position) {
        final int index = position % mLocaleInfos.size();
        return mLocaleInfos.get(index).getLocale();
    }
}

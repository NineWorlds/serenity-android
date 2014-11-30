/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.leanback.search;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.SerenityImageLoader;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CardPresenter extends Presenter {

	static Context context;

	@Inject
	SerenityImageLoader serenityImageLoader;

	static ImageLoader imageLoader;

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent) {
		context = parent.getContext();
		imageLoader = serenityImageLoader.getImageLoader();

		ImageCardView imageView = new ImageCardView(context);
		imageView.setFocusable(true);
		imageView.setFocusableInTouchMode(true);
		imageView.setBackgroundColor(context.getResources().getColor(
				R.color.card_background));
		return new CardPresenterViewHolder(imageView);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Object item) {
		VideoContentInfo video = (VideoContentInfo) item;

		CardPresenterViewHolder cardHolder = (CardPresenterViewHolder) viewHolder;
		ImageCardView imageCardView = cardHolder.getCardView();
		cardHolder.setMovie(video);

		if (video.getImageURL() != null) {
			imageCardView.setTitleText(video.getTitle());
			imageCardView.setContentText(video.getStudio());
			cardHolder.updateCardViewImage(video.getImageURL());
		}

	}

	@Override
	public void onUnbindViewHolder(ViewHolder viewHolder) {
		CardPresenterViewHolder vh = (CardPresenterViewHolder) viewHolder;
		vh.mCardView.setBadgeImage(null);
		vh.mCardView.setMainImage(null);
	}

	static class CardPresenterViewHolder extends Presenter.ViewHolder {
		private VideoContentInfo video;
		private final ImageCardView mCardView;

		public CardPresenterViewHolder(View view) {
			super(view);
			mCardView = (ImageCardView) view;
		}

		public VideoContentInfo getMovie() {
			return video;
		}

		public void setMovie(VideoContentInfo m) {
			video = m;
		}

		public ImageCardView getCardView() {
			return mCardView;
		}

		protected void updateCardViewImage(String url) {
			imageLoader.displayImage(url, mCardView.getMainImageView());
		}
	}
}

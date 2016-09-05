package us.nineworlds.serenity.widgets;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import net.ganin.darv.ExtGridLayoutManager;

public class SerenityMenuGridLayoutManager extends ExtGridLayoutManager {

    public SerenityMenuGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        setAutoMeasureEnabled(false);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public static final class Builder {
        private final Context mCtx;
        private float mOffsetFraction = 0.0F;
        private boolean mOffsetEnabled = false;
        private boolean mCircular = false;
        private int mSpanCount = 0;
        private int mOrientation = 1;
        private boolean mReverseOrder = false;
        @Nullable
        private View mArrowTowardBegin;
        @Nullable
        private View mArrowTowardEnd;
        private int mSpanSize = 0;

        public Builder(Context ctx) {
            this.mCtx = ctx;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder offsetFraction(@FloatRange(
                from = 0.0D,
                to = 1.0D
        ) float offsetFraction) {
            this.mOffsetFraction = offsetFraction;
            this.mOffsetEnabled = true;
            return this;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder circular(boolean circular) {
            this.mCircular = circular;
            return this;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder spanCount(int spanCount) {
            if(spanCount < 1 && spanCount != 0) {
                throw new IllegalArgumentException("Span count must be > 0 or AUTO_FIT");
            } else {
                this.mSpanCount = spanCount;
                return this;
            }
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder orientation(int orientation) {
            this.mOrientation = orientation;
            return this;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder reverseOrder(boolean reverseOrder) {
            this.mReverseOrder = reverseOrder;
            return this;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder navigationArrows(@Nullable View arrowTowardBegin, @Nullable View arrowTowardEnd) {
            this.mArrowTowardBegin = arrowTowardBegin;
            this.mArrowTowardEnd = arrowTowardEnd;
            return this;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder spanSizePx(int spanSizePx) {
            this.mSpanSize = spanSizePx;
            return this;
        }

        @NonNull
        public SerenityMenuGridLayoutManager.Builder spanSizeRes(@DimenRes int spanSizeRes) {
            return this.spanSizePx(this.mCtx.getResources().getDimensionPixelSize(spanSizeRes));
        }

        @NonNull
        public SerenityMenuGridLayoutManager build() {
            SerenityMenuGridLayoutManager lm = new SerenityMenuGridLayoutManager(this.mCtx, this.mSpanCount, this.mOrientation, this.mReverseOrder);
            return this.configure(lm);
        }

        @NonNull
        private SerenityMenuGridLayoutManager configure(@NonNull SerenityMenuGridLayoutManager inst) {
            inst.setCircular(this.mCircular);
            if(this.mOffsetEnabled) {
                inst.setOffset(this.mOffsetFraction);
            }

            inst.setArrowTowardBegin(this.mArrowTowardBegin);
            inst.setArrowTowardEnd(this.mArrowTowardEnd);
            inst.setSpanSizePx(this.mSpanSize);
            return inst;
        }
    }
}

package com.vickykdv.circlerectimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;

@SuppressLint("AppCompatCustomView")
public class CircleRectImage extends ImageView {

    public static final String TAG = "RoundSet";
    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;
    public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP;
    private static final ScaleType[] SCALE_TYPES = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    private final float[] mCornerRadii =
            new float[] { DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS };

    private Drawable mBackgroundDrawable;
    private ColorStateList mBorderColor =
            ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR);
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;
    private ColorFilter mColorFilter = null;
    private boolean mColorMod = false;
    private Drawable mDrawable;
    private boolean mHasColorFilter = false;
    private boolean mIsOval = false;
    private boolean mMutateBackground = false;
    private int mResource;
    private int mBackgroundResource;
    private ScaleType mScaleType;
    private Shader.TileMode mTileModeX = DEFAULT_TILE_MODE;
    private Shader.TileMode mTileModeY = DEFAULT_TILE_MODE;

    public CircleRectImage(Context context) {
        super(context);
    }

    public CircleRectImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRectImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundSet, defStyle, 0);


        int roundtopleft = a.getInt(R.styleable.RoundSet_roundTopLeft,0);
        int roundtopright = a.getInt(R.styleable.RoundSet_roundTopRight,0);
        int roundbottomleft = a.getInt(R.styleable.RoundSet_roundBottomLeft,0);
        int roundbottomright = a.getInt(R.styleable.RoundSet_roundBottomRight,0);
        int roundall = a.getInt(R.styleable.RoundSet_roundAll,0);


        int index = a.getInt(R.styleable.RoundSet_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        mBorderWidth = a.getInt(R.styleable.RoundSet_border_width, 0);
        if (mBorderWidth < 0) {
            mBorderWidth = DEFAULT_BORDER_WIDTH;
        }

        mBorderColor = a.getColorStateList(R.styleable.RoundSet_border_color);
        if (mBorderColor == null) {
            mBorderColor = ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR);
        }

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);

        if (mMutateBackground) {
            super.setBackgroundDrawable(mBackgroundDrawable);
        }

        a.recycle();


        if(roundtopleft>100){
            roundtopleft = 100;
        }

        if(roundbottomleft>100){
            roundbottomleft = 100;
        }

        if(roundtopright>100){
            roundtopright = 100;
        }

        if(roundbottomright>100){
            roundbottomright = 100;
        }



        if(roundall > 0) {
            setCornerRadius(roundall);
        }else {
            setCornerRadius(roundtopleft, roundtopright, roundbottomleft, roundbottomright);
        }



    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ImageView.ScaleType scaleType) {
        assert scaleType != null;

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ImageView.ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = RoundDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = RoundDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(mDrawable);
        }
    }

    @Override public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) { return null; }

        Drawable d = null;

        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (Exception e) {
                Log.w(TAG, "Unable to find resource: " + mResource, e);
                mResource = 0;
            }
        }
        return RoundDrawable.fromDrawable(d);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        if (mBackgroundResource != resId) {
            mBackgroundResource = resId;
            mBackgroundDrawable = resolveBackgroundResource();
            setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackgroundDrawable = new ColorDrawable(color);
        setBackgroundDrawable(mBackgroundDrawable);
    }

    private Drawable resolveBackgroundResource() {
        Resources rsrc = getResources();
        if (rsrc == null) { return null; }

        Drawable d = null;

        if (mBackgroundResource != 0) {
            try {
                d = rsrc.getDrawable(mBackgroundResource);
            } catch (Exception e) {
                Log.w(TAG, "Unable to find resource: " + mBackgroundResource, e);
                // Don't try again.
                mBackgroundResource = 0;
            }
        }
        return RoundDrawable.fromDrawable(d);
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable, mScaleType);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (mMutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundDrawable.fromDrawable(mBackgroundDrawable);
            }
            updateAttrs(mBackgroundDrawable, ImageView.ScaleType.FIT_XY);
        }
    }

    @Override public void setColorFilter(ColorFilter cf) {
        if (mColorFilter != cf) {
            mColorFilter = cf;
            mHasColorFilter = true;
            mColorMod = true;
            applyColorMod();
            invalidate();
        }
    }

    private void applyColorMod() {
        if (mDrawable != null && mColorMod) {
            mDrawable = mDrawable.mutate();
            if (mHasColorFilter) {
                mDrawable.setColorFilter(mColorFilter);
            }
        }
    }

    private void updateAttrs(Drawable drawable, ImageView.ScaleType scaleType) {
        if (drawable == null) { return; }

        if (drawable instanceof RoundDrawable) {
            ((RoundDrawable) drawable)
                    .setScaleType(scaleType)
                    .setBorderWidth(mBorderWidth)
                    .setBorderColor(mBorderColor)
                    .setOval(mIsOval)
                    .setTileModeX(mTileModeX)
                    .setTileModeY(mTileModeY);

            if (mCornerRadii != null) {
                ((RoundDrawable) drawable).setCornerRadius(
                        mCornerRadii[CornerView.KIRI_ATAS],
                        mCornerRadii[CornerView.KANAN_ATAS],
                        mCornerRadii[CornerView.KANAN_BAWAH],
                        mCornerRadii[CornerView.KIRI_BAWAH]);
            }

            applyColorMod();
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i), scaleType);
            }
        }
    }

    @Override
    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
        updateBackgroundDrawableAttrs(true);
        //noinspection deprecation
        super.setBackgroundDrawable(mBackgroundDrawable);
    }

    /**
     * @return the largest corner radius.
     */
    public float getCornerRadius() {
        return getMaxCornerRadius();
    }

    /**
     * @return the largest corner radius.
     */
    public float getMaxCornerRadius() {
        float maxRadius = 0;
        for (float r : mCornerRadii) {
            maxRadius = Math.max(r, maxRadius);
        }
        return maxRadius;
    }
//
//    /**
//     * Get the corner radius of a specified corner.
//     *
//     * @param corner the corner.
//     * @return the radius.
//     */
//    public float getCornerRadius(@CornerView int corner) {
//        return mCornerRadii[corner];
//    }
//
//    /**
//     * Set all the corner radii from a dimension resource id.
//     *
//     * @param resId dimension resource id of radii.
//     */
//    public void setCornerRadiusDimen(@DimenRes int resId) {
//        float radius = getResources().getDimension(resId);
//        setCornerRadius(radius, radius, radius, radius);
//    }
//
//    /**
//     * Set the corner radius of a specific corner from a dimension resource id.
//     *
//     * @param corner the corner to set.
//     * @param resId the dimension resource id of the corner radius.
//     */
//    public void setCornerRadiusDimen(@CornerView int corner, @DimenRes int resId) {
//        setCornerRadius(corner, getResources().getDimensionPixelSize(resId));
//    }

    /**
     * Set the corner radii of all corners in px.
     *
     * @param radius the radius to set.
     */
    public void setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * Set the corner radius of a specific corner in px.
     *
     * @param corner the corner to set.
     * @param radius the corner radius to set in px.
     */
//    public void setCornerRadius(@CornerView int corner, float radius) {
//        if (mCornerRadii[corner] == radius) {
//            return;
//        }
//        mCornerRadii[corner] = radius;
//
//        updateDrawableAttrs();
//        updateBackgroundDrawableAttrs(false);
//        invalidate();
//    }

    /**
     * Set the corner radii of each corner individually. Currently only one unique nonzero value is
     * supported.
     *
     * @param topLeft radius of the top left corner in px.
     * @param topRight radius of the top right corner in px.
     * @param bottomRight radius of the bottom right corner in px.
     * @param bottomLeft radius of the bottom left corner in px.
     */
    public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (mCornerRadii[CornerView.KIRI_ATAS] == topLeft
                && mCornerRadii[CornerView.KANAN_ATAS] == topRight
                && mCornerRadii[CornerView.KANAN_BAWAH] == bottomRight
                && mCornerRadii[CornerView.KIRI_BAWAH] == bottomLeft) {
            return;
        }

        mCornerRadii[CornerView.KIRI_ATAS] = topLeft;
        mCornerRadii[CornerView.KANAN_ATAS] = topRight;
        mCornerRadii[CornerView.KIRI_BAWAH] = bottomLeft;
        mCornerRadii[CornerView.KANAN_BAWAH] = bottomRight;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(@DimenRes int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (mBorderWidth == width) { return; }

        mBorderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    @ColorInt
    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public void setBorderColor(@ColorInt int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public void setBorderColor(ColorStateList colors) {
        if (mBorderColor.equals(colors)) { return; }

        mBorderColor =
                (colors != null) ? colors : ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    public boolean isOval() {
        return mIsOval;
    }

    public void setOval(boolean oval) {
        mIsOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public void setTileModeX(Shader.TileMode tileModeX) {
        if (this.mTileModeX == tileModeX) { return; }

        this.mTileModeX = tileModeX;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public void setTileModeY(Shader.TileMode tileModeY) {
        if (this.mTileModeY == tileModeY) { return; }

        this.mTileModeY = tileModeY;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public boolean mutatesBackground() {
        return mMutateBackground;
    }

    public void mutateBackground(boolean mutate) {
        if (mMutateBackground == mutate) { return; }

        mMutateBackground = mutate;
        updateBackgroundDrawableAttrs(true);
        invalidate();
    }
}

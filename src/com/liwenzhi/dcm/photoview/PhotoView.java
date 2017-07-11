/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.liwenzhi.dcm.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.ImageView;

/**
 * A zoomable {@link android.widget.ImageView}. See {@link PhotoViewAttacher} for most of the details on how the zooming
 * is accomplished
 */
public class PhotoView extends ImageView {

    private PhotoViewAttacher attacher;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }

    // @TargetApi(21)
//    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();

//    }

    private void init() {
        attacher = new PhotoViewAttacher(this);
        //We always pose as a Matrix scale type, though we can change to another scale type
        //via the attacher
        super.setScaleType(ScaleType.MATRIX);
    }

    /**
     * Get the current {@link PhotoViewAttacher} for this indicatorview. Be wary of holding on to references
     * to this attacher, as it has a reference to this indicatorview, which, if a reference is held in the
     * wrong place, can cause memory leaks.
     *
     * @return the attacher.
     */
    public PhotoViewAttacher getAttacher() {
        return attacher;
    }

    /**
     * 获得缩放的比例大小
     *
     * @return
     */
    @Override
    public ScaleType getScaleType() {
        return attacher.getScaleType();
    }

    /**
     * 获得图片的矩阵，九个值，实现图片的缩放旋转等等效果
     *
     * @return
     */
    @Override
    public Matrix getImageMatrix() {
        return attacher.getImageMatrix();
    }

    /**
     * 设置长按监听
     *
     * @param l
     */
    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        attacher.setOnLongClickListener(l);
    }

    /**
     * 设置点击监听
     *
     * @param l
     */
    @Override
    public void setOnClickListener(OnClickListener l) {
        attacher.setOnClickListener(l);
    }

    /**
     * 设置缩放等级，1为原本大小   0.5表示一般，2便是2倍
     *
     * @param scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        if (attacher != null) {
            attacher.setScaleType(scaleType);
        }

    }

    /**
     * 给图片设置Drawable对象
     *
     * @param drawable
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        // setImageBitmap calls through to this method
        if (attacher != null)
            attacher.update();
    }

    /**
     * 给图片设置资源文件对象
     *
     * @param resId
     */
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (attacher != null)
            attacher.update();
    }

    /**
     * 给图片设置URI对象，可以是网络中的，也可以是本地File中的
     *
     * @param uri
     */
    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        attacher.update();
    }

    /**
     * 这种显示的大小？？
     *
     * @param l
     * @param t
     * @param r
     * @param b
     * @return
     */
    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            attacher.update();
        }
        return changed;
    }

    /**
     * 设置旋转的角度    ， setRotationTo来设置绝对旋转
     *
     * @param rotationDegree
     */
    public void setRotationTo(float rotationDegree) {
        attacher.setRotationTo(rotationDegree);
    }

    /**
     * 设置旋转的角度  ， setRotationBy来设置相对旋转
     *
     * @param rotationDegree
     */
    public void setRotationBy(float rotationDegree) {
        attacher.setRotationBy(rotationDegree);
    }

    /**
     * 是否改变了焦点
     *
     * @return
     */
    @Deprecated
    public boolean isZoomEnabled() {
        return attacher.isZoomEnabled();
    }

    /**
     * 是否可以变焦
     *
     * @return
     */
    public boolean isZoomable() {
        return attacher.isZoomable();
    }

    /**
     * 设置是否可以变焦
     *
     * @param zoomable
     */
    public void setZoomable(boolean zoomable) {
        attacher.setZoomable(zoomable);
    }

    /**
     * 获得显示的区域，上下左右
     *
     * @return
     */
    public RectF getDisplayRect() {
        return attacher.getDisplayRect();
    }

    /**
     * 获得显示的矩阵，9个值
     *
     * @param matrix
     */
    public void getDisplayMatrix(Matrix matrix) {
        attacher.getDisplayMatrix(matrix);
    }

    /**
     * 设置显示的矩阵，缩放，旋转等功能
     *
     * @param finalRectangle
     * @return
     */
    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return attacher.setDisplayMatrix(finalRectangle);
    }

    /**
     * 获得最小的缩放值
     *
     * @return
     */
    public float getMinimumScale() {
        return attacher.getMinimumScale();
    }

    public float getMediumScale() {
        return attacher.getMediumScale();
    }

    /**
     * 获得最大的缩放值
     *
     * @return
     */
    public float getMaximumScale() {
        return attacher.getMaximumScale();
    }

    /**
     * 获取缩放等级
     *
     * @return
     */
    public float getScale() {
        return attacher.getScale();
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        attacher.setAllowParentInterceptOnEdge(allow);
    }

    /**
     * 设置最小缩放等级
     *
     * @param minimumScale
     */
    public void setMinimumScale(float minimumScale) {
        attacher.setMinimumScale(minimumScale);
    }

    public void setMediumScale(float mediumScale) {
        attacher.setMediumScale(mediumScale);
    }

    /**
     * 设置最大缩放等级
     *
     * @param maximumScale
     */
    public void setMaximumScale(float maximumScale) {
        attacher.setMaximumScale(maximumScale);
    }

    /**
     * 设置缩放等级
     *
     * @param minimumScale 最小缩放值
     * @param mediumScale
     * @param maximumScale 最大缩放值
     */
    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
    }

    /**
     * 矩阵改变监听事件
     *
     * @param listener
     */
    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        attacher.setOnMatrixChangeListener(listener);
    }

    /**
     * 图片点击事件
     *
     * @param listener
     */
    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        attacher.setOnPhotoTapListener(listener);
    }

    /**
     * 图片外部点击监听
     *
     * @param listener
     */
    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
        attacher.setOnOutsidePhotoTapListener(listener);
    }

    /**
     * 设置缩放等级，1为原本大小   0.5表示一般，2便是2倍
     *
     * @param scale
     */
    public void setScale(float scale) {
        if (attacher != null) {
            attacher.setScale(scale);
        }

    }

    /**
     * 设置缩放等级，1为原本大小   0.5表示一般，2便是2倍
     * 设置是否显示动画效果
     *
     * @param scale
     * @param animate
     */
    public void setScale(float scale, boolean animate) {
        attacher.setScale(scale, animate);
    }

    /**
     * 设置缩放等级，
     *
     * @param scale   缩放的等级
     * @param focalX  x方向
     * @param focalY  y方向
     * @param animate 是否显示动画效果
     */
    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        attacher.setScale(scale, focalX, focalY, animate);
    }

    /**
     * 变焦过度的时间
     *
     * @param milliseconds
     */
    public void setZoomTransitionDuration(int milliseconds) {
        attacher.setZoomTransitionDuration(milliseconds);
    }

    /**
     * 设置双击的监听事件
     *
     * @param onDoubleTapListener
     */
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        attacher.setOnDoubleTapListener(onDoubleTapListener);
    }

    /**
     * 设置缩放等级改变的监听事件
     *
     * @param onScaleChangedListener
     */
    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
        attacher.setOnScaleChangeListener(onScaleChangedListener);
    }

    /**
     * 单个触摸的滑动事件
     *
     * @param onSingleFlingListener
     */
    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        attacher.setOnSingleFlingListener(onSingleFlingListener);
    }
}

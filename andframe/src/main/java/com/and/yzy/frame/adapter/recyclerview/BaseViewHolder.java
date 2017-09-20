/**
 * Copyright 2013 Joan Zapata
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
 */
package com.and.yzy.frame.adapter.recyclerview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashSet;
import java.util.LinkedHashSet;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * Views indexed with their IDs
     */
    private final SparseArray<View> views;

    public HashSet<Integer> getNestViews() {
        return nestViews;
    }

    private final HashSet<Integer> nestViews;

    private final LinkedHashSet<Integer> childClickViewIds;

    private final LinkedHashSet<Integer> itemChildLongClickViewIds;


    public View convertView;

    /**
     * Package private field to retain the associated user object and detect a change
     */
    Object associatedObject;


    public BaseViewHolder(View view) {
        super(view);
        this.views = new SparseArray<View>();
        this.childClickViewIds = new LinkedHashSet<>();
        this.itemChildLongClickViewIds = new LinkedHashSet<>();
        this.nestViews = new HashSet<>();
        convertView = view;

    }

    public HashSet<Integer> getItemChildLongClickViewIds() {
        return itemChildLongClickViewIds;
    }

    public HashSet<Integer> getChildClickViewIds() {
        return childClickViewIds;
    }

    public View getConvertView() {

        return convertView;
    }


    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setTextViewText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字体颜色
     *
     * @param viewId
     * @param
     * @return
     */
    public BaseViewHolder setTextViewTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    /**
     * 为EditText设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setEditText(int viewId, String text) {
        EditText view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为Button设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setButtonText(int viewId, String text) {
        Button view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为RadioButton设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setRadioBtnText(int viewId, String text) {
        RadioButton view = getView(viewId);
        view.setText(text);
        return this;
    }


    /**
     * 为ImageView设置图片,通过资源文件
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public BaseViewHolder setImageByResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为view设置背景
     *
     * @param viewId
     * @param
     * @param
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BaseViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {

        getView(viewId).setBackground(drawable);

        return this;
    }

    /**
     * 为view设置背景
     *
     * @param viewId
     * @param
     * @param
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BaseViewHolder setBackgroundColor(int viewId, int color) {

        getView(viewId).setBackgroundColor(color);

        return this;
    }

    /**
     * 为ImageView设置图片，通过bitmap
     *
     * @param viewId
     * @param bm
     * @return
     */
    public BaseViewHolder setImageByBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片，通过网络下载
     *
     * @param viewId
     * @param url
     * @return
     */
    public BaseViewHolder setImageByUrl(int viewId, String url) {

        Uri uri = Uri.parse(url);

        ((SimpleDraweeView) getView(viewId)).setImageURI(uri);
        return this;
    }


    /**
     * 给checkbox修改状态
     *
     * @param viewId
     * @param isChecked
     * @return
     */
    public BaseViewHolder setCheckBoxChecked(int viewId, boolean isChecked) {
        CheckBox cb = getView(viewId);
        cb.setChecked(isChecked);
        return this;
    }

    /**
     * 给Radiobtn修改状态
     *
     * @param viewId
     * @param isChecked
     * @return
     */
    public BaseViewHolder setRadioBtnChecked(int viewId, boolean isChecked) {
        RadioButton rb = getView(viewId);
        rb.setChecked(isChecked);
        return this;
    }

    /**
     * 给Edittext设置不可编辑
     *
     * @param viewId
     * @param
     * @return
     */
    public BaseViewHolder setEditTextNotEdit(int viewId) {
        EditText et = getView(viewId);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        // 设置光标隐藏
        et.setCursorVisible(false);
        return this;
    }

    /**
     * 给Edittext设置可编辑
     *
     * @param viewId
     * @param
     * @return
     */
    public BaseViewHolder setEditTextYesEdit(int viewId) {
        EditText et = getView(viewId);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        // 设置光标显示
        et.setCursorVisible(true);
        return this;
    }


    /**
     * 设置view的可见性
     *
     * @param viewId
     * @param Visibility
     * @return
     */
    public BaseViewHolder setViewVisibility(int viewId, int Visibility) {
        View view = getView(viewId);
        view.setVisibility(Visibility);
        return this;
    }


    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }



    /**
     * add childView id
     *
     * @param viewId add the child view id   can support childview click
     * @return
     */
    public BaseViewHolder addOnClickListener(int viewId) {
        childClickViewIds.add(viewId);
        return this;
    }

    /**
     * set nestview id
     *
     * @param viewId add the child view id   can support childview click
     * @return
     */
    public BaseViewHolder setNestView(int viewId) {
        addOnClickListener(viewId);
        addOnLongClickListener(viewId);
        nestViews.add(viewId);
        return this;
    }

    /**
     * add long click view id
     *
     * @param viewId
     * @return
     */
    public BaseViewHolder addOnLongClickListener(int viewId) {
        itemChildLongClickViewIds.add(viewId);
        return this;
    }


    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item on click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item long click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item selected click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    /**
     * Sets the on checked change listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The checked change listener of compound button.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        // View unable cast to Checkable
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setAdapter(int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Retrieves the last converted object on this view.
     */
    public Object getAssociatedObject() {
        return associatedObject;
    }

    /**
     * Should be called during convert
     */
    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }
}

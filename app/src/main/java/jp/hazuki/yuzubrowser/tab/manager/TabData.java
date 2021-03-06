/*
 * Copyright (c) 2017 Hazuki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.hazuki.yuzubrowser.tab.manager;

import android.graphics.Bitmap;

import jp.hazuki.yuzubrowser.webkit.CustomWebView;
import jp.hazuki.yuzubrowser.webkit.TabType;

public class TabData {
    protected static final int STATE_LOADING = 0x001;

    public final CustomWebView mWebView;
    private TabIndexData mIndexData;
    public int mProgress = -1;

    protected int mState;

    public TabData(CustomWebView web) {
        this(web, new TabIndexData());
        mIndexData.setId(web.getIdentityId());
    }

    public TabData(CustomWebView web, TabIndexData data) {
        mWebView = web;
        mIndexData = data;
    }

    public boolean equals(CustomWebView web) {
        return mWebView.equals(web);
    }

    public int getTabType() {
        return mIndexData.getTabType();
    }

    public void setTabType(@TabType int type) {
        mIndexData.setTabType(type);
    }

    public long getParent() {
        return mIndexData.getParent();
    }

    public void setParent(long parent) {
        mIndexData.setParent(parent);
    }

    public String getOriginalUrl() {
        return mIndexData.getOriginalUrl();
    }

    public String getUrl() {
        return mIndexData.getUrl();
    }

    public void setUrl(String url) {
        mIndexData.setUrl(url);
    }

    public String getTitle() {
        return mIndexData.getTitle();
    }

    public void setTitle(String title) {
        mIndexData.setTitle(title);
    }

    public TabIndexData getTabIndexData() {
        return mIndexData;
    }

    public long getId() {
        return mIndexData.getId();
    }

    public boolean isInPageLoad() {
        return (mState & STATE_LOADING) != 0;
    }

    public void setInPageLoad(boolean b) {
        if (b)
            mState |= STATE_LOADING;
        else
            mState &= ~STATE_LOADING;
    }

    public boolean isNavLock() {
        return mIndexData.isNavLock();
    }

    public void setNavLock(boolean b) {
        mIndexData.setNavLock(b);
    }

    public void onPageStarted(String url, Bitmap favicon) {
        mState |= STATE_LOADING;
        mProgress = 0;
        mIndexData.setUrl(url);
        mIndexData.setOriginalUrl(url);
        mIndexData.setTitle(null);
    }

    public void onPageFinished(CustomWebView web, String url) {
        mState &= ~STATE_LOADING;//moved from onProgressChanged
        mIndexData.setUrl(web.getUrl());
        mIndexData.setOriginalUrl(web.getOriginalUrl());
        mIndexData.setTitle(web.getTitle());
    }

    public void onReceivedTitle(String title) {
        mIndexData.setTitle(title);
    }

    public void onStateChanged(TabData tabdata) {
        mProgress = tabdata.mProgress;
        mIndexData.setTitle(tabdata.getTitle());
        mIndexData.setUrl(tabdata.getUrl());
        mIndexData.setOriginalUrl(tabdata.getOriginalUrl());
        setInPageLoad(tabdata.isInPageLoad());
    }

    public void onProgressChanged(int newProgress) {
        mProgress = newProgress;
    }

    public int getProgress() {
        return mProgress;
    }

    public void onStartPage() {
        mIndexData.setShotThumbnail(false);
    }

    public boolean isShotThumbnail() {
        return mIndexData.isShotThumbnail();
    }

    public void shotThumbnail(Bitmap thumbnail) {
        mIndexData.setThumbnail(thumbnail);
        mIndexData.setShotThumbnail(true);
    }

    public boolean isPinning() {
        return mIndexData.isPinning();
    }

    public void setPinning(boolean pinning) {
        mIndexData.setPinning(pinning);
    }
}

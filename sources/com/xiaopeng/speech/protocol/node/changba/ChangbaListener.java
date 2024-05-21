package com.xiaopeng.speech.protocol.node.changba;

import com.xiaopeng.speech.INodeListener;
import com.xiaopeng.speech.protocol.node.changba.bean.ChangbaBean;
/* loaded from: classes.dex */
public interface ChangbaListener extends INodeListener {
    void onMusicChangbaSearch(ChangbaBean changbaBean);
}

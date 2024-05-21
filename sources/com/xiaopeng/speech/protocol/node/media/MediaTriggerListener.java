package com.xiaopeng.speech.protocol.node.media;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface MediaTriggerListener extends INodeListener {
    default void onSubmit() {
    }

    default void onCancel() {
    }
}

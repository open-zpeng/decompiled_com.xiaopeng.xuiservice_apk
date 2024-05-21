package com.xiaopeng.xui.widget.popPanelView;

import android.view.View;
import com.xiaopeng.xui.vui.VuiViewScene;
/* loaded from: classes5.dex */
public class XPopPanelView extends VuiViewScene {
    private IPopPanelViewInterface mPopPanelViewInterface;

    /* loaded from: classes5.dex */
    public interface IPopPanelViewInterface {
        int getDisplayScreenId();

        void onVuiEventExecuted();
    }

    public XPopPanelView(View contextView) {
        setVuiView(contextView);
    }

    @Override // com.xiaopeng.xui.vui.VuiViewScene
    public void onBuildScenePrepare() {
    }

    @Override // com.xiaopeng.xui.vui.IVuiViewScene
    public int getVuiDisplayLocation() {
        IPopPanelViewInterface iPopPanelViewInterface = this.mPopPanelViewInterface;
        if (iPopPanelViewInterface != null) {
            return iPopPanelViewInterface.getDisplayScreenId() - 1;
        }
        return 0;
    }

    public void setPopPanelViewInterface(IPopPanelViewInterface popPanelViewInterface) {
        this.mPopPanelViewInterface = popPanelViewInterface;
    }

    @Override // com.xiaopeng.vui.commons.IVuiSceneListener
    public void onVuiEventExecutioned() {
        IPopPanelViewInterface iPopPanelViewInterface = this.mPopPanelViewInterface;
        if (iPopPanelViewInterface != null) {
            iPopPanelViewInterface.onVuiEventExecuted();
        }
    }
}
package com.xiaopeng.xuiservice.smart.action.impl;

import android.text.TextUtils;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuiservice.egg.EggLog;
import com.xiaopeng.xuiservice.operation.OperationHalService;
import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import com.xiaopeng.xuiservice.operation.resource.AddResourceInfo;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.utils.DateTimeFormatUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.Date;
/* loaded from: classes5.dex */
public class ActionResource extends ActionBase implements OperationHalService.OperationHalListener {
    private final String description;
    private final long effectTime;
    private final long expireTime;
    private final String extraData;
    private final String file;
    private final String price;
    private final String resourceIcon;
    private final String resourceId;
    private final String resourceName;
    private final int resourceType;
    private final boolean select;

    public ActionResource(String resourceId, int resourceType, String resourceName, long effectTime, long expireTime, String extraData, String file, String resourceIcon, String description, String price, boolean select) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.effectTime = effectTime;
        this.expireTime = expireTime;
        this.extraData = extraData;
        this.file = file;
        this.resourceIcon = resourceIcon;
        this.description = description;
        this.price = price;
        this.select = select;
        if (TextUtils.isEmpty(resourceId)) {
            throw new IllegalArgumentException("no resource id");
        }
        if (resourceType <= 0) {
            throw new IllegalArgumentException("invalid resource type: " + resourceType);
        } else if (TextUtils.isEmpty(file)) {
            throw new IllegalArgumentException("no resource file");
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        ResourceInfo resourceInfo = OperationHalService.getInstance().getResourceById(this.resourceId);
        if (resourceInfo != null) {
            EggLog.INFO("resource already added, " + this);
            XuiWorkHandler.getInstance().post(new $$Lambda$J0uPGal7TH8uVd0MRHrtCVqCF94(this));
            return;
        }
        long j = this.expireTime;
        if (j > 0 && j < System.currentTimeMillis()) {
            EggLog.INFO("resource expired, won't be added to resource service, " + this);
            XuiWorkHandler.getInstance().post(new $$Lambda$J0uPGal7TH8uVd0MRHrtCVqCF94(this));
            return;
        }
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionResource$lMkPAOUZqL_hGAlpQYRdteXllLs
            @Override // java.lang.Runnable
            public final void run() {
                ActionResource.this.lambda$onStart$0$ActionResource();
            }
        });
    }

    public /* synthetic */ void lambda$onStart$0$ActionResource() {
        OperationHalService.getInstance().addEventListener(this);
        AddResourceInfo addResourceInfo = new AddResourceInfo();
        addResourceInfo.id = this.resourceId;
        addResourceInfo.type = this.resourceType;
        addResourceInfo.rsc_name = this.resourceName;
        addResourceInfo.rsc_path = this.file;
        addResourceInfo.extraData = this.extraData;
        addResourceInfo.sourceType = "egg_resource";
        addResourceInfo.effectTime = this.effectTime;
        addResourceInfo.expireTime = this.expireTime;
        addResourceInfo.createTime = System.currentTimeMillis();
        addResourceInfo.updateTime = addResourceInfo.createTime;
        addResourceInfo.resourceIcon = this.resourceIcon;
        addResourceInfo.description = this.description;
        addResourceInfo.price = this.price;
        OperationHalService.getInstance().addNewResource(addResourceInfo);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionResource$DIrcG0oRF0fXdHpysGSmyKBM_D4
            @Override // java.lang.Runnable
            public final void run() {
                ActionResource.this.lambda$onStop$1$ActionResource();
            }
        });
    }

    public /* synthetic */ void lambda$onStop$1$ActionResource() {
        OperationHalService.getInstance().removeEventListener(this);
    }

    @Override // com.xiaopeng.xuiservice.operation.OperationHalService.OperationHalListener
    public void onEvent(int code, String id, int type, String event) {
        if (!this.resourceId.equals(id)) {
            return;
        }
        if (code == 1) {
            EggLog.INFO("add resource success, " + this);
            if (this.select) {
                OperationResource operationResource = new OperationResource();
                operationResource.setId(id);
                operationResource.setResourceType(type);
                OperationHalService.getInstance().selectOperationResource(operationResource);
                return;
            }
            XuiWorkHandler.getInstance().post(new $$Lambda$J0uPGal7TH8uVd0MRHrtCVqCF94(this));
        } else if (code != 4) {
            if (code == 6) {
                EggLog.INFO("no enough disk space, action failed, " + this);
                XuiWorkHandler.getInstance().post(new $$Lambda$J0uPGal7TH8uVd0MRHrtCVqCF94(this));
            }
        } else if (this.select) {
            EggLog.INFO("resource set success, " + this);
            XuiWorkHandler.getInstance().post(new $$Lambda$J0uPGal7TH8uVd0MRHrtCVqCF94(this));
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.OperationHalService.OperationHalListener
    public void onError(int errorCode, int operation) {
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{resourceType='" + this.resourceType + "', resourceName='" + this.resourceName + "', effectTime=" + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(this.effectTime)) + ", expireTime=" + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(this.expireTime)) + ", extraData='" + this.extraData + "', file='" + this.file + "', select=" + this.select + '}';
    }
}

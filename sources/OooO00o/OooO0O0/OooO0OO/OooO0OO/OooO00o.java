package OooO00o.OooO0O0.OooO0Oo.OooO0OO;

import OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libserver.version1.entity.MachineInfoConfig;
import com.loostone.libserver.version1.entity.NetUrlConfig;
import com.loostone.libserver.version1.entity.request.config.RequestMicLimitInfo;
import com.loostone.libserver.version1.entity.request.statistical.RequestStatisticalMic;
import com.loostone.libserver.version1.entity.response.ResponseData;
import com.loostone.libserver.version1.entity.response.config.MicLimitInfo;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o implements OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0 {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final Context f248OooO00o;
    @NotNull
    public final Lazy OooO0O0;
    @NotNull
    public final Lazy OooO0OO;

    /* loaded from: classes.dex */
    public static final class OooO extends Lambda implements Function0<OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0Oo.OooO00o> {
        public OooO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0Oo.OooO00o invoke() {
            return new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0Oo.OooO00o(OooO00o.this.f248OooO00o, "REFRESH_DATA_TAG");
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0007OooO00o extends Lambda implements Function0<OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO00o.OooO00o> {
        public C0007OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO00o.OooO00o invoke() {
            return new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO00o.OooO00o(OooO00o.this.f248OooO00o, "APP_UPDATE_TAG");
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oo.OooO0OO> {
        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oo.OooO0OO invoke() {
            return new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oo.OooO0OO(OooO00o.this.f248OooO00o, "AUDIO_EFFECT_TAG");
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends Lambda implements Function0<OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o0.OooO00o> {
        public OooO0OO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o0.OooO00o invoke() {
            return new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o0.OooO00o(OooO00o.this.f248OooO00o, "MIC_VERIFICATION_TAG");
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0 extends Lambda implements Function0<OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o.OooO00o> {
        public OooOO0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o.OooO00o invoke() {
            return new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o.OooO00o(OooO00o.this.f248OooO00o, "UPLOAD_MIC_INFO_TAG");
        }
    }

    public OooO00o(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.f248OooO00o = context;
        LazyKt.lazy(new C0007OooO00o());
        this.OooO0O0 = LazyKt.lazy(new OooO0OO());
        this.OooO0OO = LazyKt.lazy(new OooOO0());
        LazyKt.lazy(new OooO());
        LazyKt.lazy(new OooO0O0());
    }

    @Override // OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0
    public void OooO00o(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDongleDevice, @NotNull OooOO0O<Boolean> listener) {
        Intrinsics.checkNotNullParameter(cachedDongleDevice, "cachedDongleDevice");
        Intrinsics.checkNotNullParameter(listener, "listener");
        OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o0.OooO00o oooO00o = (OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o0.OooO00o) this.OooO0O0.getValue();
        OooO0o oooO0o = new OooO0o(listener);
        oooO00o.getClass();
        RequestMicLimitInfo requestMicLimitInfo = new RequestMicLimitInfo();
        requestMicLimitInfo.setHidResult(1);
        requestMicLimitInfo.setHidSn(cachedDongleDevice.OooO);
        requestMicLimitInfo.setHidVersion(cachedDongleDevice.OooOO0);
        requestMicLimitInfo.setSerial(cachedDongleDevice.OooO0oo);
        requestMicLimitInfo.setVid(cachedDongleDevice.OooO0Oo);
        requestMicLimitInfo.setPid(cachedDongleDevice.OooO0o0);
        requestMicLimitInfo.setVendor(cachedDongleDevice.OooO0o);
        requestMicLimitInfo.setDeviceName(cachedDongleDevice.OooO0oO);
        oooO00o.OooO00o(NetUrlConfig.getAddress(2), requestMicLimitInfo, oooO0o);
    }

    @Override // OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0
    public void OooO00o(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDongleDevice) {
        Intrinsics.checkNotNullParameter(cachedDongleDevice, "cachedDongleDevice");
        OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o.OooO00o oooO00o = (OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0o.OooO00o) this.OooO0OO.getValue();
        UsbDevice usbDevice = cachedDongleDevice.f228OooO00o;
        String packageName = this.f248OooO00o.getPackageName();
        oooO00o.getClass();
        RequestStatisticalMic requestStatisticalMic = new RequestStatisticalMic();
        requestStatisticalMic.setVid(usbDevice.getVendorId() + "");
        requestStatisticalMic.setPid(usbDevice.getProductId() + "");
        requestStatisticalMic.setVendor(usbDevice.getManufacturerName());
        requestStatisticalMic.setDeviceName(usbDevice.getProductName());
        requestStatisticalMic.setSerial(usbDevice.getSerialNumber());
        requestStatisticalMic.setPackageName(packageName);
        requestStatisticalMic.setBrand(MachineInfoConfig.getBrand());
        requestStatisticalMic.setModel(MachineInfoConfig.getModel());
        oooO00o.OooO00o(NetUrlConfig.getAddress(3), requestStatisticalMic, null);
    }

    /* loaded from: classes.dex */
    public static final class OooO0o implements OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o<MicLimitInfo> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooOO0O<Boolean> f253OooO00o;

        public OooO0o(OooOO0O<Boolean> oooOO0O) {
            this.f253OooO00o = oooOO0O;
        }

        @Override // OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o
        public void OooO00o(@Nullable ResponseData<MicLimitInfo> responseData) {
            if (responseData.isSuccess()) {
                Intrinsics.checkNotNullParameter("NetworkVersion1", Progress.TAG);
                Intrinsics.checkNotNullParameter("micVerification() onSuccess", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                    Log.i("LogTuning", "NetworkVersion1 -> micVerification() onSuccess");
                }
                this.f253OooO00o.onSuccess(Boolean.valueOf(responseData.getData().getState() == 1));
                return;
            }
            Intrinsics.checkNotNullParameter("NetworkVersion1", Progress.TAG);
            Intrinsics.checkNotNullParameter("micVerification() onSuccess but no data", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "NetworkVersion1 -> micVerification() onSuccess but no data");
            }
            this.f253OooO00o.onError();
        }

        @Override // OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o
        public void OooO00o(@Nullable String str) {
            String msg = Intrinsics.stringPlus("micVerification() onError ------> ", str);
            Intrinsics.checkNotNullParameter("NetworkVersion1", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "NetworkVersion1 -> " + msg);
            }
            this.f253OooO00o.onError();
        }
    }
}

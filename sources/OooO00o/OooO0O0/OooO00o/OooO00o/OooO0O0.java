package OooO00o.OooO0O0.OooO00o.OooO00o;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.loostone.libtuning.channel.skyworth.old.ai.AiControlMgr;
import com.loostone.libtuning.channel.skyworth.old.record.RecordMgr;
import com.loostone.libtuning.component.service.PmKaraokeVoiceService;
/* loaded from: classes.dex */
public abstract class OooO0O0 extends Binder implements IInterface {
    public OooO0O0() {
        attachInterface(this, "com.loostone.karaoke.voice.IPmKaraokeVoiceService");
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1) {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeVoiceService");
            byte[] createByteArray = parcel.createByteArray();
            int read = RecordMgr.getInstance().read(createByteArray, parcel.readInt(), parcel.readInt());
            parcel2.writeNoException();
            parcel2.writeInt(read);
            parcel2.writeByteArray(createByteArray);
            return true;
        } else if (i == 2) {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeVoiceService");
            int sendCmd = AiControlMgr.getInstance().sendCmd(parcel.readString());
            parcel2.writeNoException();
            parcel2.writeInt(sendCmd);
            return true;
        } else if (i != 3) {
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString("com.loostone.karaoke.voice.IPmKaraokeVoiceService");
            return true;
        } else {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeVoiceService");
            int i3 = ((BaseChannelMgr) PmKaraokeVoiceService.this.f607OooO00o.getValue()).getVerifyStatus() == 2 ? 1 : 0;
            parcel2.writeNoException();
            parcel2.writeInt(i3);
            return true;
        }
    }
}

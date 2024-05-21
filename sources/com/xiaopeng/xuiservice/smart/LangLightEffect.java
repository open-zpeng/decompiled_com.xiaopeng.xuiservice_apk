package com.xiaopeng.xuiservice.smart;
/* loaded from: classes5.dex */
public class LangLightEffect {
    private int effect_id;
    private String effect_name;
    private String effect_type;
    private Front_Effect front_effect;
    private int notResetFlag;
    private Rear_Effect rear_effect;
    private int special_duration;
    private int special_mode;

    public String getEffect_name() {
        return this.effect_name;
    }

    public void setEffect_name(String effect_name) {
        this.effect_name = effect_name;
    }

    public String getEffect_type() {
        return this.effect_type;
    }

    public void setEffect_type(String effect_type) {
        this.effect_type = effect_type;
    }

    public int getEffect_id() {
        return this.effect_id;
    }

    public void setEffect_id(int effect_id) {
        this.effect_id = effect_id;
    }

    public int getSpecial_mode() {
        return this.special_mode;
    }

    public void setSpecial_mode(int special_mode) {
        this.special_mode = special_mode;
    }

    public int getSpecial_duration() {
        return this.special_duration;
    }

    public void setSpecial_duration(int special_duration) {
        this.special_duration = special_duration;
    }

    public int getNotResetFlag() {
        return this.notResetFlag;
    }

    public void setNotResetFlag(int notResetFlag) {
        this.notResetFlag = notResetFlag;
    }

    public void Front_Effect(Front_Effect front_effect) {
        this.front_effect = front_effect;
    }

    public Front_Effect getFront_Effect() {
        return this.front_effect;
    }

    public Rear_Effect getRear_Effect() {
        return this.rear_effect;
    }

    public void setRear_Effect(Rear_Effect rear_effect) {
        this.rear_effect = rear_effect;
    }

    /* loaded from: classes5.dex */
    public static class Rear_Effect {
        private int[] data;
        private int loop;
        private int period;
        private int retain;
        private String[] sdata;

        public int getPeriod() {
            return this.period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getLoop() {
            return this.loop;
        }

        public void setLoop(int loop) {
            this.loop = loop;
        }

        public int getRetain() {
            return this.retain;
        }

        public void setRetain(int retain) {
            this.retain = retain;
        }

        public int[] getData() {
            return this.data;
        }

        public void setData(int[] data) {
            this.data = data;
        }

        public String[] getSdata() {
            return this.sdata;
        }

        public void setSdata(String[] sdata) {
            this.sdata = sdata;
        }
    }

    /* loaded from: classes5.dex */
    public static class Front_Effect {
        private int[] data;
        private int loop;
        private int period;
        private int retain;
        private String[] sdata;

        public int getPeriod() {
            return this.period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getLoop() {
            return this.loop;
        }

        public void setLoop(int loop) {
            this.loop = loop;
        }

        public int getRetain() {
            return this.retain;
        }

        public void setRetain(int retain) {
            this.retain = retain;
        }

        public int[] getData() {
            return this.data;
        }

        public void setData(int[] data) {
            this.data = data;
        }

        public String[] getSdata() {
            return this.sdata;
        }

        public void setSdata(String[] sdata) {
            this.sdata = sdata;
        }
    }
}

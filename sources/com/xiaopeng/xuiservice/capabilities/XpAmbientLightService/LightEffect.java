package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import java.util.List;
/* loaded from: classes5.dex */
public class LightEffect {
    private List<Effect_init> effect_init;
    private String effect_name;
    private int effect_name_num;
    private String effect_name_show;
    private List<Effect_period> effect_period;
    private String effect_type;
    private int effect_type_num;
    private String effect_type_show;
    private String version;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setEffect_type(String effect_type) {
        this.effect_type = effect_type;
    }

    public String getEffect_type() {
        return this.effect_type;
    }

    public void setEffect_type_show(String effect_type_show) {
        this.effect_type_show = effect_type_show;
    }

    public String getEffect_type_show() {
        return this.effect_type_show;
    }

    public void setEffect_type_num(int effect_type_num) {
        this.effect_type_num = effect_type_num;
    }

    public int getEffect_type_num() {
        return this.effect_type_num;
    }

    public void setEffect_name(String effect_name) {
        this.effect_name = effect_name;
    }

    public String getEffect_name() {
        return this.effect_name;
    }

    public void setEffect_name_show(String effect_name_show) {
        this.effect_name_show = effect_name_show;
    }

    public String getEffect_name_show() {
        return this.effect_name_show;
    }

    public void setEffect_name_num(int effect_name_num) {
        this.effect_name_num = effect_name_num;
    }

    public int getEffect_name_num() {
        return this.effect_name_num;
    }

    public void setEffect_init(List<Effect_init> effect_init) {
        this.effect_init = effect_init;
    }

    public List<Effect_init> getEffect_init() {
        return this.effect_init;
    }

    public void setEffect_period(List<Effect_period> effect_period) {
        this.effect_period = effect_period;
    }

    public List<Effect_period> getEffect_period() {
        return this.effect_period;
    }

    /* loaded from: classes5.dex */
    public class Effect_init {
        private int bright;
        private int color;
        private int fade;

        public Effect_init() {
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }

        public void setBright(int bright) {
            this.bright = bright;
        }

        public int getBright() {
            return this.bright;
        }

        public void setFade(int fade) {
            this.fade = fade;
        }

        public int getFade() {
            return this.fade;
        }
    }

    /* loaded from: classes5.dex */
    public class Effect_period {
        private int bright;
        private int color;
        private boolean colorswitch;
        private int fade;

        public Effect_period() {
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }

        public void setBright(int bright) {
            this.bright = bright;
        }

        public int getBright() {
            return this.bright;
        }

        public void setFade(int fade) {
            this.fade = fade;
        }

        public int getFade() {
            return this.fade;
        }

        public void setColorswitch(boolean colorswitch) {
            this.colorswitch = colorswitch;
        }

        public boolean getColorswitch() {
            return this.colorswitch;
        }
    }
}

package it.nerr.wolframalpha4j.types;

import it.nerr.simpleapi.request.WebRequest;

public class Miscellaneous {

    private boolean reinterpret;
    private boolean useReinterpret;
    private boolean translation;
    private boolean useTranslation;
    private boolean ignorecase;
    private boolean useIgnorecase;
    private String sig;
    private boolean useSig;
    private String assumption;
    private boolean useAssumption;
    private String podstate;
    private boolean usePodstate;
    private Units units;
    private boolean useUnits;

    private Miscellaneous(Builder builder) {
        this.reinterpret = builder.reinterpret;
        this.useReinterpret = builder.useReinterpret;
        this.translation = builder.translation;
        this.useTranslation = builder.useTranslation;
        this.ignorecase = builder.ignorecase;
        this.useIgnorecase = builder.useIgnorecase;
        this.sig = builder.sig;
        this.useSig = builder.useSig;
        this.assumption = builder.assumption;
        this.useAssumption = builder.useAssumption;
        this.podstate = builder.podstate;
        this.usePodstate = builder.usePodstate;
        this.units = builder.units;
        this.useUnits = builder.useUnits;
    }

    public WebRequest addParameters(WebRequest webRequest) {
        if (useReinterpret) {
            webRequest = webRequest.query("reinterpret", Boolean.toString(reinterpret));
        }
        if (useTranslation) {
            webRequest = webRequest.query("translation", Boolean.toString(translation));
        }
        if (useIgnorecase) {
            webRequest = webRequest.query("ignorecase", Boolean.toString(ignorecase));
        }
        if (useSig) {
            webRequest = webRequest.query("sig", sig);
        }
        if (useAssumption) {
            webRequest = webRequest.query("assumption", assumption);
        }
        if (usePodstate) {
            webRequest = webRequest.query("podstate", podstate);
        }
        if (useUnits) {
            webRequest = webRequest.query("units", units.toString());
        }
        return webRequest;
    }

    public static class Builder {

            private boolean reinterpret;
            private boolean useReinterpret;
            private boolean translation;
            private boolean useTranslation;
            private boolean ignorecase;
            private boolean useIgnorecase;
            private String sig;
            private boolean useSig;
            private String assumption;
            private boolean useAssumption;
            private String podstate;
            private boolean usePodstate;
            private Units units;
            private boolean useUnits;

            public Builder reinterpret(boolean reinterpret) {
                this.reinterpret = reinterpret;
                this.useReinterpret = true;
                return this;
            }

            public Builder translation(boolean translation) {
                this.translation = translation;
                this.useTranslation = true;
                return this;
            }

            public Builder ignorecase(boolean ignorecase) {
                this.ignorecase = ignorecase;
                this.useIgnorecase = true;
                return this;
            }

            public Builder sig(String sig) {
                this.sig = sig;
                this.useSig = true;
                return this;
            }

            public Builder assumption(String assumption) {
                this.assumption = assumption;
                this.useAssumption = true;
                return this;
            }

            public Builder podstate(String podstate) {
                this.podstate = podstate;
                this.usePodstate = true;
                return this;
            }

            public Builder units(Units units) {
                this.units = units;
                this.useUnits = true;
                return this;
            }

            public Miscellaneous build() {
                return new Miscellaneous(this);
            }
    }

}

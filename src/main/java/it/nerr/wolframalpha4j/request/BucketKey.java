package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.util.RouteUtils;
import it.nerr.wolframalpha4j.route.Routes;
import reactor.util.annotation.Nullable;

import java.util.Objects;

public final class BucketKey {

    private final String uriTemplate;
    @Nullable
    private final String majorParam;

    private BucketKey(String uriTemplate, String completeUri) {
        this.uriTemplate = uriTemplate;
        this.majorParam = RouteUtils.getMajorParam(uriTemplate, completeUri);
    }

    public static BucketKey of(String uriTemplate, String completeUri) {
        return new BucketKey(uriTemplate, completeUri);
    }

    public static BucketKey of(WebRequest request) {
        return BucketKey.of(request.getRoute().getUriTemplate(), request.getCompleteUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(uriTemplate, majorParam);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BucketKey bucket = (BucketKey) obj;

        return uriTemplate.equals(bucket.uriTemplate) && Objects.equals(majorParam, bucket.majorParam);
    }

    @Override
    public String toString() {
        return Integer.toHexString(hashCode());
    }
}

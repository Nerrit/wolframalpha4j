package it.nerr.wolframalpha4j.util;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipartRequest<T> {

    private final T jsonPayload;
    private final List<Tuple2<String, InputStream>> files;

    private MultipartRequest(T jsonPayload, List<Tuple2<String, InputStream>> files) {
        this.jsonPayload = jsonPayload;
        this.files = Collections.unmodifiableList(files);
    }

    public static <T> MultipartRequest<T> ofRequest(T body) {
        return new MultipartRequest<>(body, Collections.emptyList());
    }

    public static <T> MultipartRequest<T> ofRequestAndFiles(T body, List<Tuple2<String, InputStream>> files) {
        return new MultipartRequest<>(body, files);
    }

    public <R> MultipartRequest<R> withRequest(R body) {
        return new MultipartRequest<>(body, files);
    }

    public MultipartRequest<T> addFile(String fileName, InputStream file) {
        List<Tuple2<String, InputStream>> list = new ArrayList<>(this.files);
        list.add(Tuples.of(fileName, file));
        return new MultipartRequest<>(this.jsonPayload, Collections.unmodifiableList(list));
    }

    public MultipartRequest<T> addFiles(List<Tuple2<String, InputStream>> filesList) {
        List<Tuple2<String, InputStream>> list = new ArrayList<>(this.files);
        list.addAll(filesList);
        return new MultipartRequest<>(this.jsonPayload, Collections.unmodifiableList(list));
    }

    public T getJsonPayload() {
        return jsonPayload;
    }

    public List<Tuple2<String, InputStream>> getFiles() {
        return files;
    }
}

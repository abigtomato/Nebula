package org.abigtomato.nebula.http.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class CommonRespAdapter extends CallAdapter.Factory {

    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type type, @NotNull Annotation[] annotations,
                                 @NotNull Retrofit retrofit) {
        return null;
    }
}

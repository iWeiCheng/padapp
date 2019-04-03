//package com.xjysx.xiaochetong.util.svgUtil;
//
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import com.bumptech.glide.load.Key;
//import com.bumptech.glide.load.Options;
//import com.bumptech.glide.load.model.ModelLoader;
//import com.bumptech.glide.signature.ObjectKey;
//
//import java.io.InputStream;
//
//public class MTSvgModelLoader implements ModelLoader<MTSVGItem, InputStream> {
//    @Nullable
//    @Override
//    public ModelLoader.LoadData<InputStream> buildLoadData(@NonNull MTSVGItem mtigqsvgItem, int width, int height, @NonNull Options options) {
//        Key diskCacheKey = new ObjectKey(mtigqsvgItem.getFullSVG());
//        return new LoadData<>(diskCacheKey, new MTSvgFetcher(mtigqsvgItem.getFullSVG()));
//    }
//
//    @Override
//    public boolean handles(@NonNull MTSVGItem mtigqsvgItem) {
//        return true;
//
//    }
//}
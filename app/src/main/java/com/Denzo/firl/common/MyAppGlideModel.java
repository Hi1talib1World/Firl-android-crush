package com.Denzo.firl.common;

@GlideModule
public class MyAppGlideModel extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return super.isManifestParsingEnabled();
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, AppConfig.IMAGE_MAX_CACHE_SIZE));
        RequestOptions requestOptions = RequestOptions.placeholderOf(R.mipmap.logo);
        builder.setDefaultRequestOptions(requestOptions);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }

}
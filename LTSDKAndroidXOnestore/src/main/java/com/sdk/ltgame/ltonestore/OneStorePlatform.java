package com.sdk.ltgame.ltonestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sdk.ltgame.core.common.LTGameOptions;
import com.sdk.ltgame.core.common.LTGameSdk;
import com.sdk.ltgame.core.common.Target;
import com.sdk.ltgame.core.impl.OnRechargeListener;
import com.sdk.ltgame.core.model.RechargeObject;
import com.sdk.ltgame.core.platform.AbsPlatform;
import com.sdk.ltgame.core.platform.IPlatform;
import com.sdk.ltgame.core.platform.PlatformFactory;
import com.sdk.ltgame.core.uikit.BaseActionActivity;
import com.sdk.ltgame.core.util.LTGameUtil;
import com.sdk.ltgame.ltonestore.uikit.OneStoreActivity;

import java.util.Map;

public class OneStorePlatform extends AbsPlatform {

    private OneStoreHelper mHelper;

    private OneStorePlatform(Context context, String appId, String appKey, int payTest, String publicKey,
                             int selfRequestCode, String sku, String productID, Map<String, Object> params,
                             String payType, int target) {
        super(context, appId, appKey, payTest, publicKey, selfRequestCode, sku, productID, params, payType,
                target);
    }

    @Override
    public void recharge(Activity activity, int target, RechargeObject object, OnRechargeListener listener) {
        mHelper = new OneStoreHelper(activity, object.getPublicKey(), object.getPayTest(), object.getSku(),
                object.getGoodsID(), object.getmGoodsType(), object.getSelfRequestCode(), object.getParams(), listener);
        mHelper.initOneStore(listener);
    }

    @Override
    public void onActivityResult(BaseActionActivity activity, int requestCode, int resultCode, Intent data) {
        mHelper.onActivityResult(requestCode, resultCode, data, mHelper.mRequestCode);
    }

    @Override
    public Class getUIKitClazz() {
        return OneStoreActivity.class;
    }

    @Override
    public void recycle() {
        if (mHelper != null) {
            mHelper.release();
        }
    }

    /**
     * 工厂
     */
    public static class Factory implements PlatformFactory {

        @Override
        public IPlatform create(Context context, int target) {
            IPlatform platform = null;
            LTGameOptions options = LTGameSdk.options();
            if (!LTGameUtil.isAnyEmpty(options.getLtAppId(), options.getLtAppKey(),
                    options.getSku(), options.getGoodsID(), options.getmPublicKey(),
                    options.getGoodsType()) && options.getmParams() != null &&
                    options.getmPayTest() != -1 && options.getSelfRequestCode() != -1) {
                platform = new OneStorePlatform(context, options.getLtAppId(), options.getLtAppKey(),
                        options.getmPayTest(), options.getmPublicKey(), options.getSelfRequestCode(),
                        options.getSku(), options.getGoodsID(), options.getmParams(), options.getGoodsType(), target);
            }
            return platform;
        }

        @Override
        public int getPlatformTarget() {
            return Target.PLATFORM_ONE_STORE;
        }

        @Override
        public boolean checkLoginPlatformTarget(int target) {
            return false;
        }

        @Override
        public boolean checkRechargePlatformTarget(int target) {
            return target == Target.RECHARGE_ONE_STORE;
        }
    }
}

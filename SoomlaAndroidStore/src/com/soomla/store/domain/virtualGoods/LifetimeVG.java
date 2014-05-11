/**
 * Copyright (C) 2012-2014 Soomla Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.soomla.store.domain.virtualGoods;

import com.soomla.store.StoreUtils;
import com.soomla.store.data.StorageManager;
import com.soomla.store.purchaseTypes.PurchaseType;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A <code>LifetimeVG</code> is a virtual good that is bought once and kept forever.
 *
 * The <code>LifetimeVG</code>'s characteristics are:
 *  1. Can only be purchased once.
 *  2. Your users cannot have more than one of this item.
 *
 * Real Games Examples: 'No Ads', 'Double Coins'
 *
 * This <code>VirtualItem</code> is purchasable.
 * In case you want this item to be available for purchase in the market (PurchaseWithMarket),
 * you will need to define the item in the market (Google Play, Amazon App Store, etc...).
 *
 * Inheritance:{@link com.soomla.store.domain.virtualGoods.LifetimeVG} >
 * {@link com.soomla.store.domain.virtualGoods.VirtualGood} >
 * {@link com.soomla.store.domain.PurchasableVirtualItem} >
 * {@link com.soomla.store.domain.VirtualItem}
 */
    public class LifetimeVG extends VirtualGood{

    /** Constructor
     *
     * @param mName see parent
     * @param mDescription see parent
     * @param mItemId see parent
     * @param purchaseType see parent
     */
    public LifetimeVG(String mName, String mDescription,
                      String mItemId,
                      PurchaseType purchaseType) {
        super(mName, mDescription, mItemId, purchaseType);
    }

    /**
     * Constructor
     *
     * @param jsonObject see parent
     * @throws JSONException
     */
    public LifetimeVG(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    /**
     * see parent
     *
     * @return see parent
     */
    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject();
    }

    /**
     * Gives your user exactly one <code>LifetimeVG</code>.
     *
     * @param amount the amount of the specific item to be given - if this input is greater than 1,
     *               we force the amount to equal 1, because a <code>LifetimeVG</code> can only be given once.
     * @return 1 to indicate that the user was given the good
     */
    @Override
    public int give(int amount, boolean notify) {
        if(amount > 1) {
            StoreUtils.LogDebug(TAG, "You tried to give more than one LifetimeVG."
                    + "Will try to give one anyway.");
            amount = 1;
        }

        int balance = StorageManager.getVirtualGoodsStorage().getBalance(this);

        if (balance < 1) {
            return StorageManager.getVirtualGoodsStorage().add(this, amount, notify);
        }
        return 1;
    }

    /**
     * Takes from your user exactly one <code>LifetimeVG</code>.
     *
     * @param amount the amount of the specific item to be taken - if this input is greater than 1,
     *               we force amount to equal 1, because a <code>LifetimeVG</code> can only be
     *               given once and therefore, taken once.
     * @return 1 to indicate that the user was given the good
     */
    @Override
    public int take(int amount, boolean notify) {
        if (amount > 1) {
            amount = 1;
        }

        int balance = StorageManager.getVirtualGoodsStorage().getBalance(this);

        if (balance > 0) {
            return StorageManager.getVirtualGoodsStorage().remove(this, amount, notify);
        }
        return 0;
    }

    /**
     * Determines if the user is in a state that allows him to buy a <code>LifetimeVG</code>, by
     * checking his balance of <code>LifetimeVG</code>s.
     * From the definition of a <code>LifetimeVG</code>:
     * If the user has a balance of 0 - he can buy.
     * If the user has a balance of 1 or more - he cannot buy more.
     *
     * @return true if he can buy, false otherwise
     */
    @Override
    protected boolean canBuy() {
        int balance = StorageManager.getVirtualGoodsStorage().getBalance(this);

        return balance < 1;
    }


    /** Private Members **/

    private static String TAG = "SOOMLA LifetimeVG"; //used for Log messages
}

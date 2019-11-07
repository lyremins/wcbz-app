package com.android.deviceinfo.activitys.chat;

import com.android.deviceinfo.activitys.login.UserBean;
import com.android.deviceinfo.bean.BaseResponseBean;

import java.util.List;

/**

 */
public class ContractBean extends BaseResponseBean {


    /**
     * status : 1
     * data : [{"username":"666","user_id":2,"id":2,"city":"上海","registe_time":"2019-10-30 23:40","column_desc":{"gift_mall_desc":"0元好物在这里","game_link":"https://gamecenter.faas.ele.me","game_is_show":1,"game_image_hash":"05f108ca4e0c543488799f0c7c708cb1jpeg","game_desc":"玩游戏领红包"},"point":0,"mobile":"","is_mobile_valid":true,"is_email_valid":false,"is_active":1,"gift_amount":3,"email":"","delivery_card_expire_days":0,"current_invoice_id":0,"current_address_id":0,"brand_member_new":0,"balance":0,"avatar":"default.jpg","__v":0},{"username":"888","user_id":1,"id":1,"city":"上海","registe_time":"2019-10-30 20:52","column_desc":{"gift_mall_desc":"0元好物在这里","game_link":"https://gamecenter.faas.ele.me","game_is_show":1,"game_image_hash":"05f108ca4e0c543488799f0c7c708cb1jpeg","game_desc":"玩游戏领红包"},"point":0,"mobile":"","is_mobile_valid":true,"is_email_valid":false,"is_active":1,"gift_amount":3,"email":"","delivery_card_expire_days":0,"current_invoice_id":0,"current_address_id":0,"brand_member_new":0,"balance":0,"avatar":"default.jpg","__v":0}]
     */

    public List<UserBean.DataBean> data;
}

package com.symaster.mrd.api;

/**
 * @author yinmiao
 * @since 2025/6/23
 */
public interface ChangeListener {


    void change(BasePage oldPage, BasePage newPage);

    void toHome(BasePage oldPage);

}

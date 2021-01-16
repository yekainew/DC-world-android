package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WeekCountBean extends JsonBean {

    private String week_push_all;
    private String week_self_all;
    private String week_all;


    @Getter
    @Setter
    @Accessors(chain = true)
    public static class DataBean extends JsonBean {
        private int hold_area_id;
        private String daily_push_release;
        private String count_create_time;
        private String daily_keep_release;
        private int hold_area_daily_id;
        private String daily_count_release;
    }
}

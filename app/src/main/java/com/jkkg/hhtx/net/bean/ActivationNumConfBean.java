package com.jkkg.hhtx.net.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ActivationNumConfBean {
    protected String num;
    protected boolean isCheckd;
}

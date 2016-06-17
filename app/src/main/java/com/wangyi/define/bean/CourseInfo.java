package com.wangyi.define.bean;

import com.wangyi.function.funchelp.JsonResponseParser;
import org.xutils.http.annotation.HttpResponse;

/**
 * Created by eason on 5/21/16.
 */

@HttpResponse(parser = JsonResponseParser.class)
public class CourseInfo {
    public String name;
    public boolean is;//1:必修，2:选修
    public String subject;
    public String fromUniversity;
}

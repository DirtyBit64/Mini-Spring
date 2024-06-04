package com.minis.web;

import java.util.Date;

import com.minis.beans.CustomDateEditor;

public class DateInitializer implements WebBindingInitializer{
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
    }
}

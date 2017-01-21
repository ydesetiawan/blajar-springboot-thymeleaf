package com.ydes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ydes.persistence.model.TwitterData;
import com.ydes.persistence.repository.TwitterDataRepository;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Controller
public class MainController {

    @Autowired
    private Twitter twitter;
    @Autowired
    private TwitterDataRepository twitterDataRepository;

    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public ModelAndView home() {
        List<TwitterData> twitterDatas = twitterDataRepository
                .findAllByOrderByPostingDateAsc();
        ModelAndView model = new ModelAndView();
        model.addObject("twitter_datas", twitterDatas);
        model.setViewName("home");
        return model;

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error",
                    getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;

    }
    
    @RequestMapping(value = { "/setting" }, method = RequestMethod.GET)
    public ModelAndView setting() {
        List<TwitterData> twitterDatas = twitterDataRepository
                .findAllByOrderByPostingDateAsc();
        ModelAndView model = new ModelAndView();
        model.addObject("twitter_datas", twitterDatas);
        model.setViewName("home");
        return model;

    }

    private String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession()
                .getAttribute(key);

        String error;
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }

        return error;
    }

}

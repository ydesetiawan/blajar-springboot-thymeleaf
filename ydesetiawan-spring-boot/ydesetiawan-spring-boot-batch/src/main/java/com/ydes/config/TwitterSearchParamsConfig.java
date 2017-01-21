package com.ydes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.SearchParameters.ResultType;

/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
@Configuration
@ConfigurationProperties(prefix = "twitter.search.params")
public class TwitterSearchParamsConfig {

	private String query;
	private String lang;
	private String locale;
	private ResultType resultType;
	private String resultTypeVal;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		if ("popular".equalsIgnoreCase(getResultTypeVal())) {
			resultType = ResultType.POPULAR;
		} else if ("mixed".equalsIgnoreCase(getResultTypeVal())) {
			resultType = ResultType.POPULAR;
		} else if ("recent".equalsIgnoreCase(getResultTypeVal())) {
			resultType = ResultType.POPULAR;
		}
		this.resultType = resultType;
	}

	public String getResultTypeVal() {
		return resultTypeVal;
	}

	public void setResultTypeVal(String resultTypeVal) {
		this.resultTypeVal = resultTypeVal;
	}

}

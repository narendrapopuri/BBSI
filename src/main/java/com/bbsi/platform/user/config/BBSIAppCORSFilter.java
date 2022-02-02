package com.bbsi.platform.user.config;

import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.bbsi.platform.common.generic.LogUtils;
/**
 * @author anandaluru
 *
 */

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BBSIAppCORSFilter implements Filter {
	

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
		try {
			HttpServletResponse response = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Expose-Headers", "mfa-user, uid");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Allow-Headers",
					"Content-Type, Accept, X-Requested-With, username, password, Cache-Control, Pragma, Authorization, user-id, uid, otp, mfa, user-key, cresponse, secu-comp-mode");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
	            response.setStatus(HttpServletResponse.SC_OK);
	        } else {
	            chain.doFilter(req, res);
	        }
		} catch (Exception e) {
			  LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "BBSIAppCORSFilter.doFilter"), e.getMessage());
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
		LogUtils.basicDebugLog.accept("BBSIAppCORSFilter.init()");
	}

	@Override
	public void destroy() {
		  LogUtils.basicDebugLog.accept("BBSIAppCORSFilter.destroy()");

	}

}

package com.cloud.demo.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * Zuul 的请求过滤功能
 *
 * @author : cuixiuyin
 * @date : 2019/7/17
 */
public class TokenFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    /**
     * 过滤器的类型，它决定过滤器在请求的哪个生命周期中执行。这里定义为 pre, 代表会在请求被路由之前执行。路由类型有下面几种：
     * <p>
     * - pre: 可以在请求被路由之前调用。
     * - routing: 在路由请求时被调用。
     * - post: 在 routing 和 error 过滤器之后被调用。
     * - error: 处理请求时发生错误时被调用。
     *
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器的执行顺序。当请求在一个阶段中存在多个过滤器时，需要根据该方法返回的值来依次执行，数值越小，优先级越高。
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断该过滤器是否需要被执行。这里我们直接返回了true, 因此该过滤器对所有请求都会生效。实际运用中我们可以利用该函数来指定过滤器
     * 的有效范围。
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的具体执行逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            logger.warn("token is empty");
            // 令 zuul 过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            // 设置返回的错误码
            ctx.setResponseStatusCode(401);
            // 设置返回的 body
            ctx.setResponseBody("");
            return null;
        }
        logger.info("access token is ok");
        return null;
    }
}

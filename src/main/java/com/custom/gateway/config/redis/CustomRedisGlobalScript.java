package com.custom.gateway.config.redis;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptingException;
import org.springframework.lang.Nullable;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.util.Assert;

import java.io.IOException;

public class CustomRedisGlobalScript<T> implements RedisScript<T>, InitializingBean {

    private final Object shaModifiedMonitor = new Object();

    private @Nullable
    ScriptSource scriptSource;
    private @Nullable
    String sha1;
    private @Nullable
    Class<T> resultType;

    /**
     * Creates a new {@link org.springframework.data.redis.core.script.DefaultRedisScript}
     */
    public CustomRedisGlobalScript() {
    }

    /**
     * Creates a new {@link org.springframework.data.redis.core.script.DefaultRedisScript}
     *
     * @param script must not be {@literal null}.
     * @since 2.0
     */
    public CustomRedisGlobalScript(String script) {
        this(script, null);
    }

    /**
     * Creates a new {@link org.springframework.data.redis.core.script.DefaultRedisScript}
     *
     * @param script     must not be {@literal null}.
     * @param resultType can be {@literal null}.
     */
    public CustomRedisGlobalScript(String script, @Nullable Class<T> resultType) {

        this.setScriptText(script);
        this.resultType = resultType;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.state(this.scriptSource != null, "Either script, script location," + " or script source is required");
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.script.RedisScript#getSha1()
     */
    public String getSha1() {

        synchronized (shaModifiedMonitor) {
            if (sha1 == null || scriptSource.isModified()) {
                this.sha1 = DigestUtils.sha1DigestAsHex(getScriptAsString());
            }
            return sha1;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.script.RedisScript#getResultType()
     */
    @Nullable
    public Class<T> getResultType() {
        return this.resultType;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.script.RedisScript#getScriptAsString()
     */
    public String getScriptAsString() {

        try {
            return scriptSource.getScriptAsString();
        } catch (IOException e) {
            throw new ScriptingException("Error reading script text", e);
        }
    }

    /**
     * @param resultType The script result type. Should be one of Long, Boolean, List, or deserialized value type. Can be
     *                   null if the script returns a throw-away status (i.e "OK")
     */
    public void setResultType(Class<T> resultType) {
        this.resultType = resultType;
    }

    /**
     * @param scriptText The script text
     */
    public void setScriptText(String scriptText) {
        this.scriptSource = new StaticScriptSource(scriptText);
    }

    /**
     * @param scriptLocation The location of the script
     */
    public void setLocation(Resource scriptLocation) {
        this.scriptSource = new ResourceScriptSource(scriptLocation);
    }

    /**
     * @param scriptSource A @{link {@link ScriptSource} pointing to the script
     */
    public void setScriptSource(ScriptSource scriptSource) {
        this.scriptSource = scriptSource;
    }
}

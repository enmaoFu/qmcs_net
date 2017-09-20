package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/6/20
 * Author ：chen
 */

public class AliyunSTS {


    /**
     * accessKeyId : STS.DiKsYWmaqL5asyzHyV71FMuJN
     * accessKeySecret : Ejq3Xf8YtdYC2YzJimtDiitcmgrttjMttf7PYyyCyezW
     * bucketName : qmcsimg
     * endpoint : http://oss-cn-beijing.aliyuncs.com
     * expiration : 2017-06-20T02:48:22Z
     * saveDirectory : package
     * securityToken : CAIS9wF1q6Ft5B2yfSjIpar+OOPjgL5Q+/eKcV/LrHkDO75qorDhrDz2IHBIfHFuBeEYv/s/lGpY5v4elqB6T55OSAmcNZIofj+lAqfiMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl2DwvuPvlkpPNtEqC1ACn8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMaqP8u0vAVp2aY7oHHUwkJvw/hKu/f6dxqIxV0b6Q9HapVvEdCViLIKUW4GoABBUJfkJoSFmnYaCav5Lnwovl+wfHs0f/l1Qfmyzh2aZF3ilZBjpW3Sje/kf4NDuOTTiEie9wO03oDQJUTTNLU5xUnnML526KoG1a7c0ke9cPHmyFgnmZNK7iT0ShPQaapDEyFgxVcyqIOCtnFISR+TNBvFNcp99KiAMbFbL4Wc6A=
     */

    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String endpoint;
    private String expiration;
    private String saveDirectory;
    private String securityToken;
    private String callbackUrl;
    private String callbackBody;

    public String getCallbackBody() {
        return callbackBody;
    }

    public void setCallbackBody(String callbackBody) {
        this.callbackBody = callbackBody;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getSaveDirectory() {
        return saveDirectory;
    }

    public void setSaveDirectory(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}

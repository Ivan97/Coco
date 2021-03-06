/*
 * Copyright [2018] [IoooTech,Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package tech.iooo.boot.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import lombok.AllArgsConstructor;
import tech.iooo.boot.core.utils.Assert;

/**
 * Created on 2018/10/30 5:03 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=miroku">Ivan97</a>
 */
@Sharable
@AllArgsConstructor
public class SslServerChannelInitializer extends ChannelInitializer<Channel> {

  private final SSLContext jdkSslContext;
  private final SslContext nettySslContext;
  private final boolean startTls;
  private final boolean needClientAuth;

  public SslServerChannelInitializer(SSLContext jdkSslContext) {
    this.jdkSslContext = jdkSslContext;
    this.startTls = false;
    this.needClientAuth = false;
    this.nettySslContext = null;
  }

  public SslServerChannelInitializer(SSLContext jdkSslContext, boolean needClientAuth) {
    this.jdkSslContext = jdkSslContext;
    this.startTls = false;
    this.needClientAuth = needClientAuth;
    this.nettySslContext = null;
  }

  public SslServerChannelInitializer(SSLContext jdkSslContext, boolean needClientAuth, boolean startTls) {
    this.jdkSslContext = jdkSslContext;
    this.startTls = startTls;
    this.needClientAuth = needClientAuth;
    this.nettySslContext = null;
  }

  public SslServerChannelInitializer(SslContext nettySslContext) {
    this.nettySslContext = nettySslContext;
    this.startTls = false;
    this.needClientAuth = false;
    this.jdkSslContext = null;
  }

  public SslServerChannelInitializer(SslContext nettySslContext, boolean needClientAuth) {
    this.nettySslContext = nettySslContext;
    this.needClientAuth = needClientAuth;
    this.startTls = false;
    this.jdkSslContext = null;
  }

  public SslServerChannelInitializer(SslContext nettySslContext, boolean needClientAuth, boolean startTls) {
    this.nettySslContext = nettySslContext;
    this.needClientAuth = needClientAuth;
    this.startTls = startTls;
    this.jdkSslContext = null;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    SSLEngine sslEngine = null;
    if (Objects.nonNull(nettySslContext)) {
      sslEngine = nettySslContext.newEngine(ch.alloc());
    } else if (Objects.nonNull(jdkSslContext)) {
      sslEngine = jdkSslContext.createSSLEngine();
      // 配置为 server 模式
      sslEngine.setUseClientMode(false);
    }
    Assert.notNull(sslEngine, "SSLEngine is null");
    //false为单向认证，true为双向认证  
    sslEngine.setNeedClientAuth(needClientAuth);
    // 选择需要启用的 SSL 协议，如 SSLv2 SSLv3 TLSv1 TLSv1.1 TLSv1.2 等
    sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
    // 选择需要启用的 CipherSuite 组合，如 ECDHE-ECDSA-CHACHA20-POLY1305 等
    sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
    ch.pipeline().addFirst("iooo-default-ssl", new SslHandler(sslEngine, startTls));
  }
}
